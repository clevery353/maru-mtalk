package kr.co.marueng.mtalk.controller.notice;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.marueng.mtalk.service.notice.InoticeService;
import kr.co.marueng.mtalk.vo.NoticeVO;
import kr.co.marueng.mtalk.vo.PagingVO;

@Controller
@RequestMapping("/notice")
public class NoticeRetrieveController {
	@Inject
	InoticeService service;
	
	@RequestMapping("noticeList")
	public String outputGuideList(
			@RequestParam(name="page",required=false,defaultValue="1") int currentPage,
			@RequestParam(required=false) String searchType,
			@RequestParam(required=false) String searchWord,
			Model model,
			RedirectAttributes redirectAttributes
			){
		
		PagingVO<NoticeVO> pagingVO = new PagingVO<>();
		NoticeVO searchVO = new NoticeVO();
		
		pagingVO.setScreenSize(5);
		pagingVO.setSearchVO(searchVO);
		if(StringUtils.isNotBlank(searchWord)){
			switch(searchType){
				case "noti_title":
					searchVO.setNoti_title(searchWord); 
					break;
				case "noti_text":
					searchVO.setNoti_text(searchWord);
					break;
				default:
					searchVO.setNoti_title(searchWord); 
					searchVO.setNoti_text(searchWord);
					break;
			}
		}
		long totalRecord = service.retrieveNoticeCount(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
		pagingVO.setCurrentPage(currentPage);
		List<NoticeVO> outputGuideList = service.retrieveNoticeList(pagingVO);
		pagingVO.setDataList(outputGuideList);
		
		model.addAttribute("pagingVO", pagingVO);
		
		
		return "admin/notice/noticeList";
		
	}
	
	@RequestMapping("view/{num}")
	public ModelAndView process(
			@PathVariable(value="num") String noti_id,
			ModelAndView andView
			) throws Exception{
		NoticeVO notice = service.retrieveNotice(noti_id);
		
		andView.addObject("notice", notice);
		andView.setViewName("notice/noticeView");
		
		return andView;
	}
	
}
