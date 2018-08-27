package kr.co.marueng.mtalk.controller.notice;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.notice.InoticeService;

@Controller
@RequestMapping("/notice")
public class NoticeDeleteController {
	@Inject
	InoticeService service;
	
	public void setService(InoticeService service){
		this.service = service;
	}
	
	@RequestMapping(value="deleteNotice", produces = "application/json; charset=UTF-8")
	public String deleteNotice(
			@RequestParam(name="what") String noti_id,
			RedirectAttributes redirectattributes
			)throws Exception{
		ServiceResult result = service.deleteNotice(noti_id);
		String goPage = null;
		String message = null;
		switch(result){
		case FAILED:
			message = "서버오류";
			goPage = "redirect:/notice/noticeList";
			break;
		default:
			message = "삭제 성공";
			goPage = "redirect:/notice/noticeList";
	}
	redirectattributes.addFlashAttribute("message", message);
	return goPage;
	} 
}
