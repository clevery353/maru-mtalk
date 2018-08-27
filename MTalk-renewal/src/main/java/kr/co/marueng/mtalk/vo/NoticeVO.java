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
@Alias("noticeVO")
public class NoticeVO implements Serializable {
	private String noti_id;
	private String noti_writer;
	private String noti_title;
	private String noti_date;
	private String noti_text;
	
	private Integer rnum;
}
