package kr.co.marueng.mtalk.service.admin.chat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.admin.chat.IadminChatDAO;
import kr.co.marueng.mtalk.vo.ChatRoomVO;

@Service
public class AdminChatServiceImpl implements IadminChatService {
	@Inject
	IadminChatDAO adminChatDAO;

	@Override
	public List<ChatRoomVO> retrieveAllChatRoom() {
		List<ChatRoomVO> originalList = adminChatDAO.selectAllChatRoom();
		List<ChatRoomVO> fixedList = new ArrayList<>();
		ChatRoomVO chatRoomVO = null;
		for (int i = 0; i < originalList.size(); i++) {
			if (i == 0) {
				ChatRoomVO inChatRoomVO = new ChatRoomVO();
				List<String> chatMemList = new ArrayList<>();
				inChatRoomVO.setChatroom_id(originalList.get(i).getChatroom_id());
				inChatRoomVO.setChatroom_name(originalList.get(i).getChatroom_name());
				inChatRoomVO.setMem_id(originalList.get(i).getMem_id());
				inChatRoomVO.setLast_date(originalList.get(i).getLast_date());
				chatMemList.add(originalList.get(i).getChatroom_mem());
				inChatRoomVO.setChatMemList(chatMemList);
				chatRoomVO = inChatRoomVO;
			} else if (StringUtils.equals(originalList.get(i).getChatroom_id().toString(), originalList.get(i - 1).getChatroom_id().toString())) {
				System.err.println(i+"번째VO의 chatroomid"+originalList.get(i).getChatroom_id().toString());
				System.err.println((i-1)+"번째VO의 chatroomid"+originalList.get(i-1).getChatroom_id().toString());
				System.err.println("같다는 조건으로 들어왔다");
				chatRoomVO.getChatMemList().add(originalList.get(i).getChatroom_mem());
			} else if (originalList.get(i).getChatroom_id().toString() != originalList.get(i - 1).getChatroom_id().toString()) {
				fixedList.add(chatRoomVO);
				ChatRoomVO inChatRoomVO = new ChatRoomVO();
				List<String> chatMemList = new ArrayList<>();
				inChatRoomVO.setChatroom_id(originalList.get(i).getChatroom_id());
				inChatRoomVO.setChatroom_name(originalList.get(i).getChatroom_name());
				inChatRoomVO.setMem_id(originalList.get(i).getMem_id());
				inChatRoomVO.setLast_date(originalList.get(i).getLast_date());
				chatMemList.add(originalList.get(i).getChatroom_mem());
				inChatRoomVO.setChatMemList(chatMemList);
				chatRoomVO = inChatRoomVO;
				System.err.println(i+"번째VO의 chatroomid"+originalList.get(i).getChatroom_id().toString());
				System.err.println((i-1)+"번째VO의 chatroomid"+originalList.get(i-1).getChatroom_id().toString());
			}
			if (i == originalList.size() - 1) {
				fixedList.add(chatRoomVO);
			}
		}
		return fixedList;

	}
}