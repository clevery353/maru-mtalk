package kr.co.marueng.mtalk.controller.chat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.mtalk.service.chat.IChatService;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 방소은
 * 작성일 : 2018.07.09
 * 채팅신청 관련 컨트롤러
 */
@Controller
@RequestMapping("/chat")
public class ChatInsertController {
	
	@Inject
	IChatService chatService;
	/**
	 * 1:1 친구신청 (친구아이디 더블클릭시 작업)
	 * @param fri_memid 신청하는 친구의 mem_id아이디
	 * @return 생성된 채팅방ID 반환
	 */
	@RequestMapping("apply")
	@ResponseBody
	public String chatApply(String fri_memid, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		
		List<String> chatMemberList = new ArrayList<>();
		chatMemberList.add(mem_id);
		chatMemberList.add(fri_memid);
		
		String result_id = chatService.chatInsert(mem_id, chatMemberList);
		
		return result_id;
	}
	/**
	 * 그룹채팅방 만들기
	 * @param memIdList
	 * @return 채팅방 아이디 반환
	 */
	@RequestMapping("make")
	@ResponseBody
	public String chatMake(String memIdList, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		
		List<String> chatMemberList = new ArrayList<>();
		chatMemberList.add(mem_id);
		String[] chatMem = memIdList.split("&");
		for(String cm : chatMem){
			chatMemberList.add(cm);			
		}
		String result_id = chatService.chatInsert(mem_id, chatMemberList);
		return result_id;
	}
}
