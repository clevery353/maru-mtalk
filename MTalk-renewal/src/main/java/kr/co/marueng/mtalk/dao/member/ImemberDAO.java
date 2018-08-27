package kr.co.marueng.mtalk.dao.member;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;

public interface ImemberDAO {
	/**
	 * 친구 목록 조회시 친구 아이디로
	 * 친구 이름 조회
	 * @param mem_id
	 * @return
	 */
	public String selectMemberName(String mem_id);
	
	/**
	 * 받은 친구신청 목록 시 필요한 회원정보
	 * @param mem_id
	 * @return
	 */
	public MemberVO selectRelationsMember(String mem_id);
	
	/*조문경씨*/
	public void memberEnroll(MemberVO vo);
	public MemberVO memberLogin(String id);
	public void memberInfoUpdate(HashMap<String, Object>map);
	public MemberVO memberIdSearch(HashMap<String, Object>map); // 2018-07-21 문경 수정
	/**
	/**
	 * 문경 작성
	 * 멤버 비밀번호 변경전 신원 확인
	 * @param mem_id, mem_mail
	 * @return memberVO
	 */
	public MemberVO memberPwSearch(HashMap<String, Object>map);
	public void memberImageUpload(Map<String, Object>map)throws SQLException;
	public void memberUpdatePw(HashMap<String, Object>map);
	public int memberIdCheck(String mem_id);
	public int memberDelete(String mem_id);
	/**
	 * 0709 소은작성
	 * 회원의 아이디 리스트를 받아, 이름을 담은 맵으로 반환한다.
	 * @param mem_id
	 * @return 
	 */
	public Map<String, String> memberInfoName(List<String> chatMemberList);
	/**
	 * 0709 소은작성
	 * 말풍선vo 리스트를 받아, 회원 이름을 넣어 반환한다.
	 * @param 
	 * @return 
	 */
	public List<ChatBubbleVO> bubbleMemberName(List<ChatBubbleVO> chatBubbleList);
	/**
	 * 문경 작성
	 * 멤버 비밀번호 업데이트
	 * @param mem_pw
	 * @return mem_id
	 */
	public String memberMyInfoPwUp(String mem_pw);
	
	public List<FriendVO> alertLogin(String mem_id);

//	2018-07-23 현호 추가
	/**
	 * 아이디 중복 체크용
	 * @param findId
	 * @return
	 */
	public MemberVO checkOutUser(String findId);
	/**
	 * 회원정보수정 이메일 0724 문경추가
	 * @param member
	 * @return
	 */
	public int mailupdate(MemberVO member); 
}
