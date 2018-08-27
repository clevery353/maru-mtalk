package kr.co.marueng.mtalk.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.ibatis.type.Alias;

/**
 * @author 방소은
 * @DATE 2018. 7. 7.
 * 말풍선 읽음 여부 VO
 */
@Data
@NoArgsConstructor
@Alias("chatReadVO")
public class ChatReadVO {

	private String chatread_date; // 채팅방 읽은 시각
	private String chatroom_id; // 채팅방 아이디
	private String mem_id; // 읽은 회원 아이디
	
	/**
	 * 회원이 채팅방에 들어와 읽는 순간, 채팅방에 존재하는 모든 말풍선 아이디를 조회하여 읽음여부 데이터를 insert 한다.
	 * 동시에 채팅방 참여자들에게는 
	 * 말풍선정보 Map<말풍선아이디, 읽은 회원수> 를 보내고,
	 * 채팅방 화면단에서는 (총 채팅방참여자 수) - (읽은 회원수) = 읽음표시 숫자 로 표현한다.   
	 */
}
