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
public class GroupUpdateController {
	@Inject
	private IgroupService service;
	
	public void setService(IgroupService service){
		this.service = service;
	}
	
	@RequestMapping(value="editName", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult editName(String group_id, String group_name){
		FriendGroupVO group = new FriendGroupVO();
		group.setFriendGroup_id(group_id);
		group.setFriendGroup_name(group_name);
		return service.editName(group);
	}
}
