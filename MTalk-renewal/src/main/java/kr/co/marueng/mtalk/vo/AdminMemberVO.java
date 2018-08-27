package kr.co.marueng.mtalk.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("adminMemberVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberVO<T> implements Serializable {
	private List<T> memberList;
	private List<T> joinList;
}
