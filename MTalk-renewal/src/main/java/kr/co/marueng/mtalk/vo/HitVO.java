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
@Alias("hitVO")
public class HitVO implements Serializable{
	private String hit_id;
	private String hit_date;
	private String hit_qty; 
	private String hit_year;
	private String hit_month;
	private String hit_day;
}
