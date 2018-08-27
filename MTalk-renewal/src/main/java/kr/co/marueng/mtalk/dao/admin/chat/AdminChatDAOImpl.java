package kr.co.marueng.mtalk.dao.admin.chat;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.ChatRoomVO;

@Repository("adminChatDAO")
public class AdminChatDAOImpl implements IadminChatDAO {
	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public List<ChatRoomVO> selectAllChatRoom() {
		return sqlSession.selectList("chatroom.selectAllChatRoom");
	}

}
