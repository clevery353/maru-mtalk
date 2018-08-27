package kr.co.marueng.mtalk.service.relations;

import java.util.List;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

public interface IrelationsService {
	public List<MemberVO> retrieveRelations(String mem_id);

	public ServiceResult rejectFriend(RelationsVO relations);

	public ServiceResult addFriend(RelationsVO relations);

	public Integer checkAlreadySendRelations(RelationsVO relations);

	public Integer checkAlreadyReceiveRelations(RelationsVO relations);
}
