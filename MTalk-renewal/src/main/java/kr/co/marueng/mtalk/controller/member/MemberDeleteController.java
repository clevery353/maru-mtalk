package kr.co.marueng.mtalk.controller.member;

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

import kr.co.marueng.mtalk.service.chat.IChatService;
import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberDeleteController {
//	메서드 생성후 메서드에 매핑 걸 때
//	@RequestMapping(value = "memberDelete",  produces = "application/json; charset=utf8")
//	이런식으로 produces 설정 필요
//  아작스 방식 떔시
	
	@Inject
	ImemberService service;
	@Inject
	IChatService chatService;
	/**
	 * 회원탈퇴 컨트롤러 소은수정 0719
	 */
	@RequestMapping(value = "/DeleteMember", method = RequestMethod.POST)
	@ResponseBody 
	public List<String> DeleteMember(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception {
		
		String mem_id = (String) session2.getAttribute("mem_id");		
		List<String> outRoomIdList = null;
		try{
			outRoomIdList = service.memberDelete(mem_id);
			session2.removeAttribute("member");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return outRoomIdList;
	}
}
