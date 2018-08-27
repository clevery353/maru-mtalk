package kr.co.marueng.mtalk.controller.notice;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.notice.InoticeService;
import kr.co.marueng.mtalk.vo.NoticeVO;

@Controller
@RequestMapping("/notice")
public class NoticeUpdateController {
	@Inject
	InoticeService service;
	
	public void setService(InoticeService service){
		this.service = service;
	}
//	2018-07-25 현호 수정
	@RequestMapping(value="updateNotice", method=RequestMethod.GET)
	public String updateOutputGuideForm(
			@RequestParam(name="what") String noti_id,
			Model model,
			HttpSession session
			)throws Exception{
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
		NoticeVO notice = service.retrieveNotice(noti_id);
		model.addAttribute("notice",notice);
		
		return goPage;
	}
	
	@RequestMapping(value="updateNotice", method=RequestMethod.POST)
	public String updateOutputGuide(
			NoticeVO notice,
			RedirectAttributes redirectattributes,
			Model model
			)throws Exception{
		ServiceResult result = service.modifyNotice(notice);
		
		String goPage = null;
		String message = null;
		
		switch(result){
		case FAILED:
			message = "수정오류";
			goPage = "redirect:/notice/noticeList";
			break;
		default:
			message = "수정성공";
			goPage = "redirect:/notice/noticeList";
			break;
		}
		redirectattributes.addFlashAttribute("message",message);
		return goPage;
	}
}
