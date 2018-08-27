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
public class FriendUpdateController {
	@Inject
	private IfriendService service;
	
	public void setService(IfriendService service){
		this.service = service;
	}
	
	
	/**
	 * 그룹에서 제외 -> 기본 그룹으로 이동
	 * @param friend_id
	 * @return
	 */
	@RequestMapping(value="excludeFriend", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult changeFriendGroup (
			String friend_id,
			String mem_id
			){
		FriendVO friend = new FriendVO();
		friend.setFri_firstmem(mem_id);
		friend.setFri_friendmem(friend_id);
		
		return service.excludeFriend(friend);
	}
	
	/**
	 * 즐겨찾기 추가
	 * @param friend_id
	 * @return
	 */
	@RequestMapping(value="addBookmark", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult addBookmark(
			String friend_id,
			String mem_id
			){
		FriendVO friend = new FriendVO();
		friend.setFri_firstmem(mem_id);
		friend.setFri_friendmem(friend_id);
		System.err.println("mem_id:"+mem_id);
		return service.addBookmark(friend);
	}
	
	/**
	 * 즐겨찾기 제외
	 * @param friend_id
	 * @return
	 */
	@RequestMapping(value="excludeBookmark", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult excludeBookmark(
			String friend_id,
			String mem_id
			){
		FriendVO friend = new FriendVO();
		friend.setFri_firstmem(mem_id);
		friend.setFri_friendmem(friend_id);
		
		return service.excludeBookmark(friend);
	}
	
	@RequestMapping(value="moveGroup", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult moveGroup(
			String friend_id,
			String group_id,
			String mem_id
			){
		
		FriendVO friend = new FriendVO();
		friend.setFri_firstmem(mem_id);
		friend.setFri_friendmem(friend_id);
		friend.setFri_friendGroupId(group_id);
		
		return service.moveGroup(friend);
	}
	
}
