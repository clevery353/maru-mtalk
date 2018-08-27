package kr.co.marueng.mtalk.dao.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.ChatAttachVO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.ChatMemberVO;
import kr.co.marueng.mtalk.vo.ChatReadVO;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.ChatSearchVO;
import kr.co.marueng.mtalk.vo.OgtagVO;

@Repository("chatDAO") //서비스임플의 변수명과 동일
public class ChatDAOImpl implements IChatDAO{
	
	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public String chatInsert(ChatRoomVO chatRoomVO, List<String> chatMemberList) {
		// 채팅방 생성
		sqlSession.insert("chatroom.chatroomInsert", chatRoomVO);
		
		for(String mem : chatMemberList){
			// 채팅참여자 insert 
			ChatMemberVO chatMemberVO = new ChatMemberVO();
			chatMemberVO.setChatroom_id(chatRoomVO.getChatroom_id());
			chatMemberVO.setMem_id(mem);
			sqlSession.insert("chatmember.chatmemberInsert", chatMemberVO);
			// 대화 읽음처리 default값 insert 
			ChatReadVO chatReadVO = new ChatReadVO();
			chatReadVO.setMem_id(mem);
			chatReadVO.setChatroom_id(chatRoomVO.getChatroom_id());
			sqlSession.insert("chatread.chatreadInsert", chatReadVO);
		}
		
		return chatRoomVO.getChatroom_id();
	}
	
	// 채팅목록 조회 메서드
	@Override
	public List<ChatRoomVO> chatSelectList(String mem_id) {
		return sqlSession.selectList("chatroom.chatroomSelect", mem_id);
	}

	// 마지막 채팅내용 조회 메서드
	@Override
	public ChatBubbleVO lastchatSelect(String chatroom_id) {
		return sqlSession.selectOne("chatbubble.last_chat", chatroom_id);
	}
	
	// 회원의 채팅방 입장시간 조회 메서드 
	@Override
	public ChatMemberVO selectChatEntered(ChatMemberVO chatMemberVO){
		return sqlSession.selectOne("chatmember.selectChatEntered", chatMemberVO);
	}
	// 1:1채팅 신청시 중복검사 메서드
	@Override
	public String duplicateChat(ChatMemberVO chatMemberVO) {
		List<String> list = sqlSession.selectList("chatmember.chatmemberSelectList", chatMemberVO);
		for(String chatroom_id : list){
			int cnt = Integer.parseInt(sqlSession.selectOne("chatmember.chatmemberSum", chatroom_id));
			if(cnt == 2){
				return chatroom_id;
			}
		}
		return null; // 동일한 채팅방이 없을시 null 반환
	}

	// 채팅방 기준 전체 말풍선 조회
	@Override
	public List<ChatBubbleVO> roomAllBubble(ChatMemberVO chatMemberVO){
		return sqlSession.selectList("chatbubble.chatroom_allBubble", chatMemberVO);
	}

	// 채팅 말풍선 insert 
	@Override
	public int chatbubbleInsert(ChatBubbleVO chatBubbleVO) {
		return sqlSession.insert("chatbubble.chatbubbleInsert", chatBubbleVO); 
	}

	// 채팅방에 참여중인 회원 아이디 조회
	@Override
	public List<String> chatMemberList(String chatroom_id) {
		return sqlSession.selectList("chatmember.chatmemberList", chatroom_id);
	}

	// 채팅방 클릭시 현재시간으로 말풍선 읽음 update
	@Override
	public int chatroomRead(ChatReadVO chatReadVO) {
		int result = sqlSession.update("chatread.chatreadUpdate", chatReadVO);
		return result;
	}

	// 채팅방 읽음 시간 조회
	@Override
	public List<ChatReadVO> chatreadDate(String chatroom_id) {
		return sqlSession.selectList("chatread.chatreadDate", chatroom_id);
	}

	// 아이디기준 모든 채팅방 읽은 시간 조회
	@Override
	public List<ChatReadVO> myReadList(String mem_id) {
		return sqlSession.selectList("chatread.myReadList", mem_id);
	}
	
