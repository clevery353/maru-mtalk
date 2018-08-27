package kr.co.marueng.mtalk.controller.group;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.group.IgroupService;
import kr.co.marueng.mtalk.vo.FriendGroupVO;

@Controller
@RequestMapping("/group")
public class GroupDeleteController {
	@Inject
	private IgroupService service;
	
	public void setService(IgroupService service){
		this.service = service;
	}
	
	@RequestMapping(value="deleteGroup", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult deleteGroup(String group_id, String mem_id){
		FriendGroupVO group = new FriendGroupVO();
		group.setFriendGroup_member(mem_id);
		group.setFriendGroup_id(group_id);
		return service.deleteGroup(group);
	}
	
}
