package kr.co.marueng.mtalk.service.admin.hit;

import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.HitVO;

public interface IhitService {

	public List<HitVO> retrieveAllHit();

	public List<HitVO> retrieveHit(Map<String, String> condition);


}
