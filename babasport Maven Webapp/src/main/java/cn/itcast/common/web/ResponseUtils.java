package cn.itcast.common.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * 异步返回各种格式
 * json
 * xml
 * text
 * @author bx
 *
 */
public class ResponseUtils {

	//发送内容"application/json;charset=UTF-8"
	public static void render(HttpServletResponse response,String contentType,String text){
		
		response.setContentType(contentType);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//如果发送是Json
	public static void renderJson(HttpServletResponse response,String text){
		render(response, "application/json;charset=UTF-8", text);
	}
	
	//如果发送xml
	public static void renderXml(HttpServletResponse response,String text){
		render(response, "text/xml;charset=UTF-8", text);
	}
	
	//如果发送text
	public static void renderText(HttpServletResponse response,String text){
		render(response, "text/plain;charset=UTF-8", text);
	}

}
