package kr.co.marueng.mtalk.dao.admin.chat;

import java.util.List;

import kr.co.marueng.mtalk.vo.ChatRoomVO;

public interface IadminChatDAO {

	public List<ChatRoomVO> selectAllChatRoom();

}
