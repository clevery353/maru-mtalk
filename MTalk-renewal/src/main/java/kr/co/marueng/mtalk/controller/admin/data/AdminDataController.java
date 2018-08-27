package kr.co.marueng.mtalk.controller.admin.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.marueng.mtalk.service.admin.chat.IadminChatService;
import kr.co.marueng.mtalk.service.admin.hit.IhitService;
import kr.co.marueng.mtalk.service.admin.member.IadminMemberService;
import kr.co.marueng.mtalk.service.admin.useData.IuseDataService;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.DataPageInitVO;
import kr.co.marueng.mtalk.vo.HitVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.UseDataVO;

@Controller
@RequestMapping("/admin/data")
public class AdminDataController {
	@Inject
	IhitService hitService;
	@Inject
	IuseDataService useDataService;
	@Inject
	IadminChatService adminChatService;
	@Inject
	IadminMemberService adminMemberService;
	
	public void setHitService(IhitService service){
		this.hitService = service;
	}
	
//	2018-07-25 현호 수정
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
				goPage="admin/data";
			}
		}else{
			goPage="errors/error403";
		}
		DataPageInitVO dataPageInitVO = new DataPageInitVO();
		
//		전체 접속자수 조회
		List<HitVO> hitList = new ArrayList<>();
		hitList = hitService.retrieveAllHit();
//		전체 회원의 총 데이터 사용량 조회
		List<UseDataVO> useDataList = new ArrayList<>();
		useDataList = useDataService.retrieveAllData();
//		전체 채팅방 리스트 조회
		List<ChatRoomVO> chatRoomList = new ArrayList<>();
		chatRoomList = adminChatService.retrieveAllChatRoom();
//		전체 회원 조회
		List<MemberVO> memberList = new ArrayList<>();
		memberList = adminMemberService.retrieveMember();
		
		
		dataPageInitVO.setHitList(hitList);
		dataPageInitVO.setUseDataList(useDataList);
		dataPageInitVO.setChatRoomList(chatRoomList);
		dataPageInitVO.setMemberList(memberList);
		
		model.addAttribute("dataPageInitVO", dataPageInitVO);
		return goPage;
	};
	
	@RequestMapping("hitList")
	@ResponseBody
	public List<HitVO> retrieveHit(
			String hitYear,
			String hitMonth
			){
		Map<String, String> condition = new HashMap<>();
		condition.put("hitYear", hitYear);
		condition.put("hitMonth", hitMonth);
		return hitService.retrieveHit(condition);
	}
	
	@RequestMapping("dataList")
	@ResponseBody
	public List<MemberVO> retrieveMemberData(
			String dataMember,
			String dataYear,
			String dataMonth
			){
		System.err.println("AdminDataController 메서드 들어");
		List<MemberVO> result = new ArrayList<>();
		if(StringUtils.equals(dataMember, "")
				&&StringUtils.equals(dataYear, "allYear")
				&&StringUtils.equals(dataMonth, "allMonth")
				){
			System.err.println("AdminDataController:데이터멤버 널 조건 들어옴");
			result = adminMemberService.retrieveMember();
		}else{
			Map<String, String> condition = new HashMap<>();
			condition.put("dataMember", dataMember);
			condition.put("dataYear", dataYear);
			condition.put("dataMonth", dataMonth);
			result = useDataService.retriveData(condition);
		}
		
		return result;
	}
	
	
}
