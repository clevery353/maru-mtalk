package kr.co.marueng.mtalk.vo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 곽현호
 * @DATE 2018. 7. 5.
 */
@Data
@NoArgsConstructor
@Alias("friendGroupVO")
public class FriendGroupVO implements Serializable {
	private String friendGroup_id; //친구 그룹 아이디 시퀀스로 의미없는 아이디
	private String friendGroup_name; // 친구 그룹 이름
	private String friendGroup_member; //회원 아이디. 어떤 회원의 그룹인지 구분
}
