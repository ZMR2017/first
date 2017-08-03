package cn.itcast.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 本地Session
 * @author Administrator
 *
 */
public class HttpSessionProvider implements SessionProvider {

	public void setAttribute(HttpServletRequest request, String name,
			Serializable value) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();//true   cookie jsessionid
		session.setAttribute(name, value);
		
		
	}

	public Serializable getAttribute(HttpServletRequest request, String name) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		if(session != null){
			return (Serializable) session.getAttribute(name);
		}
		
		return null;
	}

	public void logout(HttpServletRequest request) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
	}

	public String getSessionId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		//request.getRequestedSessionId(); //http:localhost:8080/html/sfsf.shtml?JESSIONID=eweweq23323
		
		
		return request.getSession().getId();
	}

}
