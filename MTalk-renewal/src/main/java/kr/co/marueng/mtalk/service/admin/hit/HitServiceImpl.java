package kr.co.marueng.mtalk.service.admin.hit;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.admin.hit.IhitDAO;
import kr.co.marueng.mtalk.vo.HitVO;

@Service
public class HitServiceImpl implements IhitService {
	@Inject
	IhitDAO hitDAO;
	
	@Override
	public List<HitVO> retrieveAllHit() {
		return hitDAO.selectAllHit();
	}

	@Override
	public List<HitVO> retrieveHit(Map<String, String> condition) {
		return hitDAO.selectHit(condition);
	}


}
