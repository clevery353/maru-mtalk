package kr.co.marueng.mtalk.service.chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.chat.IChatDAO;
import kr.co.marueng.mtalk.dao.friend.IfriendDAO;
import kr.co.marueng.mtalk.dao.member.ImemberDAO;
import kr.co.marueng.mtalk.vo.ChatAttachVO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.ChatMemberVO;
import kr.co.marueng.mtalk.vo.ChatReadVO;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.OgtagVO;

@Service
public class ChatServiceImpl implements IChatService{

	@Inject
	IChatDAO chatDAO;
	@Inject
	ImemberDAO memberDAO;
	@Inject
	IfriendDAO friendDAO;

	// 채팅신청 메서드 
	@Override
	public String chatInsert(String mem_id, List<String> chatMemberList) {
		String duplicateChatId = null;
		if(chatMemberList.size() <= 2){ // 1:1채팅생성인지 확인
			ChatMemberVO chatMemberVO = new ChatMemberVO();
			chatMemberVO.setMem_id(mem_id);
			chatMemberVO.setSecond_id(chatMemberList.get(1));
			duplicateChatId = chatDAO.duplicateChat(chatMemberVO); // 1:1채팅이 존재하는지 여부			
		}
		
		if(duplicateChatId == null){
			ChatRoomVO chatRoomVO = new ChatRoomVO();
			// 채팅방명 생성 : 참여회원들의 이름 모두 가져오기
			Map<String, String> chatMemberMap= memberDAO.memberInfoName(chatMemberList);
			Iterator<String> ikey = chatMemberMap.keySet().iterator();
			String roomName = "";
			while(ikey.hasNext()){
				roomName += chatMemberMap.get(ikey.next())+", ";
			}
			roomName = roomName.substring(0, roomName.length()-2);
			chatRoomVO.setChatroom_name(roomName);
			chatRoomVO.setMem_id(mem_id); // 최초 방 생성자
			String chatroom_id = chatDAO.chatInsert(chatRoomVO, chatMemberList);
			
			// 방생성 문구 
			List<String> inviteList = new ArrayList<>();
			chatMemberList.remove(mem_id);
			inviteList.add(mem_id);
			inviteList.addAll(chatMemberList);
			Map<String, String> nameMap = memberDAO.memberInfoName(inviteList);
			String inviteBub = "";
			for(int i=0; i<inviteList.size(); i++){
				String name = nameMap.get(inviteList.get(i));
				if(mem_id.equals(inviteList.get(i))){
					inviteBub += name+"님이 ";
				}else if(i!=0 && i==inviteList.size()-1){
					inviteBub += name+"님과의 방을 생성하였습니다.";
				}else{
					inviteBub += name+"님, ";
				}
			}
			ChatBubbleVO inviteMessage = new ChatBubbleVO();
			inviteMessage.setMem_id(mem_id);
			inviteMessage.setChatbubble_attach("2");
			inviteMessage.setChatbubble_text(inviteBub);
			inviteMessage.setChatroom_id(chatroom_id);
			int create = chatDAO.chatbubbleInsert(inviteMessage);
			if(create < 1){
				System.err.println("생성하셨습니다 문구 실패!");
			}
			return chatroom_id;
		}else{
			return duplicateChatId;
		}
	}

