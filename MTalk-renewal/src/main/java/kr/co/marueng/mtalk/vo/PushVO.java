package kr.co.marueng.mtalk.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;


/* 
 * 알림VO
 * 조문경
 * 
 * */

@Data
@NoArgsConstructor
@Alias("pushVO")
public class PushVO {
	private String push_id;
	private String push_mem;
	private String push_type;
	private String push_status;

}
