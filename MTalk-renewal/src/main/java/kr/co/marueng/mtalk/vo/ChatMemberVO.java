package kr.co.marueng.mtalk.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.ibatis.type.Alias;

/**
 * @author 방소은
 * @DATE 2018. 7. 7.
 * 채팅 참여자 VO
 */
@Data
@NoArgsConstructor
@Alias("chatMemberVO")
public class ChatMemberVO {

	private String chatroom_id; // 소속 채팅방 아이디 
	private String mem_id; // 참여자 아이디 
	private String chatroom_name; // 개인이 설정한 채팅방이름
	private String chatmember_entered; // 해당 참여자의 대화방 입장시간(최초참여시간)
	
	private String second_id; // 참여자가 아닌 아이디, 1:1 채팅생성시, 중복된 채팅방이 있는지 조회할때 사용
	/**
	 * 현재 시점의 채팅참여자들의 아이디들을 수집한다.
	 * 채팅방에서 참여자가 들어오거나 나가면 데이터를 insert/delete 해줘야한다.
	 * (물론 참여자가 기존에 작성했던 말풍선은 member테이블에서 아이디를 참조해오기 때문에
	 * 삭제시에도 말풍선 데이터에는 작성자 내용이 반영된다.) 
	 */
}