	// 채팅방 나가기
	@Override
	public String chatroomOut(String outRoomId, String mem_id) {
		ChatMemberVO chatMemberVO = new ChatMemberVO();
		chatMemberVO.setChatroom_id(outRoomId);
		chatMemberVO.setMem_id(mem_id);
		int result = sqlSession.delete("chatmember.chatroomOut", chatMemberVO);
		String resultStr = "false";
		if(result > 0){
			// chatread 에서 해당 회원 데이터
			ChatReadVO chatReadVO = new ChatReadVO();
			chatReadVO.setChatroom_id(outRoomId);
			chatReadVO.setMem_id(mem_id);
			sqlSession.delete("chatread.chatreadDelete", chatReadVO);
			// 채팅방 참여자 다시 뽑아옴
			List<String> roomname = sqlSession.selectList("chatmember.chatroomRenameData", outRoomId);
			if(roomname.size() < 1){ // 참여자 전원 나가면 chatroom delete
				int del = sqlSession.delete("chatbubble.outroom_allBubbleDelete", outRoomId);
				/*if(del > 0){
					int del2 = sqlSession.delete("chatroom.chatroomDelete", outRoomId);
					if(del2 > 0){
						resultStr = "allout";
					}
				}*/
			}else{
				String rename = "";
				for(String name : roomname){
					rename += name+", ";				
				}
				rename = rename.substring(0, rename.length()-2);				
				// 현재 참여자목록으로 채팅방 이름 업데이트
				ChatRoomVO chatRoomVO = new ChatRoomVO();
				chatRoomVO.setChatroom_id(outRoomId);
				chatRoomVO.setChatroom_name(rename);
				int res = sqlSession.update("chatroom.chatroomRename", chatRoomVO);
				if(res > 0){
					resultStr = "ok";
				}
			}
		}
		
		return resultStr;
	}

	// 마지막 채팅확인시간
	@Override
	public String myLastReadDate(String mem_id) {
		return sqlSession.selectOne("chatread.myLastReadDate", mem_id);
	}

	// 마지막 채팅발생시간
	@Override
	public String myRoomLastChatDate(String mem_id) {
		return sqlSession.selectOne("chatbubble.myRoomLastChatDate", mem_id);
	}

	// 이 방에서의 마지막 버블아이디
	@Override
	public String currBubbleGetLastId(ChatBubbleVO chatBubbleVO) {
		return sqlSession.selectOne("chatbubble.currBubbleGetLastId", chatBubbleVO);
	}
	
	// 채팅 첨부파일 정보 insert
	@Override
	public int chatAttachInsert(ChatAttachVO chatAttachVO) {
		return sqlSession.insert("chatattach.attachInsert", chatAttachVO);
	}

	// 채팅 첨부파일 정보 select
	@Override
	public ChatAttachVO chatAttachSelect(String chatbubble_id) {
		return sqlSession.selectOne("chatattach.attachSelect", chatbubble_id);
	}

	// 채팅방에 참여자 추가
	@Override
	public String chatMemberUpdate(String chatroom_id, List<String> chatMemberList) {
		for(String mem : chatMemberList){
			ChatMemberVO chatMemberVO = new ChatMemberVO();
			chatMemberVO.setChatroom_id(chatroom_id);
			chatMemberVO.setMem_id(mem);
			int res = sqlSession.update("chatmember.chatmemberInsert", chatMemberVO);
			if(res == 0){
				return null;
			}else{
				// 대화 읽음처리 default값 insert 
				ChatReadVO chatReadVO = new ChatReadVO();
				chatReadVO.setMem_id(mem);
				chatReadVO.setChatroom_id(chatroom_id);
				sqlSession.insert("chatread.chatreadInsert", chatReadVO);
			}
		}
		// 채팅방 참여자 다시 뽑아옴
		List<String> roomname = sqlSession.selectList("chatmember.chatroomRenameData", chatroom_id);
		String rename = "";
		for(String name : roomname){
			rename += name+", ";	
		}
		rename = rename.substring(0, rename.length()-2);				
		// 현재 참여자목록으로 채팅방 이름 업데이트
		ChatRoomVO chatRoomVO = new ChatRoomVO();
		chatRoomVO.setChatroom_id(chatroom_id);
		chatRoomVO.setChatroom_name(rename);
		int res = sqlSession.update("chatroom.chatroomRename", chatRoomVO);
		if(res > 0){
			return chatroom_id;
		}else{
			return null;			
		}
	}

