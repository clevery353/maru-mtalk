package kr.co.marueng.mtalk.dao.group;

import java.util.List;

import kr.co.marueng.mtalk.vo.FriendGroupVO;

public interface IgroupDAO {

	/**
	 * 해당 회원의 그룹 목록 조회
	 * @param mem_id
	 * @return
	 */
	public List<FriendGroupVO> selectGroupList(String mem_id);
	
	/**
	 * 해당 회원의 그룹 생성
	 * @param group
	 * @return
	 */
	public Integer insertGroup(FriendGroupVO group);

	/**
	 * 그룹이름 변경
	 * @param group
	 * @return
	 */
	public Integer editName(FriendGroupVO group);

	/**
	 * 그룹삭제
	 * @param group
	 * @return
	 */
	public Integer deleteGroup(FriendGroupVO group);

	/**
	 * 그룹 중복 확인
	 * @param group
	 * @return
	 */
	public Integer checkGroupDuplication(FriendGroupVO group);
}
