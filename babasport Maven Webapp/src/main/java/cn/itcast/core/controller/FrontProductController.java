package cn.itcast.core.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import cn.itcast.common.page.Pagination;
import cn.itcast.common.web.session.SessionProvider;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Feature;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.Type;
import cn.itcast.core.query.product.BrandQuery;
import cn.itcast.core.query.product.FeatureQuery;
import cn.itcast.core.query.product.ProductQuery;
import cn.itcast.core.query.product.SkuQuery;
import cn.itcast.core.query.product.TypeQuery;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ColorService;
import cn.itcast.core.service.product.FeatureService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.product.TypeService;

/**
 * 前台商品列表 测试 商品详情页面
 * 
 * @author Administrator
 * 
 */
@Controller
public class FrontProductController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private FeatureService featureService;
	
	@Autowired
	private SkuService skuService;
	
	@Autowired
	private SessionProvider sessionProvider;


	// 商品列表页面
	@RequestMapping(value = "/product/display/list.shtml")
	public String list(Integer pageNo, Integer brandId, String brandName,Integer typeId, String typeName,ModelMap modelMap) {

		
		

		// 加载商品属性
		FeatureQuery featureQuery = new FeatureQuery();

		List<Feature> features = featureService.getFeatureList(featureQuery);

		modelMap.addAttribute("features", features);

		// 分页参数
		StringBuilder params = new StringBuilder();
		// 设置页号
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 设置每页数
		productQuery.setPageSize(Product.FRONT_PAGE_SIZE);
		//设置Id倒排
		productQuery.orderbyId(false);
		// 条件TODO
		boolean flag = false;
		// 条件Map容器
		Map <String,String> query = new LinkedHashMap<String, String>();
		
		
		
		if(null!=brandId){
			productQuery.setBrandId(brandId);
			flag = true;
			query.put("品牌", brandName);
			modelMap.addAttribute("brandId", brandId);
			modelMap.addAttribute("brandName", brandName);
			params.append("&").append("brandId=").append(brandId).append("&brandName=").append(brandName);
		}else{
			BrandQuery brandQuery = new BrandQuery();

			brandQuery.setFields("id,name");
			brandQuery.setIsDisplay(1);
			// 加载品牌
			List<Brand> brands = brandService.getBrandList(brandQuery);

			modelMap.addAttribute("brands", brands);
		}
		//类型ID
		if(null != typeId){
			productQuery.setTypeId(typeId);
			flag = true;
			query.put("类型", typeName);
			modelMap.addAttribute("typeId", typeId);
			modelMap.addAttribute("typeName", typeName);
			params.append("&").append("typeId=").append(typeId).append("&typeName=").append(typeName);
		}else{
			// 加载商品类型
			TypeQuery typeQuery = new TypeQuery();
			// 指定查询哪些字段
			typeQuery.setFields("id,name");
			typeQuery.setIsDisplay(1);
			typeQuery.setParentId(1);
			List<Type> types = typeService.getTypeList(typeQuery);

			modelMap.addAttribute("types", types);

		}
		
		//条件
		modelMap.addAttribute("query", query);
		
		modelMap.addAttribute("flag", flag);
		
		Pagination pagination = productService.getProductListWithPage(productQuery);
		// 分页页面展示
		String url = "/product/display/list.shtml?";
		pagination.pageView(url, params.toString());

		modelMap.addAttribute("pagination", pagination);

		return "product/product";
	}
	
	//跳转商品详情页
	@RequestMapping( value = "/product/detail.shtml")
	public String detail(Integer id,ModelMap modelMap){
		//商品加载
		Product product = productService.getProductByKey(id);
		
		modelMap.addAttribute("product", product);
		
	
		List<Sku> skus = skuService.getStock(id);
		modelMap.addAttribute("skus", skus);
		//去重复
		List<Color> colors = new ArrayList<Color>();
		
		//遍历Sku
		for (Sku sku : skus) {
			//判断集合中是否已经有此颜色对象
			if(!colors.contains(sku.getColor())){
				colors.add(sku.getColor());
			}
		}
		
		
		modelMap.addAttribute("colors", colors);
		
		return "product/productDetail";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// 第一个Springmvc

	@RequestMapping(value = "/test/springmvc.do")
	public String test(String name, Date birthday) {

		System.out.println();
		return "";
	}

	/*
	 * @InitBinder public void initBinder(WebDataBinder binder, WebRequest
	 * request) { //转换日期格式 DateFormat dateFormat = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * 
	 * binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,
	 * true));
	 * 
	 * 
	 * }
	 */

}