	// 채팅방 리스트 조회  : 0718 소은수정
	@Override
	public List<ChatRoomVO> chatSelectList(String mem_id) {
		List<ChatRoomVO> chatRoomList = chatDAO.chatSelectList(mem_id);
		for(ChatRoomVO vo : chatRoomList){
			ChatBubbleVO lastBubble = chatDAO.lastchatSelect(vo.getChatroom_id());
			if(lastBubble!=null){
				ChatMemberVO cmvo = new ChatMemberVO();
				cmvo.setMem_id(mem_id);
				cmvo.setChatroom_id(vo.getChatroom_id());
				cmvo = chatDAO.selectChatEntered(cmvo);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date lastChatDate = null;
				Date enteredDate = null;
				try {
					lastChatDate = format.parse(lastBubble.getChatbubble_date());
					enteredDate = format.parse(cmvo.getChatmember_entered());				
				}catch(ParseException e){
					e.printStackTrace();
				}
				
				int compare = lastChatDate.compareTo(enteredDate);
				if( compare > 0 ){
					vo.setLast_date(lastBubble.getChatbubble_date());
					vo.setLast_text(lastBubble.getChatbubble_text());					
				}
			}
			// 개인이 설정한 채팅방 이름 조회
			String myChatName = chatDAO.myChatroomName(mem_id, vo.getChatroom_id());
			if(myChatName!=null){
				vo.setChatroom_name(myChatName); 
			}
			// 1:1채팅 = 친구프사, 없으면 기본이미지 /  그룹채팅 = 그룹이미지
			List<String> cmem = chatMemberList(vo.getChatroom_id()); // 해당참여자 아이디불러와서
			if(cmem.size() < 3){ 
				if(cmem.size() > 1){
					for(String m : cmem){
						if(!mem_id.equals(m)){ // 친구의 프로필사진 얻어온다
							MemberVO memberVO = memberDAO.selectRelationsMember(m);
							if(memberVO!=null){
								if(memberVO.getMem_propic() == null){
									vo.setChatPic("chat-userpic.png");
								}else{
									vo.setChatPic(memberVO.getMem_propic());															
								}
							}else{
								vo.setChatPic("chat-userpic.png");
							}
						}
					}
				}else{ // 참여자가 나밖에 없는경우
					vo.setChatPic("chat-userpic.png");
				}
			}else{ // 그룹채팅이면
				vo.setChatPic("group-propic.png");
			}
		}
		return chatRoomList;
	}

	// 채팅창 말풍선 조회 
	@Override
	public List<ChatBubbleVO> roomAllBubble(String chatroom_id, String mem_id){
		ChatMemberVO chatMemberVO = new ChatMemberVO();
		chatMemberVO.setChatroom_id(chatroom_id);
		chatMemberVO.setMem_id(mem_id);
		List<ChatBubbleVO> bubbleList = chatDAO.roomAllBubble(chatMemberVO);
		if(bubbleList.size() > 0){
			bubbleList = memberDAO.bubbleMemberName(bubbleList); // 채팅작성자 이름 넣음
			ChatReadVO chatReadVO = new ChatReadVO();
			chatReadVO.setChatroom_id(chatroom_id);
			chatReadVO.setMem_id(mem_id);
			chatDAO.chatroomRead(chatReadVO); // 말풍선 읽음처리				
		}else{
			bubbleList = new ArrayList<>();
		}
		return bubbleList;
	}

	// 마지막 채팅내용 조회
	@Override
	public ChatBubbleVO lastchatSelect(String chatroom_id) {
		return chatDAO.lastchatSelect(chatroom_id);
	}

	// 말풍선 insert
	@Override
	public int chatbubbleInsert(ChatBubbleVO chatBubbleVO) {
		int result = chatDAO.chatbubbleInsert(chatBubbleVO);
		if(result > 0){
			ChatReadVO chatReadVO = new ChatReadVO();
			chatReadVO.setChatroom_id(chatBubbleVO.getChatroom_id());
			chatReadVO.setMem_id(chatBubbleVO.getMem_id());
			result = chatDAO.chatroomRead(chatReadVO);
		}
		return result;
	}

	// 채팅방 읽은 시각 리스트 조회
	@Override
	public List<ChatReadVO> chatreadDate(String chatroom_id) {
		return chatDAO.chatreadDate(chatroom_id);
	}

	// 내가 읽었던 모든 채팅방 시각 리스트 조회
	@Override
	public List<ChatReadVO> myReadList(String mem_id) {
		return chatDAO.myReadList(mem_id);
	}

