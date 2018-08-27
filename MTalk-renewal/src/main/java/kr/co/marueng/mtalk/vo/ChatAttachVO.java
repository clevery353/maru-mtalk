package kr.co.marueng.mtalk.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.ibatis.type.Alias;

/**
 * @author 방소은
 * @DATE 2018. 7. 7.
 * 채팅 첨부파일 VO
 */
@Data
@NoArgsConstructor
@Alias("chatAttachVO")
public class ChatAttachVO {

	private String chatattach_id; // 첨부파일 id
	private String chatbubble_id; // 해당하는 말풍선 id
	private String chatattach_original; // 원본파일명
	private String chatattach_path; // 서버저장경로
	private String chatattach_url; // 서버저장url
	private String chatattach_mime; // 파일 mime
	private String chatattach_size; // 파일 size
	private String chatattach_fancy; // 파일 fancy
	
	/**
	 * 말풍선vo에서 첨부파일 여부가 1이면 이 VO를 참조한다. 
	 */
}
