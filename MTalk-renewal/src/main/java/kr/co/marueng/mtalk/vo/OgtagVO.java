package kr.co.marueng.mtalk.vo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 방소은
 * @DATE 2018. 7. 19.
 * OGTAG VO
 */
@Data
@NoArgsConstructor
@Alias("ogtagVO")
public class OgtagVO implements Serializable{
	
	private int ogtag_id; // og아이디
	private String ogtag_title; // og:title
	private String ogtag_url; // og:url (실제url)
	private String ogtag_inputurl; // 사용자가 말풍선에 적은 url
	private String ogtag_description; // og:description
	private String ogtag_image; // og:image
	
	/**0719 소은추가 
	 * DB에서 ogtag 테이블에 동일한 url값이 있으면 가져오고,
	 * 동일한 url값이 없으면 새로 크롤링 하여 DB에 insert한다.
	 */
}
