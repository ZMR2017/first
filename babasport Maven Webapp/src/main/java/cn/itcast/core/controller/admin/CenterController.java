package cn.itcast.core.controller.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;


@Controller
@RequestMapping(value="/control")
public class CenterController {
	//第一个Springmvc
	
	@RequestMapping(value="/test/springmvc.do" )
	public String test(String name , Date birthday){
		
		System.out.println();
		
		return "";
	}

/*	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		//转换日期格式
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat, true));
		
		
	}*/
	
	//跳转入口页面
	@RequestMapping(value="/index.do" )
	public String index(String name , Date birthday){
		
		System.out.println();
		
		return "index";
	}
	
	//跳转头页面
	@RequestMapping(value="/top.do" )
	public String top(String name , Date birthday){
		
		System.out.println();
		
		return "top";
	}
	
	//跳转body页面
	@RequestMapping(value="/main.do" )
	public String main(String name , Date birthday){
		
		System.out.println();
		
		return "main";
	}
	
	//跳转left页面
	@RequestMapping(value="/left.do" )
	public String left(String name , Date birthday){
		
		System.out.println();
		
		return "left";
	}
	
	//跳转body页面
	@RequestMapping(value="/right.do" )
	public String right(String name , Date birthday){
		
		System.out.println();
		
		return "right";
	}


}
