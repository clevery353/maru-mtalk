package kr.co.marueng.mtalk.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 방소은
 * @DATE 2018. 7. 7.
 * 채팅 말풍선 VO
 */
@Data
@NoArgsConstructor
@Alias("chatBubbleVO")
public class ChatBubbleVO {

	private String chatbubble_id; // 말풍선 아이디 (data = 채팅방아이디_시퀀스 / ex: cr1_1)
	private String mem_id; // 작성자 아이디
	private String chatbubble_text; // 말풍선 텍스트 내용 
	private String chatbubble_date; // 말풍선 작성시간 ex) 2018-07-07 12:22:30
	private String chatbubble_attach; // 말풍선이 첨부파일인지 여부, 텍스트=0, 첨부파일=1, 채팅참여상태메시지=2 
	private String chatroom_id; // 소속 채팅방 아이디
	
	private String mem_name; // 작성자 이름
	
	private String bubPropic; // 작성자 프로필사진 임시그릇변수 : 0718 소은추가
	
	/**
	 * '말풍선'은 회원이 적는 하나의 채팅객체.
	 * 첨부파일여부 컬럼으로 텍스트인지, 첨부파일인지 구분한다.
	 * 첨부파일이라면 ChatAttachVO에서 파일내용을 참조하여 View로 넘긴다.
	 */
}
