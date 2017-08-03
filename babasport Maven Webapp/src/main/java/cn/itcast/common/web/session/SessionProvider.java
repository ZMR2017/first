package cn.itcast.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * session  供应类
 * @author Administrator
 *
 */
public interface SessionProvider {

	//往Session 设置值
	//那么 Constants buyer_session
	//value 用户对象
	public void setAttribute(HttpServletRequest request,String name,Serializable value);
	
	//从Session中取值
	public Serializable getAttribute(HttpServletRequest request,String name); 
	
	//退出登录
	public void logout(HttpServletRequest request);
	
	//获取SessionId
	public String getSessionId(HttpServletRequest request);
	
}
