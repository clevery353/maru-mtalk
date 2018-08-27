package kr.co.marueng.mtalk.dao.admin.hit;

import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.HitVO;

public interface IhitDAO {

	/**
	 * 전체 hit 조회
	 * @return
	 */
	public List<HitVO> selectAllHit();

	/**
	 * 조건에 의한 hit 조회
	 * @param condition
	 * @return
	 */
	public List<HitVO> selectHit(Map<String, String> condition);


}
