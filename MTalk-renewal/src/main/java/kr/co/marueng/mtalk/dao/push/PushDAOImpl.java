package kr.co.marueng.mtalk.dao.push;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.PushVO;

/* 
 * 알림DAO
 * 조문경
 * 
 * */

@Repository("pushDAO")
public class PushDAOImpl implements IpushDAO {

	@Inject
	SqlSessionTemplate sqlSession;

	/* 알림저장 (친구 신청시 상대가 오프라인일 때) */
	@Override
	public void push_insert(PushVO vo) {
		// TODO Auto-generated method stub
		sqlSession.insert("push.push_input",vo);
	}

	/* 알림가져옴 (로그인했을 때 친구신청 여부를 알려주기위해) */
	@Override
	public PushVO push_get(String mem) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("push.push_get",mem);
	}

	/* 알림가져옴 (친구신청 여부를 확인했을 때) */
	@Override
	public int push_confirm(String mem) {
		// TODO Auto-generated method stub
		
		return sqlSession.update("push.push_confirm", mem);
	}

}
