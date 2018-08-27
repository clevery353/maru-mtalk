package kr.co.marueng.mtalk.controller.friend;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.friend.IfriendService;
import kr.co.marueng.mtalk.vo.FriendVO;

@Controller
@RequestMapping("/friend")
public class FriendDeleteController {
	@Inject
	private IfriendService service;
	
	public void setService(IfriendService service){
		this.service = service;
	}
	
	@RequestMapping(value="deleteFriend", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult friendList(
			String friend_id,
			String mem_id
			){
		FriendVO friend = new FriendVO();
		friend.setFri_firstmem(mem_id);
		friend.setFri_friendmem(friend_id);
		return service.deleteFriend(friend);
	}
}
