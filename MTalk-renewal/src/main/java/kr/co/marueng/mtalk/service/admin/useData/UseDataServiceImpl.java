package kr.co.marueng.mtalk.service.admin.useData;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.admin.useData.IuseDataDAO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.UseDataVO;

@Service
public class UseDataServiceImpl implements IuseDataService {
	@Inject
	IuseDataDAO adminUseDataDAO;
	
	@Override
	public List<UseDataVO> retrieveAllData() {
		return adminUseDataDAO.selectAllData();
	}

	@Override
	public List<MemberVO> retriveData(Map<String, String> condition) {
		return adminUseDataDAO.selectData(condition);
	}

}
