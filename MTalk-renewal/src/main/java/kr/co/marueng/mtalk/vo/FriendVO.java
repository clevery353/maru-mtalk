package kr.co.marueng.mtalk.vo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 곽현호
 * @DATE 2018. 7. 5.
 */
@Data
@NoArgsConstructor
@Alias("friendVO")
public class FriendVO implements Serializable{
	private String fri_id; //친구목록 아이디, 시퀀스라 의미 없음
	private String fri_firstmem; //회원 아이디, 어떤 회원의 친구목록인지 구분일 때 사용
	private String fri_friendmem; //친구 회원 아이디
	private String fri_friendmem_name; //친구회원 이름
	private String fri_friendGroupId; //해당 친구의 친구그룹 설정
	private String fri_friendGroupName; //해당 친구의 친구그룹 이름
	private String fri_bookMark; //해당 친구의 즐겨찾기 추가여부
	private String fri_friendProPic; //해당 친구의 프로필 사진
	private String fri_friendProText; //해당 친구의 프로필
}
