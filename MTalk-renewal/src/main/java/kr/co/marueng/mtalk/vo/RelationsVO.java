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
@Alias("relationsVO")
public class RelationsVO implements Serializable{
	private String rel_firstMem;
	private String rel_secondMem;
	private String rel_status;
	private String rel_date;
}
