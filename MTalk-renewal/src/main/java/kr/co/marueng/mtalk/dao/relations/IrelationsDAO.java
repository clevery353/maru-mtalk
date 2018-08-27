package kr.co.marueng.mtalk.dao.relations;

import java.util.List;

import kr.co.marueng.mtalk.vo.RelationsVO;

public interface IrelationsDAO {
	/**
	 * 받은 친구신청 조회
	 * @param mem_id
	 * @return
	 */
	public List<RelationsVO> selectRelations(String mem_id);
	
	/**
	 * 관계상태 업데이트
	 * @param relations
	 * @return
	 */
	public Integer updateRelations(RelationsVO relations);

	/**
	 * 친구거절 or 친구삭제 시 관계테이블 데이터 삭제
	 * @param relations
	 * @return
	 */
	public Integer rejectFriend(RelationsVO relations);

	/**
	 * 친구신청
	 * @param relations
	 * @return
	 */
	public Integer addFriend(RelationsVO relations);

	/**
	 * 친구신청 전 이미 친구신청을 보낸 회원인지 조회
	 * @param relations
	 * @return
	 */
	public Integer checkAlreadySendRelations(RelationsVO relations);

	/**
	 * 친구신청 전 이미 친구신청을 받은 회원인지 조회
	 * @param relations
	 * @return
	 */
	public Integer checkAlreadyReceiveRelations(RelationsVO relations);
	/**
	 * 해당 회원의 모든 초대관련 목록 조회 0718 소은추가
	 * @param relations
	 * @return
	 */
	public List<RelationsVO> allMemRelations(RelationsVO relations);
	/**
	 * 회원탈퇴시 해당 관계에 상태를 D로 바꾸어 탈퇴한 회원임을 표시한다.
	 * @param relations
	 * @return 
	 */
	public int outMemRelations(RelationsVO relations);
	
}
