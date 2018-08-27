package kr.co.marueng.mtalk.dao.admin.useData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.UseDataVO;

@Repository("adminUseDataDAO")
public class UseDataDAOImpl implements IuseDataDAO {
	@Inject
	SqlSessionTemplate sqlSession;

	@Override
	public List<UseDataVO> selectAllData() {
		return sqlSession.selectList("useData.selectAllData");
	}
//2018-07-25 현호 수정
	@Override
	public List<MemberVO> selectData(Map<String, String> condition) {
		List<MemberVO> result = new ArrayList<>();
		String dataYear = condition.get("dataYear");
		String dataMonth = condition.get("dataMonth");
		String dataMember = condition.get("dataMember");
		System.err.println("useDataDAOImpl 들어옴");
		System.err.println(dataMember+":daoImple");
		if(StringUtils.equals(dataMember,"")&&StringUtils.equals(dataYear, "allYear")&& StringUtils.equals(dataMonth, "allMonth")){
//			전체 데이터 조회
			result = sqlSession.selectList("useData.selectData");
		}else if(!StringUtils.equals(dataMember,"")&&StringUtils.equals(dataYear, "allYear") && StringUtils.equals(dataMonth, "allMonth")){
//			멤버만 선택한 데이터 조회
			System.err.println("유저만 데이터 들어옴"+dataMember);
			result = sqlSession.selectList("useData.selectMemberData",dataMember);
			System.err.println(result.toString());
		}else if(!StringUtils.equals(dataMember,"")&&!StringUtils.equals(dataYear, "allYear") && StringUtils.equals(dataMonth, "allMonth")){
//			멤버와 년도를 선택한 데이터 조회
			result = sqlSession.selectList("useData.selectMemberYearData", condition);
		}else if(StringUtils.equals(dataMember,"")&&!StringUtils.equals(dataYear, "allYear")&& StringUtils.equals(dataMonth, "allMonth")){
//			년도만 선택한 조회
			result = sqlSession.selectList("useData.selectYearData", condition);
		}else if(StringUtils.equals(dataMember,"")&&!StringUtils.equals(dataYear, "allYear")&& !StringUtils.equals(dataMonth, "allMonth")){
//			년도와 월을 선택한 데이터 조회
			result = sqlSession.selectList("useData.selectMonthData", condition);
		}else{
//			모두 선택한 데이터 조회
			result = sqlSession.selectList("useData.selectMemberMonthData" , condition);
		}
		return result;
	}
	// 소은추가 0721
		@Override
		public int insertUsedata(UseDataVO usedataVO) {
			return sqlSession.insert("useData.insertUsedata", usedataVO);
		}
}
