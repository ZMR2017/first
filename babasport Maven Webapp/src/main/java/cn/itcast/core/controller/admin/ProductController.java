package cn.itcast.core.controller.admin;

import java.awt.image.BandedSampleModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.Type;
import cn.itcast.core.query.product.BrandQuery;
import cn.itcast.core.query.product.ColorQuery;
import cn.itcast.core.query.product.FeatureQuery;
import cn.itcast.core.query.product.ProductQuery;
import cn.itcast.core.query.product.TypeQuery;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ColorService;
import cn.itcast.core.service.product.FeatureService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.product.TypeService;
import cn.itcast.core.service.staticpage.StaticPageService;
/**
 * 后台商品管理
 * 商品列表
 * 商品添加
 * 商品上架
 * 
 * @author bx
 *
 */
@Controller
public class ProductController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private FeatureService featureService;
	
	@Autowired
	private ColorService colorService;
	
	@Autowired
	private StaticPageService staticPagrService;
	
	@Autowired
	private SkuService skuService; 
	
	//商品列表
	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo,String name,Integer brandId,Integer isShow, ModelMap modelMap){
		BrandQuery brandQuery = new BrandQuery();
	
		brandQuery.setFields("id,name");
		brandQuery.setIsDisplay(1);
		//加载品牌
		List<Brand> brands = brandService.getBrandList(brandQuery);
		
		modelMap.addAttribute("brands",brands);
		
		//分页参数
		StringBuilder params = new StringBuilder();
		
		//商品条件对象
		ProductQuery productQuery = new ProductQuery();
		//1、判断条件是否为NUll
		if(StringUtils.isNotBlank(name)){
			productQuery.setName(name);
			//要求模糊查询
			productQuery.setNameLike(true);
			
			params.append("name=").append(name);
		
			//回显查询条件
			modelMap.addAttribute("name", name);
			
		}
		//判断品牌ID
		if(null != brandId){
			productQuery.setBrandId(brandId);
			
			params.append("&brandId=").append(brandId);
			
			//回显查询条件
			modelMap.addAttribute("brandId", brandId);
		}
		//判断上下架
		if(null != isShow){
			productQuery.setIsShow(isShow);
			params.append("&isShow=").append(isShow);
		
			//回显查询条件
			modelMap.addAttribute("isShow", isShow);
		
		}else{
			productQuery.setIsShow(0);
			params.append("&isShow=").append(isShow);
			
			//回显查询条件
			modelMap.addAttribute("isShow", 0);
		
		}
		
		//设置页号
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//设置每页数
		productQuery.setPageSize(5);
		//按照ID倒排
		productQuery.orderbyId(false);
		
		//加载有分页的商品
		Pagination pagination = productService.getProductListWithPage(productQuery);
		
		//分页页面展示
		String url ="/product/list.do?";
		pagination.pageView(url, params.toString());
		
		modelMap.addAttribute("pagination", pagination);
		
		
		return "/product/list";
	}
	
	//去添加页面
	@RequestMapping(value ="/product/toadd.do")
	public String toAdd(ModelMap modelMap){
		//加载商品类型
		TypeQuery typeQuery = new TypeQuery();
		//指定查询哪些字段
		typeQuery.setFields("id,name");
		typeQuery.setIsDisplay(1);
		typeQuery.setParentId(1);
		List<Type> types = typeService.getTypeList(typeQuery);
		
		modelMap.addAttribute("types",types);
		
		BrandQuery brandQuery = new BrandQuery();
		
		brandQuery.setFields("id,name");
		brandQuery.setIsDisplay(1);
		//加载品牌
		List<Brand> brands = brandService.getBrandList(brandQuery);
		
		modelMap.addAttribute("brands",brands);
		
		//加载商品属性
		FeatureQuery featureQuery = new FeatureQuery();
		
		List<Feature> features = featureService.getFeatureList(featureQuery);
		
		modelMap.addAttribute("features",features);
		
		//加载颜色
		ColorQuery colorQuery = new ColorQuery();
		
		colorQuery.setParentId(0);
		
		List<Color> colors = colorService.getColorList(colorQuery);
		
		modelMap.addAttribute("colors",colors);
		
		return "product/add";
	}
	
	//商品添加
	@RequestMapping(value = "/product/add.do")
	public String add(Product product ,Img img){
		//1 、商品表 图片表 、 Sku表
		
		
		product.setImg(img);
		//传商品对象到Service
		productService.addProduct(product);
		
		return "redirect:/product/list.do";
	}
	
	//上架
	@RequestMapping(value = "/product/isShow.do")
	public String isShow(Integer[] ids,ModelMap modelMap,Integer pageNo,String name,Integer brandId,Integer isShow){
		//实例化商品
		Product product = new Product();
		product.setIsShow(1);
		//上架
		if(null != ids && ids.length>0){
			for (Integer id : ids) {
				product.setId(id);
				//修改上架状态
				productService.updateProductByKey(product);
				//TODO 静态化
				Map<String,Object> root = new HashMap<String, Object>();
				//设置值
				//商品加载
				Product p = productService.getProductByKey(id);
				
				root.put("product", p);
				
			
				List<Sku> skus = skuService.getStock(id);
				root.put("skus", skus);
				//去重复
				List<Color> colors = new ArrayList<Color>();
				
				//遍历Sku
				for (Sku sku : skus) {
					//判断集合中是否已经有此颜色对象
					if(!colors.contains(sku.getColor())){
						colors.add(sku.getColor());
					}
				}
				
				root.put("colors", colors);
				staticPagrService.productIndex(root, id);
			}
		}
		
		
		//判断
		if(null != pageNo){
			modelMap.addAttribute("pageNo", pageNo);
		}
		if(StringUtils.isNotBlank(name)){
			modelMap.addAttribute("name", name);
		}
		if(null != brandId){
			modelMap.addAttribute("brandId", brandId);
		}
		if(null != isShow){
			modelMap.addAttribute("isShow", isShow);
		}
		
		
		return "redirect:/product/list.do";
	}
	
}
