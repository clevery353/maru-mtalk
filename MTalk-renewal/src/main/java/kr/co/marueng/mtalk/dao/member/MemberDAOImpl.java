package kr.co.marueng.mtalk.dao.member;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;

@Repository("memberDAO")	
public class MemberDAOImpl implements ImemberDAO{
	@Inject
	SqlSessionTemplate sqlSession;

	@Override
	public String selectMemberName(String mem_id) {
		return sqlSession.selectOne("member.selectMemberName", mem_id);
	}

	@Override
	public MemberVO selectRelationsMember(String mem_id) {
		return sqlSession.selectOne("member.selectRelationsMember", mem_id);
	}
	
	@Override
	public void memberEnroll(MemberVO vo) {
		// TODO Auto-generated method stub
		System.out.println("memberEnrollDAO");
		System.out.println("member_Id : "+vo.getMem_id());
		sqlSession.insert("member.memberEnroll",vo);
		sqlSession.insert("member.memberEnroll_friendgroup",vo.getMem_id());
	}

	@Override
	public MemberVO memberLogin(String id) {
		// TODO Auto-generated method stub
		System.out.println("memberLoginlDAO");
		System.out.println("member_Id : "+id);
		return sqlSession.selectOne("member.memberLogin",id);
	}

	@Override
	public void memberInfoUpdate(HashMap<String, Object>map) {
		// TODO Auto-generated method stub
		System.out.println("memberInfoUpdate");
		System.out.println("member_Id : "+map.get("mem_id"));
		sqlSession.update("member.memberInfoUpdate",map);	
	}

	@Override
	public MemberVO memberIdSearch(HashMap<String, Object>map) { // 2018-07-21 문경 수정
		// TODO Auto-generated method stub
		System.out.println("memberIdSearch");
		//System.out.println("member_mail : "+mail);
		return sqlSession.selectOne("member.memberIdSearch",map);
	}

	@Override
	public MemberVO memberPwSearch(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		
		System.out.println("memberPwSearch");
		System.out.println("member_id : "+map.get("mem_id"));
		return sqlSession.selectOne("member.memberPwSearch",map);
	}

	@Override
	public void memberImageUpload(Map<String, Object> map)throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("memberImageUpload-DAO");
		System.out.println("member_id : "+map.get("mem_id"));
		sqlSession.update("member.imageUpload",map);
	}

	@Override
	public void memberUpdatePw(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		System.out.println("memberUpdatePw-DAO");
		System.out.println("member_id : "+map.get("mem_id"));
		sqlSession.update("member.UpdatePw",map);
	}

	@Override
	public int memberIdCheck(String mem_id) {
		// TODO Auto-generated method stub
		System.out.println("memberIdCheck-DAO");
		System.out.println("mem_id : "+mem_id);
		return sqlSession.selectOne("member.memberIdCheck", mem_id);
	}

	@Override
	public int memberDelete(String mem_id) {
		// TODO Auto-generated method stub
		System.out.println("memberDelete-DAO");
		System.out.println("mem_id : "+mem_id);
		return sqlSession.update("member.deleteMember",mem_id);
	}
	
	// 0709 소은추가 : 회원아이디리스트로 이름 조회
	@Override
	public Map<String, String> memberInfoName(List<String> chatMemberList) {
		Map<String, String> chatMemberMap = new TreeMap<>();
		for(String mem_id : chatMemberList){
			String mem_name = sqlSession.selectOne("member.memberInfoName", mem_id);
			chatMemberMap.put(mem_id, mem_name);
		}
		return chatMemberMap;
	}
	
	// 0718 소은수정 : 말풍선리스트에 회원 이름과 프로필사진 넣어 반환
	@Override
	public List<ChatBubbleVO> bubbleMemberName(List<ChatBubbleVO> chatBubbleList) {
		for(ChatBubbleVO bub : chatBubbleList){
			MemberVO mem = sqlSession.selectOne("member.selectRelationsMember", bub.getMem_id());
			if(mem!=null){ 
				bub.setMem_name(mem.getMem_name());
				if(mem.getMem_propic()!=null){
					bub.setBubPropic(mem.getMem_propic());				
				}else{
					bub.setBubPropic("chat-userpic.png");
				}
			}else{ // 탈퇴한 회원의 경우 null
				bub.setMem_name("(알수없음)");
				bub.setBubPropic("chat-userpic.png");
			}
		}
		return chatBubbleList;
	}
	
	@Override
	public String memberMyInfoPwUp(String mem_pw) {
		System.out.println("memberMyInfoPwUp-DAO");
		return sqlSession.selectOne("member.MyInfoPwUp",mem_pw);
	}

	@Override
	public List<FriendVO> alertLogin(String mem_id) {
		System.out.println("alertLogin-DAO");
		return sqlSession.selectList("member.alertLogin",mem_id);
	}

	//	2018-07-23 현호 추가
	@Override
	public MemberVO checkOutUser(String findId) {
		return sqlSession.selectOne("member.selectRelationsMember", findId);
	}
	
	// 문경 추가 - 2017.07.24
	@Override
	public int mailupdate(MemberVO member) {  
		// TODO Auto-generated method stub
		return sqlSession.update("member.emailupdate",member);
	}
}
