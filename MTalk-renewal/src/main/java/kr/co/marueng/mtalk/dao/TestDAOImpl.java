package kr.co.marueng.mtalk.dao;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.TestVO;

@Repository("testDAO")
public class TestDAOImpl implements TestDAO{

	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public List<TestVO> getTestValue(TestVO testVO) {
		return sqlSession.selectList("sample.getTestValue", testVO);
	}

}
