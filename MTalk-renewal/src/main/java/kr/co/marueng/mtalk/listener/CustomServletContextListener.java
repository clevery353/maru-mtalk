package kr.co.marueng.mtalk.listener;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.marueng.Constants;
import kr.co.marueng.mtalk.vo.MemberVO;

@WebListener
public class CustomServletContextListener implements ServletContextListener {
	Logger logger = LoggerFactory.getLogger(this.getClass()); 

  
	public void contextInitialized(ServletContextEvent sce)  {
		// 어플리케이션 전체에서 제일 먼저 실행
		// cPath 
		ServletContext application = sce.getServletContext();
		application.setAttribute("cPath", application.getContextPath());
		application.setAttribute(Constants.CURRENTUSERCOUNTNAME, new Integer(0));
		application.setAttribute(Constants.ALLUSERCOUNTNAME, new Integer(0));
		//접속자 리스트(Collection,Map<String,MemberVO>)
		application.setAttribute(Constants.CURRENTUSERLISTNAME, new HashMap<String,MemberVO>());
		logger.info("{} 컨텍스트 로딩 완료", application.getContextPath());
	}
	
    public void contextDestroyed(ServletContextEvent sce)  {
    	ServletContext application = sce.getServletContext();
    	logger.info("{} 컨텍스트 소멸 완료", application.getContextPath());
    }

	
}