	// 모든 채팅방 나가기 (회원탈퇴시사용) : 0718 소은추가
	@Override
	public List<String> memAllRoomOut(String mem_id) {
		// 회원이 소속된 방목록을 조회하여 하나씩 방을 나간다.
		List<ChatMemberVO> memList = sqlSession.selectList("chatmember.chatBelongMember", mem_id);
		List<String> outRoomList = new ArrayList<String>();
		for(ChatMemberVO cmvo : memList){
			String outRoomId = cmvo.getChatroom_id();
			String resStr = chatroomOut(outRoomId, mem_id);
			outRoomList.add(outRoomId);
		}
		return outRoomList;
	}

	// 채팅방 검색 - 채팅참여자의 이름으로 검색 : 0718 소은추가
	@Override
	public List<ChatRoomVO> chatroomSearch(String mem_id, String search_txt) {
		ChatSearchVO chatSearchVO = new ChatSearchVO();
		chatSearchVO.setMem_id(mem_id);
		chatSearchVO.setSearch_txt(search_txt);
		List<ChatRoomVO> list = sqlSession.selectList("chatroom.chatroomMemSearch", chatSearchVO);
		if(list.size()==0){
			System.err.println("검색조건 없음, "+mem_id+", "+search_txt);
		}
		return list; 			
	}

	// 개인이 설정한 채팅방 이름 있는지 조회 : 0719 소은추가
	@Override
	public String myChatroomName(String mem_id, String chatroom_id) {
		ChatMemberVO mvo = new ChatMemberVO();
		mvo.setChatroom_id(chatroom_id);
		mvo.setMem_id(mem_id);
		ChatMemberVO res = sqlSession.selectOne("chatmember.myChatroomName", mvo);
		if(res!=null){
			if(res.getChatroom_name() != null){
				return res.getChatroom_name(); 
			}
		}
		return null;
	}
	
	// 채팅방 이름변경(개인설정) : 0719 소은추가
	@Override
	public int chatEditName(String mem_id, String chatroom_id, String chatroom_name) {
		ChatMemberVO mvo = new ChatMemberVO();
		mvo.setChatroom_id(chatroom_id);
		mvo.setChatroom_name(chatroom_name);
		mvo.setMem_id(mem_id);
		return sqlSession.update("chatmember.chatEditName", mvo);
	}

	// ogtag존재여부 확인 : 0719 소은추가
	@Override
	public OgtagVO selectOgtag(String url) {
		return sqlSession.selectOne("ogtag.selectOgtag", url);
	}
	
	// ogtag신규정보 insert : 0719 소은추가
	@Override
	public int insertOgtag(OgtagVO og) {
		return sqlSession.insert("ogtag.insertOgtag", og);
	}
	
	// 채팅방내 모든 대화내용 불러오기(채팅저장에사용) : 0720 소은추가
	@Override
	public List<ChatBubbleVO> chatbubbleAllSave(ChatMemberVO chatMemberVO) {
		return sqlSession.selectList("chatbubble.chatbubbleAllSave", chatMemberVO);
	}

	// 채팅방의 최초참여시간 변경
	@Override
	public int chatmemberEnteredUpdate(ChatMemberVO chatMemberVO) {
		return sqlSession.update("chatmember.chatmemberEnteredUpdate", chatMemberVO);
	}
	
	// 문경 추가 07-23
	@Override
	public int chatmemberCount(int chatroom_id) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("chatmember.ChatIdMemberCount", chatroom_id);
	}
	// 문경 추가 07-23 유저이름 변경시 채팅방 이름 목록 불러오기
	@Override
	public List<ChatRoomVO> ChatMemNameUpdate(String mem_id) {
		// TODO Auto-generated method stub
		System.out.println("여긴들어온당.");
		List<ChatRoomVO> list_cr =  sqlSession.selectList("chatmember.ChatMemNameUpdate",mem_id);
		if(list_cr != null) System.out.println("리스트 값 있엉!");
		return list_cr;
	}
	// 문경 추가 07-23 변경된 채팅방이름을 insert
	@Override
	public int UpdateRoomName_mem(ChatRoomVO vo) {
		// TODO Auto-generated method stub
		return sqlSession.update("chatroom.updateroomname",vo);
	}
	
	
}
