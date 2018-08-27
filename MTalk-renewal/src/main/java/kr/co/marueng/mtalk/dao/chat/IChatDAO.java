package kr.co.marueng.mtalk.dao.chat;

import java.util.List;

import kr.co.marueng.mtalk.vo.ChatAttachVO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.ChatMemberVO;
import kr.co.marueng.mtalk.vo.ChatReadVO;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.OgtagVO;

public interface IChatDAO {
	/**
	 * 1:1채팅신청 
	 * @param chatRoomVO
	 * @param List<String> chatMemberList 총 참여자 리스트
	 * @return 생성된 채팅방ID
	 */
	public String chatInsert(ChatRoomVO chatRoomVO, List<String> chatMemberList);
	
	/**
	 * 채팅방 리스트 조회
	 * @param mem_id
	 * @return 채팅방VO 리스트 
	 */
	public List<ChatRoomVO> chatSelectList(String mem_id);
	/**
	 * 해당 채팅방의 마지막 대화 텍스트 조회
	 * @param chatroom_id
	 * @return ChatBubbleVO(last_text, chatbubble_date)
	 */
	public ChatBubbleVO lastchatSelect(String chatroom_id);
	/**
	 * 중복된 1:1 대화방이 있는지 조회
	 * @param chatMemberVO
	 * @return 존재시, 채팅방ID 반환 / 비존재시 null 반환
	 */
	public String duplicateChat(ChatMemberVO chatMemberVO);
	/**
	 * 하나의 채팅 말풍선 insert
	 * @param chatBubbleVO
	 * @return 성공 1, 실패 0
	 */
	public int chatbubbleInsert(ChatBubbleVO chatBubbleVO);
	/**
	 * 채팅방내에 모든 말풍선 조회
	 * @param chatroom_id
	 * @return ChatBubbleVO 리스트 반환
	 */
	public List<ChatBubbleVO> roomAllBubble(ChatMemberVO chatMemberVO);
	/**
	 * 해당 채팅방에 참여자들 조회
	 * @param chatroom_id
	 * @return 참여자 아이디 리스트 반환
	 */
	public List<String> chatMemberList(String chatroom_id);
	/**
	 * 해당 채팅방에 모든 말풍선 읽음처리
	 * @param chatReadVO / chatroom_id, mem_id
	 * @return 1이상 성공
	 */
	public int chatroomRead(ChatReadVO chatReadVO);
	/**
	 * 채팅읽음 현황 조회
	 * @param chatroom_id
	 * @return 해당 채팅방의 회원들이 마지막 채팅확인시간 리스트 반환
	 */
	public List<ChatReadVO> chatreadDate(String chatroom_id);
	/**
	 * 아이디기준 채팅방 읽은 현황 조회 
	 * @param mem_id
	 * @return 해당 아이디의 모든 채팅확인시간 리스트 반환
	 */
	public List<ChatReadVO> myReadList(String mem_id);
	/**
	 * 해당 채팅방 나가기
	 * @param outRoomId
	 * @param mem_id
	 * @return 성공여부 문자열 반환 ok, false
	 */
	public String chatroomOut(String outRoomId, String mem_id);
	/**
	 * 내가 마지막에 채팅을 확인한 시간 조회 
	 * @param mem_id
	 * @return 마지막 채팅확인시간
	 */
	public String myLastReadDate(String mem_id);
	/**
	 * 내가 존재하는 방들의 마지막 채팅발생 시간 조회
	 * @param mem_id
	 * @return 마지막 채팅발생시간
	 */
	public String myRoomLastChatDate(String mem_id);
	/**
	 * 현재 채팅방에서의 마지막 추가된 bubble아이디
	 * @param chatBubbleVO
	 * @return chatbubble_id
	 */
	public String currBubbleGetLastId(ChatBubbleVO chatBubbleVO);
	/**
	 * 첨부파일 정보 insert
	 * @param chatAttachVO
	 * @return 1이상 성공
	 */
	public int chatAttachInsert(ChatAttachVO chatAttachVO);
	/**
	 * 첨부파일 정보 select
	 * @param chatbubble_id
	 * @return 첨부파일vo
	 */
	public ChatAttachVO chatAttachSelect(String chatbubble_id);
	/**
	 * 채팅방에 참여자 추가
	 * @param chatroom_id
	 * @param chatMemberList
	 * @return 성공시 chatroom_id, 실패 null 
	 */
	public String chatMemberUpdate(String chatroom_id, List<String> chatMemberList);
	/**
	 * 탈퇴시 모든 채팅방에서 나가기 : 0718 소은추가
	 * @param mem_id
	 * @return 나가기한 모든 방들의 아이디 리스트
	 */
	public List<String> memAllRoomOut(String mem_id);
	/**
	 * 채팅방 검색 - 참여자이름으로 검색 : 0718 소은추가
	 * @param mem_id
	 * @param search_txt
	 * @return 검색조건에 해당하는 채팅방리스트
	 */
	public List<ChatRoomVO> chatroomSearch(String mem_id, String search_txt);
	/**
	 * 개인이 설정한 채팅방이름이 있는지 조회 : 0719 소은추가
	 * @param mem_id
	 * @param chatroom_id
	 * @return 있으면 chatroom_name, 없으면 null반환
	 */
	public String myChatroomName(String mem_id, String chatroom_id);
	/**
	 * 채팅방 이름변경 : 0719 소은추가
	 * @param mem_id
	 * @param chatroom_id
	 * @param chatroom_name
	 * @return 1 성공, 0 실패
	 */
	public int chatEditName(String mem_id, String chatroom_id, String chatroom_name);
	/**
	 * ogtag정보 조회 : 0719 소은추가
	 * @param url
	 * @return OgtagVO, 존재하지 않으면 null
	 */
	public OgtagVO selectOgtag(String url);
	/**
	 * ogtag 신규정보 insert : 0719 소은추가
	 * @param og
	 * @return 성공 1, 실패 0
	 */
	public int insertOgtag(OgtagVO og);
	/**
	 * 해당 채팅방 모든 대화내용 불러오기(단, 해당회원이 최초입장한시간부터)
	 * @param chatMemberVO
	 * @return 성공 List<ChatBubbleVO>, 실패 null
	 */
	public List<ChatBubbleVO> chatbubbleAllSave(ChatMemberVO chatMemberVO);
	/**
	 * 채팅방 참여자의 최초참여시간을 현재시점으로 update
	 * @param chatMemberVO
	 * @return 성공 1, 실패 0
	 */
	public int chatmemberEnteredUpdate(ChatMemberVO chatMemberVO);
	/**
	 * 해당 채팅방에 해당 회원이 최초 입장한 시간 조회
	 * @param chatMemberVO
	 * @return chatMemberVO
	 */
	public ChatMemberVO selectChatEntered(ChatMemberVO chatMemberVO);
	/**
	 * 문경 추가 0723
	 * 채팅방이름 변경시 명수도 같이 표현
	 * @param chatroom_id
	 * @return 채팅방 참여 명수
	 */
	public int chatmemberCount(int chatroom_id);
	/**
	 * 문경 추가 0723
	 * 유저가 이름 변경시 채팅방 명에도 변경 적용
	 * @param mem_id
	 * @return chatroomVO
	 */
	public List<ChatRoomVO> ChatMemNameUpdate(String mem_id);
	/**
	 * 문경 추가 0723
	 * 변경된 유저이름을 채팅방 이름에 추가
	 * @param chatRoomVO
	 * @return int
	 */
	public int UpdateRoomName_mem(ChatRoomVO vo);
}
