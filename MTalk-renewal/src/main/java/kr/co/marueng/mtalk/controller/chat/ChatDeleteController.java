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
public class ChatDeleteController {
	
	@Inject
	IChatService chatService;
	/**
	 * 채팅방나가기
	 * @param outRoomId 나갈 현재채팅방의 아이디
	 * @return 성공여부 반환
	 */
	@RequestMapping("out")
	@ResponseBody
	public List<String> chatApply(String outRoomId, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		System.out.println(mem_id);
		System.out.println(outRoomId);
		String state = chatService.chatroomOut(outRoomId, mem_id);
		List<String> result = new ArrayList<>();
		result.add(state);
		return result;
	}
	
}
