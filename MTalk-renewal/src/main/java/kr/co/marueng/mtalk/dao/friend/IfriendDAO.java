package kr.co.marueng.mtalk.dao.friend;

import java.util.Map;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.vo.FriendGroupVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

public interface IfriendDAO {
	
	/**
	 * 해당 회원의 친구 목록, 즐겨찾기 되어있는 친구 목록 조회
	 * @param mem_id
	 * @return
	 */
	public Map<String, Object> selectFriendList(String mem_id);
	
	/**
	 * 친구 목록 생성
	 * @param friend
	 * @return
	 */
	public ServiceResult createFriend(FriendVO friend);

	/**
	 * 친구 그룹에서 제외(기본 그룹으로 이동)
	 * @param friend
	 * @return
	 */
	public Integer excludeFriend(FriendVO friend);

	/**
	 * 친구 삭제
	 * @param friend
	 * @return
	 */
	public Integer deleteFriend(FriendVO friend);

	/**
	 * 즐겨찾기 추가
	 * @param friend
	 * @return
	 */
	public Integer addBookmark(FriendVO friend);

	/**
	 * 즐겨찾기 제외
	 * @param friend
	 * @return
	 */
	public Integer excludeBookmark(FriendVO friend);

	/**
	 * 그룹 삭제시 회원을 기본 그룹으로 이동
	 * @param group_id
	 * @return
	 */
	public Integer excludeGroup(FriendGroupVO group);

	/**
	 * 친구의 그룹 이동
	 * @param friend
	 * @return
	 */
	public Integer moveGroup(FriendVO friend);

	/**
	 * 친구 신청 전 이미 친구 관계인지 확인
	 * @param relations
	 * @return
	 */
	public Integer checkAlreadyFriend(RelationsVO relations);
	/** 0717 소은추가
	 * 서로 친구인지 확인한다. 아이디 2개모두 해당되는 데이터가 있으면 친구관계로 보고, friendVO 반환
	 * @param friendVO(fri_firstmem과 fri_friendmem)  
	 * @return 성공시 데이터1건, 실패시 null 반환
	 */
	public FriendVO isFriend(FriendVO friend);
}

