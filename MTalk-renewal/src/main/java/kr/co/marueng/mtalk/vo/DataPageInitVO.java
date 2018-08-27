package kr.co.marueng.mtalk.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("dataPageInitVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPageInitVO implements Serializable {
	private List<HitVO> hitList;
	private List<UseDataVO> useDataList;
	private List<ChatRoomVO> chatRoomList;
	private List<MemberVO> memberList;
}
