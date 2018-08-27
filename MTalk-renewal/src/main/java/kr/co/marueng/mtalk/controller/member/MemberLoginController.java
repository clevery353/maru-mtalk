package kr.co.marueng.mtalk.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.marueng.mtalk.MySessionAttributeListener;
import kr.co.marueng.mtalk.service.chat.IChatService;
import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 조문경
 * @DATE 2018. 7. 11.
 */


@Controller
@RequestMapping("/member")
public class MemberLoginController { // 멤버 로그인할 때
	@Inject
	ImemberService service;
	@Inject
	IChatService chatService;
	
	@RequestMapping(value = "/GetMemInfo", produces="application/json; charset=UTF-8",  method = RequestMethod.POST)
	@ResponseBody
	public MemberVO GetMemInfo(HttpServletResponse resp, String mem_id) throws Exception {
	
		System.out.println("mem_id - "+mem_id);
		MemberVO vo = service.memberLogin(mem_id);
		System.out.println("GetMemInfo_name = "+vo.getMem_name());
		
		return vo;
		
		}
	
	//로그인
	@RequestMapping(value = "/login", produces="application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, String> process(HttpServletResponse resp, MemberVO member,
			HttpSession session) throws Exception {
		
		String mem_id = member.getMem_id();
		List<String> chatMemberList = new ArrayList<String>();
		chatMemberList.add(mem_id);
		Map<String, String> memNameMap = service.memberInfoName(chatMemberList);
		String mem_name = memNameMap.get(mem_id);
		String mem_pw = member.getMem_pw();
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		MemberVO authMember = service.memberLogin(mem_id);//디비에 저장된 회원의 정보
		try{
			if(authMember.getMem_status().equals("Y"))
			{
				if(mem_pw.equals(authMember.getMem_pw())){
					session.setAttribute("mem_id", mem_id); //세션스코프에 회원정보를 넣어준
					session.setAttribute("mem_name", mem_name); //세션스코프에 회원정보를 넣어준
					System.err.println(authMember.getMem_type());
					if(authMember.getMem_type().equals("회원"))
					{
						map.put("result","success_member");
						strb.append(mapper.writeValueAsString(map));
						session.setAttribute("mem_type","member");
						System.out.println("member_type2 : "+authMember.getMem_type());
					}
					else if(authMember.getMem_type().equals("관리자")){
						map.put("result","success_manager");
						strb.append(mapper.writeValueAsString(map));
						session.setAttribute("mem_type","manager");
						System.out.println("member_type : "+authMember.getMem_type());
						//session.setAttribute("mem_type","manager");
					}
					//goPage="index";
				}
			}else if(authMember.getMem_status().equals("W")){
				map.put("result","Waiting");
				strb.append(mapper.writeValueAsString(map));
			}else if(authMember.getMem_status().equals("R")){
				map.put("result","Reject");
				strb.append(mapper.writeValueAsString(map));
			}else{
				map.put("result","delete");
				strb.append(mapper.writeValueAsString(map));
			}
		}catch(NullPointerException e){
		}
		//return goPage;
		return map;
	}

	@RequestMapping(value = "/logout", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String logout(HttpServletRequest req, HttpServletResponse resp,MemberVO vo, Errors error,  
			HttpSession session) throws Exception // 멤버 로그아웃할 때
	{
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		try
		{
			session.removeAttribute("mem_id");
			session.removeAttribute("mem_name");
			session.removeAttribute("mem_type");
			map.put("result", "success");
			strb.append(mapper.writeValueAsString(map));
			
		}catch(Exception e){
			map.put("result", "failure");
			strb.append(mapper.writeValueAsString(map));
		}	
		
		return strb.toString();
	}
	
	
	@RequestMapping(value = "/alertLogin", produces="application/json; charset=UTF-8",  method = RequestMethod.POST)
	@ResponseBody
	public List<FriendVO> alertLogin(String mid,  
			HttpSession session) throws Exception
	{
		String mem_id = mid;
		System.out.println("alertLoginController - memid : "+mem_id);
		
		String result = "";
		
		List<FriendVO>list_f = (List<FriendVO>)service.alertLogin(mem_id);
		System.out.println("list : "+list_f.size());
		
		return list_f;
	}
	
	public void addLoginManager(String mem_id)
	{
		List<String>loginMemberList = MySessionAttributeListener.getLoginMemberList();
		loginMemberList.add(mem_id);
		MySessionAttributeListener.setLoginMemberList(loginMemberList);
	}
	
}
