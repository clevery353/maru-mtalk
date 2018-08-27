package kr.co.marueng.mtalk.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 방소은
 * @DATE 2018. 7. 18.
 * 채팅방 검색 VO
 */
@Data
@NoArgsConstructor
@Alias("chatSearchVO")
public class ChatSearchVO {
	
	private String mem_id; // 로그인회원 아이디
	private String search_txt; // 검색어
	
}
