package kr.co.marueng.mtalk.controller.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.mtalk.service.chat.IChatService;

/**
 * @author 방소은
 * 작성일 : 2018.07.15
 * 채팅수정 관련 컨트롤러
 */
@Controller
@RequestMapping("/chat")
public class ChatUpdateController {

	@Inject
	IChatService chatService;
	/**
	 * 그룹채팅방 만들기, 참여자상대 초대
	 * @param memIdList
	 * @return 채팅방 아이디 반환
	 */
	@RequestMapping("invite")
	@ResponseBody
	public Map<String, String> chatInvite(String memIdList, String chatroom_id, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		
		List<String> chatMemberList = new ArrayList<>();
		String[] chatMem = memIdList.split("&");
		for(String cm : chatMem){
			chatMemberList.add(cm);
		}
		String result_id = chatService.chatInvite(mem_id, chatroom_id, chatMemberList);
		Map<String, String> result = new HashMap<String, String>();
		result.put("result", result_id);
		return result;
	}
	
	/**
	 * 채팅방이름 개인별 변경
	 * @param chatroom_name
	 * @param chatroom_id
	 * @param session
	 * @return
	 */
	@RequestMapping("roomname")
	@ResponseBody
	public List<String> chatEditName(String chatroom_name, String chatroom_id, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		int res = chatService.chatEditName(mem_id, chatroom_id, chatroom_name);
		List<String> list = new ArrayList<>();
		if(res > 0){
			list.add("ok");
		}else{
			list.add("fail");
		}
		return list;
	}
	
	/**
	 * 해당 채팅방 대화내용을 삭제한다
	 * (실제 작업은 chatMember의 최초참여시간을 변경하여, 이전 대화가 보이지 않도록 하였다)
	 * @param chatroom_name
	 * @param chatroom_id
	 * @param session
	 * @return
	 */
	@RequestMapping("chatdel")
	@ResponseBody
	public Map<String, String> myChatDel(String chatroom_name, String chatroom_id, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		int res = chatService.myChatDel(mem_id, chatroom_id);
		Map<String, String> result = new HashMap<>();
		if(res > 0){
			result.put("result", "ok");
		}else{
			result.put("result", "fail");
		}
		return result;
	}
	
	
	
}
