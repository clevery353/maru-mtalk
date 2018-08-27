package kr.co.marueng.mtalk.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.TestDAO;
import kr.co.marueng.mtalk.vo.TestVO;

@Service
public class TestServiceImpl implements TestService{

	@Inject
    TestDAO testDAO;
    
    @Override
    public List<TestVO> getTestValue(TestVO testVO){
          return testDAO.getTestValue(testVO);
    }


}
