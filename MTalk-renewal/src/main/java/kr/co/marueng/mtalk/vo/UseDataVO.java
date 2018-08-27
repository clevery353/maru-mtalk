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
@Alias("useDataVO")
public class UseDataVO implements Serializable{
	private String use_id;
	private String use_mem;
	private String use_memName;
	private String use_date;
	private String use_qty;
	private String use_year;
	private String use_month;
	private String use_day;
}
