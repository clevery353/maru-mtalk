package kr.co.marueng.mtalk.dao.admin.member;

import java.util.List;

import kr.co.marueng.mtalk.vo.MemberVO;

public interface IadminMemberDAO {

	/**
	 * 가입 대기 회원 조회
	 * @return
	 */
	public List<MemberVO> retrieveJoinMember();

	/**
	 * 회원조회
	 * @return
	 */
	public List<MemberVO> retrieveMember();

	/**
	 * 회원 가입 승인
	 * @param mem_id
	 */
	public Integer approveMember(String mem_id);

	/**
	 * 회원 가입 거절
	 * @param mem_id
	 * @return
	 */
	public int rejectMember(String mem_id);

	/**
	 * 회원 강퇴
	 * @param mem_id
	 * @return
	 */
	public int banMember(String mem_id);

}
