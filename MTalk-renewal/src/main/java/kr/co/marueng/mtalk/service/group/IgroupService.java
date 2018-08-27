package kr.co.marueng.mtalk.service.group;

import java.util.Map;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.vo.FriendGroupVO;

public interface IgroupService {
	/**
	 * 그룹추가
	 * @param group
	 * @return
	 */
	public ServiceResult createGroup(FriendGroupVO group);

	/**
	 * 그룹 이름 수정
	 * @param group
	 * @return
	 */
	public ServiceResult editName(FriendGroupVO group);

	/**
	 * 그룹 삭제
	 * @param group_id
	 * @return
	 */
	public ServiceResult deleteGroup(FriendGroupVO group);

	/**
	 * 그룹 중복 검사
	 * @param group
	 * @return
	 */
	public Integer checkGroupDuplication(FriendGroupVO group);

	/**
	 * 그룹 목록 조회
	 * @param mem_id
	 * @return
	 */
	public Map<String, String> retrievegroupList(String mem_id);
}
