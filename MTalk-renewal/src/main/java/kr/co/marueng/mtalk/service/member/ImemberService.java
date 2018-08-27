package kr.co.marueng.mtalk.service.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 조문경
 * @DATE 2018. 7. 11.
 */
public interface ImemberService {
	public void memberEnroll(MemberVO vo);
	public MemberVO memberLogin(String id);
	public void memberInfoUpload(HashMap<String, Object>map);
	public MemberVO memberIdSearch(HashMap<String,Object>map); // 2018-07-21 문경 수정
	public MemberVO memberPwSearch(HashMap<String, Object>map); // 2018-07-21 문경 수정
	public void memberImageUpload(Map<String, Object>map);
	public void memberUpdatePw(HashMap<String,Object>map);
	public int memberIdCheck(String mem_id);
	/**
	 * 0719 소은수정
	 * @param mem_id
	 * @return 회원탈퇴하며 소속된 방을 모두 나가며, 그 방들의 아이디를 리스트로 반환한다.
	 */
	public List<String> memberDelete(String mem_id);

	/**
	 * 0715 소은작성
	 * 회원의 아이디 리스트를 받아, 이름을 담은 맵으로 반환한다.
	 * @param mem_id
	 * @return 
	 */
	public Map<String, String> memberInfoName(List<String> chatMemberList);
	
	public String memberMyInfoPwUp(String mem_pw);
	public List<FriendVO> alertLogin(String mem_id);
	/**
	 * 탈퇴한 회원인지 조회 위해 사용 2018-07-23 현호 추가
	 * @param findId
	 * @return
	 */
	public MemberVO checkOutUser(String findId);
	/**
	 * 내정보수정 이메일 2018-07-24  문경 추가
	 * @param member
	 * @return
	 */
	public int mailupdate(MemberVO member);
}
