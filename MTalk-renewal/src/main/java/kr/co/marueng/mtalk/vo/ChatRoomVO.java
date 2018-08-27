package kr.co.marueng.mtalk.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 방소은
 * @DATE 2018. 7. 7.
 * 채팅방 VO
 */
@Data
@NoArgsConstructor
@Alias("chatRoomVO")
public class ChatRoomVO {
	
	private String chatroom_id; // 채팅방 아이디 (auto_increment ex: 1)
	private String chatroom_name; // 채팅방 이름
	private String mem_id; // 최초 생성한 회원 아이디
	
	private String last_text; // 마지막 채팅내용
	private String last_date; // 마지막 채팅시간
	
	private String memReadDate; // 개인의 해당채팅방 읽은 시간 임시저장
	/**방소은 추가 2018-07-18*/
	private String chatPic; // 채팅방 대표사진으로 사용할 임시그릇변수 
	
	/**곽현호추가2018-07-19*/
	private String chatroom_mem; //채팅참여자 이름
	private List<String> chatMemList; //채팅참여자 이름의 리스트
	
	
	/**
	 * 채팅방 이름은 ChatMember테이블에서 회원목록을 가져와서, 그 회원의 아이디로 회원명을 가져와서 콤마로 구분하고 나열하여 만든다.
	 * 참여자가 변동될 시, 채팅방 이름도 update되어야 한다.
	 */
}
