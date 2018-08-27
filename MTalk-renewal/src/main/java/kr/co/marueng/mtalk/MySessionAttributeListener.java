package kr.co.marueng.mtalk;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import kr.co.marueng.mtalk.service.member.ImemberService;

public final class MySessionAttributeListener implements  HttpSessionAttributeListener{

	@Inject
	ImemberService service; // db에 접근하기 위한 서비스 객체
	
	public static List<String> loginMemberList = new ArrayList<>();//로그인한 멤버를 담아두기 위한 리스트
	
	public static void setLoginMemberList(List<String> loginMemberList) {
	 	MySessionAttributeListener.loginMemberList = loginMemberList;
	}
	
	public static List<String> getLoginMemberList() { 
	      return loginMemberList;
	}
	

	public MySessionAttributeListener(){}
	@Override
	public void attributeAdded(HttpSessionBindingEvent sessionBindingEvent) { //세션이 생성될때마다 호출됨. = 사용자가 로그인할때마다 호출
		// TODO Auto-generated method stub
		HttpSession session = sessionBindingEvent.getSession();
		System.out.println("[SessionAttr]"+new java.util.Date()+"Attribute added, session "+session+":"+sessionBindingEvent.getName()+"="+sessionBindingEvent.getValue());
		
		Scheduler s = new Scheduler();
		s.ReceivedSession_up();
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent sessionBindingEvent) { //사용자가 로그아웃할 떄마다 호출
		// TODO Auto-generated method stub
		HttpSession session = sessionBindingEvent.getSession();
		System.out.println("[SessionAttr]"+new java.util.Date()+"Attribute removed, session "+session+":"+sessionBindingEvent.getName());
		
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent sessionBindingEvent) {
		// TODO Auto-generated method stub
		
	}

	
}
