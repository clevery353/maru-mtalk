package kr.co.marueng.mtalk.controller.relations;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.service.friend.IfriendService;
import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.service.relations.IrelationsService;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

@Controller
@RequestMapping("/relations")
public class RelationsInsertController {
	@Inject
	private IrelationsService service;
	@Inject
	private IfriendService friendService;
	@Inject
	private ImemberService memberService; //2018-07-23 현호 추가
	
	public void setService(IrelationsService service){
		this.service = service;
	}
	
	@RequestMapping(value="addFriend", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ServiceResult rejectFriend(
			String findId,
			String mem_id
			){
		ServiceResult result = null;
		
		RelationsVO relations = new RelationsVO();
		relations.setRel_firstMem(mem_id);
		relations.setRel_secondMem(findId);
		int checkFriend = friendService.checkAlreadyFriend(relations);
		int checkRelationSend = service.checkAlreadySendRelations(relations);
		int checkRelationReceive = service.checkAlreadyReceiveRelations(relations);
		//2018-07-23 현호 추가
		MemberVO checkOutMember = memberService.checkOutUser(findId);
		if(checkOutMember==null){
			result = ServiceResult.OUTMEMBER;
		}else if(checkFriend>0){
			result = ServiceResult.ALREADYFRIEND;
		}else if(checkRelationSend>0){
			result = ServiceResult.ALREADYSEND;
		}else if(checkRelationReceive>0){
			result = ServiceResult.ALREADYRECEIVE;
		}else if(StringUtils.equals(findId, mem_id)){ //현호 수정 2018-07-23
			result = ServiceResult.MYSELF;
		}else{
			result = service.addFriend(relations);
		}
		
		return result;
	}
}
