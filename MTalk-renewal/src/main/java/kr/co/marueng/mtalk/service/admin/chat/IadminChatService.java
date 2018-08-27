package kr.co.marueng.mtalk.service.admin.chat;

import java.util.List;

import kr.co.marueng.mtalk.vo.ChatRoomVO;

public interface IadminChatService {

	/**
	 * 페이지3 초기 채팅 리스트 조회
	 * @return
	 */
	public List<ChatRoomVO> retrieveAllChatRoom();

}
