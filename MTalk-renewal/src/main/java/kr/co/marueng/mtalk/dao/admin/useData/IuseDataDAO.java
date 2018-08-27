package kr.co.marueng.mtalk.dao.admin.useData;

import java.util.List;
import java.util.Map;

import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.UseDataVO;

public interface IuseDataDAO {

	public List<UseDataVO> selectAllData();

	public List<MemberVO> selectData(Map<String, String> condition);
	
	/** 소은추가 0721
	 * 사용자가 사용한 데이터 입력
	 * @param usedataVO
	 * @return 성공 1, 실패 0
	 */
	int insertUsedata(UseDataVO usedataVO);

}