	// 해당 채팅방 나가기
	@Override
	public String chatroomOut(String outRoomId, String mem_id) {
		String result = chatDAO.chatroomOut(outRoomId, mem_id);
		if("ok".equals(result)){
			List<String> getname = new ArrayList<>();
			getname.add(mem_id);
			Map<String, String> nameMap = memberDAO.memberInfoName(getname);
			String name = nameMap.get(mem_id);
			ChatBubbleVO outMessage = new ChatBubbleVO();
			outMessage.setMem_id(mem_id);
			outMessage.setChatbubble_attach("2");
			outMessage.setChatbubble_text(name+"님이 나가셨습니다");
			outMessage.setChatroom_id(outRoomId);
			int out = chatDAO.chatbubbleInsert(outMessage);
			if(out > 0){
				result = "ok";
			}else{
				result = "false";
			}
		}else if("allout".equals(result)){
			result = "ok";
		}
		return result;
	}

	@Override
	public ChatAttachVO attachSelectOne(String chatbubble_id) {
		return chatDAO.chatAttachSelect(chatbubble_id);
	}

	// 채팅방 초대 0717 소은수정
	@Override
	public String chatInvite(String mem_id, String chatroom_id, List<String> chatMemberList) {
		
		List<String> isOnechat = chatDAO.chatMemberList(chatroom_id); // 1:1채팅인지 여부
		if(isOnechat.size() > 2){ // 그룹채팅방 = 기존방에 참여자 추가
			chatroom_id = chatDAO.chatMemberUpdate(chatroom_id, chatMemberList);
			if(chatroom_id==null){
				return "fail";
			}else{
				List<String> inviteList = new ArrayList<>();
				inviteList.add(mem_id);
				chatMemberList.remove(mem_id);
				inviteList.addAll(chatMemberList);
				Map<String, String> nameMap = memberDAO.memberInfoName(inviteList);
				String inviteBub = "";
				for(int i=0; i<inviteList.size(); i++){
					String name = nameMap.get(inviteList.get(i));
					if(i==0){
						inviteBub += name+"님이 ";
					}else if(i==inviteList.size()-1){
						inviteBub += name+"님을 초대하셨습니다.";
					}else{
						inviteBub += name+"님, ";
					}
				}
				ChatBubbleVO inviteMessage = new ChatBubbleVO();
				inviteMessage.setMem_id(mem_id);
				inviteMessage.setChatbubble_attach("2");
				inviteMessage.setChatbubble_text(inviteBub);
				inviteMessage.setChatroom_id(chatroom_id);
				int out = chatDAO.chatbubbleInsert(inviteMessage);
				if(out > 0){
					return chatroom_id;									
				}else{
					System.err.println("초대하셨습니다 문구 실패!");
					return chatroom_id;
				}
			}
		}else{ // 1:1채팅방 or 비활성 채팅방 = 새로생성
			ChatRoomVO chatRoomVO = new ChatRoomVO();
			for(String mem : isOnechat){
				chatMemberList.add(mem);
			}
			// 채팅방명 생성 : 참여회원들의 이름 모두 가져오기
			Map<String, String> chatMemberMap= memberDAO.memberInfoName(chatMemberList);
			Iterator<String> ikey = chatMemberMap.keySet().iterator();
			String roomName = "";
			while(ikey.hasNext()){
				roomName += chatMemberMap.get(ikey.next())+", ";
			}
			roomName = roomName.substring(0, roomName.length()-2);
			chatRoomVO.setChatroom_name(roomName);
			chatRoomVO.setMem_id(mem_id); // 최초 방 생성자
			String newroom_id = chatDAO.chatInsert(chatRoomVO, chatMemberList);
			
			// 방생성 문구 
			List<String> inviteList = new ArrayList<>();
			chatMemberList.remove(mem_id);
			inviteList.add(mem_id);
			inviteList.addAll(chatMemberList);
			Map<String, String> nameMap = memberDAO.memberInfoName(inviteList);
			String inviteBub = "";
			for(int i=0; i<inviteList.size(); i++){
				String name = nameMap.get(inviteList.get(i));
				if(mem_id.equals(inviteList.get(i))){
					inviteBub += name+"님이 ";
				}else if(i!=0 && i==inviteList.size()-1){
					inviteBub += name+"님과의 방을 생성하였습니다.";
				}else{
					inviteBub += name+"님, ";
				}
			}
			ChatBubbleVO inviteMessage = new ChatBubbleVO();
			inviteMessage.setMem_id(mem_id);
			inviteMessage.setChatbubble_attach("2");
			inviteMessage.setChatbubble_text(inviteBub);
			inviteMessage.setChatroom_id(newroom_id);
			int out = chatDAO.chatbubbleInsert(inviteMessage);
			if(out < 1){
				System.err.println("생성하셨습니다 문구 실패!");
			}
			return newroom_id;
		}
	}

