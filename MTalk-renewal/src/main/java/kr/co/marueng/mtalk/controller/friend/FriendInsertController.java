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
public class FriendInsertController {
	@Inject
	private IfriendService service;
	
	public void setService(IfriendService service){
		this.service = service;
	}
	
	/**
	 * 친구 수락
	 * @param mem_id
	 * @return
	 */
	@RequestMapping(value="friendCreate", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult friendCreate(
			String friend_id,
			String mem_id
			){
		FriendVO friend = new FriendVO();
		friend.setFri_firstmem(friend_id);
		friend.setFri_friendmem(mem_id);
		friend.setFri_friendGroupId("기본");
		friend.setFri_bookMark("N");
		ServiceResult result = service.createFriend(friend);
		return result;
	}
}
