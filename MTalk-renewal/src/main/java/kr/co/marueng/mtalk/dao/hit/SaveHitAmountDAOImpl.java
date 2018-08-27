package kr.co.marueng.mtalk.dao.hit;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SaveHitAmountDAOImpl implements IsaveHitAmountDAO{
	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public void saveAmount(int todayCount) {
		System.out.println("접속자수 저장");
		sqlSession.selectOne("hit.insertSaveAmount",todayCount);
		
	}

}
