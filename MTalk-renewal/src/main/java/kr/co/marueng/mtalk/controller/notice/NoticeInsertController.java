package kr.co.marueng.mtalk.controller.notice;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.notice.InoticeService;
import kr.co.marueng.mtalk.vo.NoticeVO;

@Controller
@RequestMapping("/notice/insert")
public class NoticeInsertController {
	@Inject
	InoticeService service;
	
	public void setService(InoticeService service){
		this.service = service;
	}
//	2018-07-25 현호 수정
	@RequestMapping(method = RequestMethod.GET)
	public String form(
			Model model,
			HttpSession session
			){
		String goPage = "";
		String userType = (String)session.getAttribute("mem_type");
		if(userType!=null){
			if(userType!="manager"){
				goPage = "errors/error403";
			}else{
				goPage="notice/noticeForm";
			}
		}else{
			goPage="errors/error403";
		}
		if(model.containsAttribute("notice")){
			NoticeVO notice = (NoticeVO) model.asMap().get("notice");
			if(notice.getNoti_id()!=null){
				model.addAttribute("notice", new NoticeVO());
			}
		}else{
			model.addAttribute("notice", new NoticeVO());
		}
		String member_id = (String) session.getAttribute("mem_id"); //세션에서 받아오기
		model.addAttribute("member_id",member_id);
		return goPage;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String process(
			@ModelAttribute("notice") NoticeVO notice,
			Errors errors,
			Model model,
			SessionStatus status,
			RedirectAttributes redirectAttributes
			){
		String goPage = null;
		String message = null;
		String member_id = "admin"; //세션에서 받아오기
		if(!errors.hasErrors()){
			notice.setNoti_writer(member_id);
			ServiceResult result = service.createNotice(notice);
			if(ServiceResult.OK.equals(result)){
				message = "등록 성공";
				goPage = "redirect:noticeList";
				status.setComplete();
			}else{
				message = "서버오류";
				goPage = "redirect:noticeForm";
			}
		}else{
			goPage = "redirect:noticeForm";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return goPage;
	}
}
