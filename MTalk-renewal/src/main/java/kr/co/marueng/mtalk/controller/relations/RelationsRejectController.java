package kr.co.marueng.mtalk.controller.relations;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.relations.IrelationsService;
import kr.co.marueng.mtalk.vo.RelationsVO;

@Controller
@RequestMapping("/relations")
public class RelationsRejectController {
	@Inject
	private IrelationsService service;
	
	public void setService(IrelationsService service){
		this.service = service;
	}
	
	@RequestMapping(value="reject", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult rejectFriend(
			String mem_id, String friend_id
			){
		RelationsVO relations = new RelationsVO();
		relations.setRel_firstMem(friend_id);
		relations.setRel_secondMem(mem_id);
		ServiceResult result = service.rejectFriend(relations);
		return result;
	}
}
