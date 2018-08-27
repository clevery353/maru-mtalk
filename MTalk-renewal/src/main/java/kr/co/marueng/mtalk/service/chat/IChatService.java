package kr.co.marueng.mtalk.service.chat;

import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.ChatAttachVO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.ChatReadVO;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.OgtagVO;

public interface IChatService {
	/**
	 * 1:1채팅신청 
	 * @param mem_id 최초생성자아이디, List<String> chatMemberList 총 채팅참여자 리스트 
	 * @return 생성된 채팅방ID
	 */
	public String chatInsert(String mem_id, List<String> chatMemberList);
	/**
	 * 채팅방 리스트조회
	 * @param mem_id
	 * @return 채팅방VO리스트
	 */
	public List<ChatRoomVO> chatSelectList(String mem_id);
	/**
	 * 채팅방내 모든 말풍선 조회 / 읽음과 동시에 ChatRead데이터 insert
	 * @param chatroom_id, mem_id
	 * @return 말풍선VO 리스트 반환
	 */
	public List<ChatBubbleVO> roomAllBubble(String chatroom_id, String mem_id);
	/**
	 * 마지막 말풍선 조회
	 * @param chatroom_id
	 * @return ChatBubbleVO
	 */
	public ChatBubbleVO lastchatSelect(String chatroom_id);
	/**
	 * 말풍선 insert 
	 * @param chatBubbleVO
	 * @return 성공 1, 실패 0
	 */
	public int chatbubbleInsert(ChatBubbleVO chatBubbleVO);
	/**
	 * 회원별 해당 채팅방 읽은 시각 조회
	 * @param chatroom_id
	 * @return 채팅방 기준의 마지막 채팅방 조회 시각 리스트 반환
	 */
	public List<ChatReadVO> chatreadDate(String chatroom_id);
	/**
	 * 채팅방 리스트에 조회시 내가 읽지 않은 채팅방 확인을 위해, 채팅방 읽음 시간 리스트 조회
	 * @param mem_id
	 * @return 접속자 기준의 채팅방 조회 시각 리스트 반환
	 */
	public List<ChatReadVO> myReadList(String mem_id);
	/**
	 * 채팅방 나가기
	 * @param outRoomId(나갈채팅방아이디), mem_id(나갈회원아이디)
	 * @return 성공 ok, 실패 false
	 */
	public String chatroomOut(String outRoomId, String mem_id);
	/**
	 * 첨부파일 조회
	 * @param chatbubble_id
	 * @return chatAttachVO
	 */
	public ChatAttachVO attachSelectOne(String chatbubble_id);
	/**
	 * 채팅방 친구초대 
	 * @param mem_id
	 * @param chatroom_id
	 * @param chatMemberList 초대목록
	 * @return 채팅방 아이디 
	 */
	public String chatInvite(String mem_id, String chatroom_id, List<String> chatMemberList);
	/**
	 * 참여중인 채팅인원 조회
	 * @param chatroom_id
	 * @return 채팅참여회원 아이디 리스트
	 */
	public List<String> chatMemberList(String chatroom_id);
	/**
	 * 채팅방에 채팅참여자 목록 확인
	 * @param mem_id, chatroom_id
	 * @return 참여자들의 프로필정보 리스트
	 */
	public List<MemberVO> currentChatMemList(String mem_id, String chatroom_id);
	/**
	 * 채팅방 검색 - 참여자 이름으로 검색
	 * @param mem_id
	 * @param search_txt
	 * @return 검색조건에 해당하는 채팅방리스트
	 */
	public List<ChatRoomVO> chatroomSearch(String mem_id, String search_txt);
	/**
	 * 채팅방이름 변경(개인설정) 
	 * @param mem_id
	 * @param chatroom_name
	 * @return 1 성공, 0 실패
	 */
	public int chatEditName(String mem_id, String chatroom_id, String chatroom_name);
	/**
	 * ogtag존재여부 확인
	 * @param url
	 * @return 존재시 ogtagVO, 없으면 null
	 */
	public OgtagVO selectOgtag(String url);
	/**
	 * 새로운 ogtag정보 insert
	 * @param result
	 * @return 성공 1이상, 실패 0
	 */
	public int insertOgtag(Map<String, List<String>> result);
	/**
	 * 채팅방의 모든 대화내용 불러오기(채팅저장에 사용)
	 * @param mem_id, chatroom_id
	 * @return 성공 List<ChatBubbleVO>, 실패 null
	 */
	public List<ChatBubbleVO> chatbubbleAllSave(String mem_id, String chatroom_id);
	/**
	 * 채팅방 대화내용 삭제(해당 회원의 최초참여시간을 바꾸어 이전대화 안보이도록 처리)
	 * @param mem_id
	 * @param chatroom_id
	 * @return 성공 1, 실패 0
	 */
	public int myChatDel(String mem_id, String chatroom_id);
	/**
	 * 문경 추가 0723
	 * 채팅방이름 변경시 명수도 같이 표현
	 * @param chatroom_id
	 * @return 채팅방 참여 명수
	 */
	public int chatmemberCount(int chatroom_id);
}
