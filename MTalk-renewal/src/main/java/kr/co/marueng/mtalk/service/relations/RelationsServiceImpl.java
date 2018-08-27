package kr.co.marueng.mtalk.service.relations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.dao.member.ImemberDAO;
import kr.co.marueng.mtalk.dao.relations.IrelationsDAO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

/**
 * @author 곽현호
 * @DATE 2018. 7. 9.
 */
@Service
public class RelationsServiceImpl implements IrelationsService {
	@Inject
	IrelationsDAO relationsDAO;
	
	@Inject
	ImemberDAO memberDAO;
	
	// 친구신청 목록 조회시, 0718 소은수정
	@Override
	public List<MemberVO> retrieveRelations(String mem_id) {
		List<RelationsVO> relationsList = new ArrayList<>();
		List<MemberVO> memberList = new ArrayList<>();
		
		relationsList = relationsDAO.selectRelations(mem_id);
		for(int i = 0; i< relationsList.size(); i++){
			MemberVO vo = memberDAO.selectRelationsMember(relationsList.get(i).getRel_firstMem());
			if(vo!=null){
				memberList.add(vo);				
			}
			
			/* 내가 보낸 친구신청 목록 보여줄때, 'D' 상태인 관계는 member조회를 해도 나오지 않는다.
			 * 이때 아래처럼 탈퇴한 회원으로 보여줄수있도록 한다. 
			 * if(vo==null){
				vo = new MemberVO();
				vo.setMem_id(relationsList.get(i).getRel_firstMem());
				vo.setMem_name("탈퇴한 회원");
				vo.setMem_propic("outuser.png");
				memberList.add(vo);
			}*/
		}
		
		return memberList;
	}

	@Override
	public ServiceResult rejectFriend(RelationsVO relations) {
		ServiceResult result = null;
		int rowCnt = relationsDAO.rejectFriend(relations);
		if(rowCnt<0){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public ServiceResult addFriend(RelationsVO relations) {
		ServiceResult result = null;
		int rowCnt = relationsDAO.addFriend(relations);
		if(rowCnt<0){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public Integer checkAlreadySendRelations(RelationsVO relations) {
		return relationsDAO.checkAlreadySendRelations(relations);
	}

	@Override
	public Integer checkAlreadyReceiveRelations(RelationsVO relations) {
		return relationsDAO.checkAlreadyReceiveRelations(relations);
	}

}
