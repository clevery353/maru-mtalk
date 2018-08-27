package kr.co.marueng.mtalk;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * 로그인/로그아웃 세션을 관리
 * @author 방소은
 */
public class SessionMonitoring implements HttpSessionAttributeListener, ServletContextListener {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	// HttpSessionAttributeListener는 로그인 로그아웃했을때의 세션을 잡음.
	// HttpSessionListener는 홈페이지 접속시 세션을 잡음.

	public static List<String> loginMemberList = new ArrayList<>(); // 현재 로그인중인 회원 목록

	public static List<String> getLoginMemberList() {
		return loginMemberList;
	}

	public static void setLoginMemberList(List<String> loginMemberList) {
		SessionMonitoring.loginMemberList = loginMemberList;
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {

		HttpSession session = se.getSession();
		if ("member".equals(se.getName())) {
			logger.error("{} 로그인 세션", session.getId());
			MemberVO member = (MemberVO) session.getAttribute("member");
			loginMemberList.add(member.getMem_id());
			System.err.println("현재접속자목록"+loginMemberList);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {
		HttpServletRequest request = null;
		try{
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			HttpSession session = se.getSession();
			if ("member".equals(se.getName())) {
				logger.error("{} 로그아웃 세션", session.getId());
				MemberVO member = (MemberVO) se.getValue();
				loginMemberList.remove(member.getMem_id());
				System.err.println("현재접속자목록"+loginMemberList);
			}
		}catch(NullPointerException e){
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.setAttribute("loginMemberList", loginMemberList);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// application 종료 시

	}

}