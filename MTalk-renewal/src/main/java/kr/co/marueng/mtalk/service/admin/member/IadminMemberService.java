package kr.co.marueng.mtalk.service.admin.member;

import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 곽현호
 * @DATE 2018. 7. 19.
 */
public interface IadminMemberService {

	/**
	 * 가입신청한 멤버 리스트
	 * @return
	 */
	public List<MemberVO> retrieveJoinMember();

	/**
	 * 가입한 멤버 리스트
	 * @return
	 */
	public List<MemberVO> retrieveMember();

	/**
	 * 회원가입 승인
	 * @param mem_id
	 * @return
	 */
	public Map<String, Object> approveMember(String mem_id);

	/**
	 * 회원가입 거절
	 * @param mem_id
	 * @return
	 */
	public Map<String, Object> rejectMember(String mem_id);

	/**
	 * 회원 강퇴
	 * @param mem_id
	 * @return
	 */
	public Map<String, Object> banMember(String mem_id);

}
