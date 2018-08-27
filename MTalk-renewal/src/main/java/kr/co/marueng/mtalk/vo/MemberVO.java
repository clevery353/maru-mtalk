package kr.co.marueng.mtalk.vo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 곽현호
 * @DATE 2018. 7. 4.
 */
/**
 * @author 곽현호
 * @DATE 2018. 7. 5.
 */
@Data
@NoArgsConstructor
@Alias("memberVO")
public class MemberVO implements Serializable {
	@NotBlank(message="회원아이디는 필수 입력사항입니다.")
	private String mem_id;
	@NotBlank(message="비밀번호는 필수 입력사항입니다.")
	private String mem_pw;
	@NotBlank(message="별명은 필수 입력사항입니다.")
	private String mem_name;
	private String mem_mail;
	private String mem_protext;
	private String mem_type;
	private String mem_status;
	//private MultipartFile mem_proPicture;
	private String mem_propic; //DB Access용
	private String mem_date;
	private String isFriend; // 해당 회원이 친구인지 확인하는 임시그릇변수 : 0717 소은추가
	private String use_data; //해당 회원의 데이터 총 사용량 임시그릇 변수:0722 현호추가
	private String use_year;
	private String use_month;
	private String use_day;
	
	
}
