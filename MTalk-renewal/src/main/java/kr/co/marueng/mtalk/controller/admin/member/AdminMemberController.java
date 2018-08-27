package kr.co.marueng.mtalk.controller.admin.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.marueng.mtalk.service.admin.member.IadminMemberService;
import kr.co.marueng.mtalk.vo.AdminMemberVO;
import kr.co.marueng.mtalk.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class AdminMemberController {
	@Inject
	private IadminMemberService service;
	
	public void setService(IadminMemberService service){
		this.service = service;
	}
	
	@RequestMapping("")
	public String adminPageAccess(
			Model model,
			RedirectAttributes redirectAttributes,
			HttpSession session
			){
		String goPage = "";
		String userType = (String)session.getAttribute("mem_type");
		System.err.println(userType);
		System.err.println((String)session.getAttribute("mem_id"));
		System.err.println((String)session.getAttribute("mem_name"));
		if(userType!=null){
			if(userType!="manager"){
				goPage = "errors/error403";
			}else{
				goPage="admin/main";
			}
		}else{
			goPage="errors/error403";
		}
		AdminMemberVO<MemberVO> adminMemberVO = new AdminMemberVO<MemberVO>();
		List<MemberVO> memberList = new ArrayList<>();
		memberList = service.retrieveMember();
		List<MemberVO> joinMemberList = new ArrayList<>();
		joinMemberList = service.retrieveJoinMember();
		adminMemberVO.setMemberList(memberList);
		adminMemberVO.setJoinList(joinMemberList);
		
		model.addAttribute("adminMemberVO",adminMemberVO);
		return goPage;
	};
	
	@RequestMapping("approveMember")
	@ResponseBody
	public Map<String, Object> approveMember(
		String mem_id	
			){
		return service.approveMember(mem_id);
	}
	
	@RequestMapping("rejectMember")
	@ResponseBody
	public Map<String, Object> rejectMember(
		String mem_id	
			){
		return service.rejectMember(mem_id);
	}
	
	@RequestMapping("banMember")
	@ResponseBody
	public Map<String, Object> banMember(
			String mem_id
			){
		return service.banMember(mem_id);
	}
	
}
