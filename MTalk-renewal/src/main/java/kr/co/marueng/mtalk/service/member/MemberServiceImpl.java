package kr.co.marueng.mtalk.service.member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.chat.IChatDAO;
import kr.co.marueng.mtalk.dao.member.ImemberDAO;
import kr.co.marueng.mtalk.dao.relations.IrelationsDAO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

@Service
public class MemberServiceImpl implements ImemberService{
	@Inject
	ImemberDAO memberDAO;
	@Inject
	IChatDAO chatDAO; // 소은추가 0718
	@Inject
	IrelationsDAO relationsDAO; // 소은추가 0718
	
	@Override
	public void memberEnroll(MemberVO vo) {
		// TODO Auto-generated method stub
		System.out.println("memberEnrollService");
		System.out.println("member_Id : "+vo.getMem_id());
		memberDAO.memberEnroll(vo);
	}

	@Override
	public MemberVO memberLogin(String id) {
		// TODO Auto-generated method stub
		
		System.out.println("memberLoginService");
		System.out.println("member_Id : "+id);
		return memberDAO.memberLogin(id);
	}

	@Override
	public void memberInfoUpload(HashMap<String, Object>map) {
		// TODO Auto-generated method stub
		System.out.println("memberInfoUpload");
		System.out.println("member_Id : "+map.get("mem_id"));
		memberDAO.memberInfoUpdate(map);
	}

	@Override
	public MemberVO memberIdSearch(HashMap<String, Object>map) { // 2018-07-21 문경 수정
		// TODO Auto-generated method stub
		System.out.println("memberIdSearch");
//		System.out.println("member_mail : "+mail);
		return memberDAO.memberIdSearch(map);
	}

	@Override
	public MemberVO memberPwSearch(HashMap<String, Object> map) { // 2018-07-21 문경 수정
		// TODO Auto-generated method stub
		System.out.println("memberPwSearch");
		System.out.println("member_id : "+map.get("mem_id"));
		return memberDAO.memberPwSearch(map);
	}

	@Override
	public void memberImageUpload(Map<String, Object> map) {
		// TODO Auto-generated method stub
		System.out.println("memberImageUpload-Service");
		//System.out.println("member_id : "+map.get("mem_id"));
		try {
			memberDAO.memberImageUpload(map);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void memberUpdatePw(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		System.out.println("memberUpdatePw-Service");
		System.out.println("member_id : "+map.get("mem_id"));
		memberDAO.memberUpdatePw(map);
	}

	@Override
	public int memberIdCheck(String mem_id) {
		// TODO Auto-generated method stub
		System.out.println("memberUpdatePw-Service");
		return memberDAO.memberIdCheck(mem_id);
	}

	/**
	 * 0718 소은수정
	 * 회원탈퇴시, 관련된 채팅방에서 나가야한다 
	 * 또한 친구신청 관계가 있다면 삭제하거나 상태를 변경해야한다
	 */
	@Override
	public List<String> memberDelete(String mem_id) {
		System.out.println("memberDelete-Service");
		List<String> getname = new ArrayList<>();
		getname.add(mem_id);
		Map<String, String> nameMap = memberDAO.memberInfoName(getname);
		int res = memberDAO.memberDelete(mem_id);
		List<String> outRoomIdList = null;
		if(res > 0){
			// 1. 소속된 모든 채팅방 탈퇴
			outRoomIdList = chatDAO.memAllRoomOut(mem_id); 
			if(outRoomIdList.size() > 0){
				for(String out : outRoomIdList){
					String name = nameMap.get(mem_id);
					ChatBubbleVO outMessage = new ChatBubbleVO();
					outMessage.setMem_id(mem_id);
					outMessage.setChatbubble_attach("2");
					outMessage.setChatbubble_text(name+"님이 나가셨습니다");
					outMessage.setChatroom_id(out);
					chatDAO.chatbubbleInsert(outMessage);
				}
			}
			
			// 2. 친구신청 관계테이블 데이터 삭제 및 수정
			RelationsVO relations = new RelationsVO();
			relations.setRel_firstMem(mem_id);
			List<RelationsVO> allRel = relationsDAO.allMemRelations(relations);
			System.err.println("allRel : "+allRel.size()+", "+allRel.toString());
			if(allRel!=null){
				for(RelationsVO r : allRel){
					String me = r.getRel_firstMem();
					if(mem_id.equals(me)){ // 친구신청한 회원이 '나'면 초대데이터 삭제
						res = relationsDAO.rejectFriend(r);
						System.err.println("친구삭제 : "+res);
					}else{ // 친구신청받은 입장이면 관계 'D'로 업데이트
						res = relationsDAO.outMemRelations(r);
						System.err.println("친구삭제D : "+res);
					}
				}
			}
			relationsDAO.rejectFriend(relations);
		}
		return outRoomIdList;
	}

	@Override
	public Map<String, String> memberInfoName(List<String> chatMemberList) {
		return memberDAO.memberInfoName(chatMemberList);
	}
	
//	2018-07-20 문경추가
	@Override
	public String memberMyInfoPwUp(String mem_pw) {
		// TODO Auto-generated method stub
		System.out.println("memberMyInfoPwUp-Service");
		return memberDAO.memberMyInfoPwUp(mem_pw);
	}

	@Override
	public List<FriendVO> alertLogin(String mem_id) {
		// TODO Auto-generated method stub
		System.out.println("alertLogin-Service");
		return memberDAO.alertLogin(mem_id);
	}
	//	2018-07-23 현호 추가
	/* 회원 탈퇴여부 조회 위해 사용
	 * (non-Javadoc)
	 * @see kr.co.marueng.mtalk.service.member.ImemberService#checkOutUser(java.lang.String)
	 */
	@Override
	public MemberVO checkOutUser(String findId) {
		return memberDAO.checkOutUser(findId);
	}
	
	// 2018.07.24 문경 추가
	@Override
	public int mailupdate(MemberVO member) { 
		// TODO Auto-generated method stub
		return memberDAO.mailupdate(member);
	}
}
