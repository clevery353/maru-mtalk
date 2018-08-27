package kr.co.marueng.mtalk.dao;

import java.util.List;

import kr.co.marueng.mtalk.vo.TestVO;

public interface TestDAO {
	public List<TestVO> getTestValue(TestVO testVO);
}
