package kr.co.marueng.mtalk.listener;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.marueng.Constants;

@WebListener
public class CustomSessionListener implements HttpSessionListener, HttpSessionAttributeListener{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static Integer allUserCount = 0;
	
    public void sessionCreated(HttpSessionEvent se)  {
    	//세션 생성 후 1. 현재 접속자 수 증가, 2. 누적 접속자 수 증가
    	HttpSession session =  se.getSession();
    	logger.info("{} 세션생성",session.getId());
    	ServletContext application = session.getServletContext();
    	Integer currentUserCount = (Integer) application.getAttribute(Constants.CURRENTUSERCOUNTNAME);
    	Integer allUserCount = (Integer) application.getAttribute(Constants.ALLUSERCOUNTNAME);
    	application.setAttribute(Constants.CURRENTUSERCOUNTNAME, new Integer(currentUserCount+1));
    	application.setAttribute(Constants.ALLUSERCOUNTNAME, new Integer(allUserCount+1));
    	allUserCount = (Integer) application.getAttribute(Constants.ALLUSERCOUNTNAME);
    	this.allUserCount = allUserCount;
    }

    public void sessionDestroyed(HttpSessionEvent se)  {
    	//세션 소멸됨 1. 현재 접속자 수 감소
    	HttpSession session =  se.getSession();
    	logger.info("{} 세션소멸", session.getId());
    	ServletContext application = session.getServletContext();
    	Integer currentUserCount = (Integer) application.getAttribute(Constants.CURRENTUSERCOUNTNAME);
    	application.setAttribute(Constants.CURRENTUSERCOUNTNAME, new Integer(currentUserCount-1));
    }
    //LifeCycle event handler end
    //---------------------------------------------------
    // Change to attributes event handler start

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		//속성명이 authMember 인 경우, 접속자(MemberVO) 리스트 증가
//		if("authMember".equals(event.getName())){
//			MemberVO authMember =  (MemberVO) event.getValue();
//			ServletContext application = event.getSession().getServletContext();
//			Map<String, MemberVO> userList = (Map<String, MemberVO>) application.getAttribute(Constants.CURRENTUSERLISTNAME);
//			userList.put(authMember.getMem_id(), authMember);
//			
//		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		//속성명이 authMember 인 경우, 접속자(MemberVO) 리스트에서 제거
//		if("authMember".equals(event.getName())){
//			MemberVO authMember =  (MemberVO) event.getValue();
//			ServletContext application = event.getSession().getServletContext();
//			Map<String, MemberVO> userList = (Map<String, MemberVO>) application.getAttribute(Constants.CURRENTUSERLISTNAME);
//			userList.remove(authMember.getMem_id());
//		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		
	}
	
}
