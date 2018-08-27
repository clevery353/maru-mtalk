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
public class GroupInsertController {
	@Inject
	private IgroupService service;
	
	public void setService(IgroupService service){
		this.service = service;
	}
	
	
	@RequestMapping(value="create", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult createGroup(String mem_id, String group_name){
		ServiceResult result = null;
		
		FriendGroupVO group = new FriendGroupVO();
		group.setFriendGroup_member(mem_id);
		group.setFriendGroup_name(group_name);
		
		
		int rowCnt = service.checkGroupDuplication(group);
		if(rowCnt>0){
			result = ServiceResult.DUPLICATED;
		}else{
			service.createGroup(group);
			result = ServiceResult.OK;
		}
		
		return result;
	}
}