	@Override
	public List<String> chatMemberList(String chatroom_id) {
		return chatDAO.chatMemberList(chatroom_id);
	}
	
	// 채팅참여자의 프로필정보 조회 : 0718 소은추가
	@Override
	public List<MemberVO> currentChatMemList(String mem_id, String chatroom_id) {
		List<String> chatMemberList = chatDAO.chatMemberList(chatroom_id); // 참여자 아이디들
		List<MemberVO> currentChatMemList = new ArrayList<MemberVO>(); // 반환될 프로필묶음
		for(String mem : chatMemberList){
			MemberVO member = memberDAO.selectRelationsMember(mem); // 해당 회원 프로필조회
			FriendVO friend = new FriendVO(); // 친구관계인지 확인
			friend.setFri_firstmem(mem_id);
			friend.setFri_friendmem(mem);
			FriendVO res = friendDAO.isFriend(friend);
			if(res!=null)
				member.setIsFriend("친구");
			currentChatMemList.add(member);
		}
		return currentChatMemList;
	}

	// 채팅방 검색 : 채팅방이름 or 채팅참여자로 검색된 방들을 반환 : 0718 소은추가
	@Override
	public List<ChatRoomVO> chatroomSearch(String mem_id, String search_txt) {
		return chatDAO.chatroomSearch(mem_id, search_txt);
	}

	// 채팅방 이름 변경(개인설정) : 0719 소은추가
	@Override
	public int chatEditName(String mem_id, String chatroom_id, String chatroom_name) {
		return chatDAO.chatEditName(mem_id, chatroom_id, chatroom_name);
	}

	// ogtag 데이터 있는지 조회
	@Override
	public OgtagVO selectOgtag(String inputurl) {
		return chatDAO.selectOgtag(inputurl);
	}

	// ogtag 신규데이터 insert 
	@Override
	public int insertOgtag(Map<String, List<String>> result) {
		OgtagVO og = new OgtagVO();
		if(result.get("og:url")!=null){
			og.setOgtag_url(result.get("og:url").get(0));			
		}else{
			og.setOgtag_url(result.get("inputurl").get(0));
		}
		if(result.get("og:image")!=null){
			og.setOgtag_image(result.get("og:image").get(0));
		}else{
			og.setOgtag_image("res/ProfileImageUpload/site-default.png");
		}
		og.setOgtag_title(result.get("og:title").get(0));
		og.setOgtag_description(result.get("og:description").get(0));
		og.setOgtag_inputurl(result.get("inputurl").get(0));
		int res = chatDAO.insertOgtag(og);
		return res;
	}

	// 참여자가 참여한 시점부터의 채팅방내 말풍선정보 조회(채팅저장)
	@Override
	public List<ChatBubbleVO> chatbubbleAllSave(String mem_id, String chatroom_id) {
		ChatMemberVO chatMemberVO = new ChatMemberVO();
		chatMemberVO.setChatroom_id(chatroom_id);
		chatMemberVO.setMem_id(mem_id);
		return chatDAO.chatbubbleAllSave(chatMemberVO);
	}

	@Override
	public int myChatDel(String mem_id, String chatroom_id) {
		ChatMemberVO chatMemberVO = new ChatMemberVO();
		chatMemberVO.setChatroom_id(chatroom_id);
		chatMemberVO.setMem_id(mem_id);
		return chatDAO.chatmemberEnteredUpdate(chatMemberVO);
	}
	
	/* 채팅 참여자 수 가져옴 - 문경추가 0723 */
	@Override
	public int chatmemberCount(int chatroom_id) {
		return chatDAO.chatmemberCount(chatroom_id);
	}
}
