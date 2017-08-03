package cn.itcast.core.controller.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.service.product.BrandService;

/**
 * 品牌
 * @author bx
 *
 */
@Controller
public class BrandController {
	
	@Autowired
	private BrandService brandService;

	
	//品牌列表页面
	@RequestMapping(value = "/brand/list.do" )
	public String list(String name,Integer isDisplay,Integer pageNo,ModelMap modelMap){
		//参数
		StringBuilder params=new StringBuilder();
		
		
		Brand brand = new Brand();
		//判断传进来的名称是否为空并且还要判断 是否为空串 blank "" "  " emtpy ""
   			if(StringUtils.isNotBlank(name)){
   				brand.setName(name);
   				params.append("name=").append(name);
   			}
   			//是 不是
   			if(isDisplay != null){
   				
   				brand.setIsDisplay(isDisplay);
   				params.append("&").append("isDisplay=").append(isDisplay);
   			}else{
   				
   				brand.setIsDisplay(1);
   				params.append("&").append("isDisplay=").append(1);
   			}
   			
   			//如果页号为null或小于 1 置为1
   			//页号
   			brand.setPageNo(Pagination.cpn(pageNo));

   			//每页数
   			brand.setPageSize(5);
   			
   			//分页对象
   			Pagination pagination = brandService.getBrandListWithPage(brand);
   			
   			//分页展示 /brand/list.do?name=兰博伊人（lanboyiren）    &isDisplay=1&pageNo=2
   			
   			String url = "/brand/list.do";
   			pagination.pageView(url, params.toString());
   			
   			modelMap.addAttribute("pagination", pagination);
   			modelMap.addAttribute("isDisplay", isDisplay);
   			modelMap.addAttribute("name", name);
			
		return "brand/list";
	}
	
	//跳转品牌添加页面
	@RequestMapping(value = "/brand/toadd.do")
	public String toAdd(){
		
		return "brand/add";
	}
	
	//添加品牌
	@RequestMapping(value = "/brand/add.do")
	public String add(Brand brand){
		//添加开始
		brandService.addBrand(brand);
		
		return "redirect:/brand/list.do";
	}
	
	//删除一个品牌
	@RequestMapping(value = "/brand/delete.do")
	public String delete(Integer id ,String name,Integer isDisplay,ModelMap modelMap){
		//删除
		brandService.deleteBrandByKey(id);
		
		if(StringUtils.isNotBlank(name)){
			
			modelMap.addAttribute("name",name);
		}
		if(isDisplay!=null){
			modelMap.addAttribute("isDisplay", isDisplay);
		}
		
		return "redirect:/brand/list.do";
	}
	
	//删除一个品牌
	@RequestMapping(value = "/brand/deletes.do")
	public String deletes(Integer[] ids ,String name,Integer isDisplay,ModelMap modelMap){
		//删除
		brandService.deleteBrandByKeys(ids);
		
		if(StringUtils.isNotBlank(name)){
			
			modelMap.addAttribute("name",name);
		}
		if(isDisplay!=null){
			modelMap.addAttribute("isDisplay", isDisplay);
		}
		
		return "redirect:/brand/list.do";
	}
	//去修改页面
	@RequestMapping(value = "/brand/toedit.do")
	public String toEdit(Integer id, ModelMap modelMap){
		
		Brand brand = brandService.getBrandByKey(id);
		
		modelMap.addAttribute("brand", brand);
		
		return "/brand/edit";
	}
	
	//修改页面
	@RequestMapping(value = "/brand/edit.do")
	public String edit(Brand brand, ModelMap modelMap){
		
		brandService.updateBrandByKey(brand);
		
		return "redirect:/brand/list.do";
	}
	
	
}
