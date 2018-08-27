package kr.co.marueng.mtalk.service.admin.useData;

import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.UseDataVO;

public interface IuseDataService {

	/**
	 * 지워도 되는 메서드
	 * @return
	 */
	public List<UseDataVO> retrieveAllData();

	/**
	 * 유저별 데이터 사용량 조회
	 * @param condition
	 * @return
	 */
	public List<MemberVO> retriveData(Map<String, String> condition);

}
