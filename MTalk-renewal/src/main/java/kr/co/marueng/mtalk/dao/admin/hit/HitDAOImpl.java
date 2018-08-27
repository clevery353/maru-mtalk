package kr.co.marueng.mtalk.dao.admin.hit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.HitVO;

@Repository("hitDAO")
public class HitDAOImpl implements IhitDAO {
	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public List<HitVO> selectAllHit() {
		return sqlSession.selectList("hit.selectAllHit");
	}

	@Override
	public List<HitVO> selectHit(Map<String, String> condition) {
		List<HitVO> result = new ArrayList<>();
		String hitYear = condition.get("hitYear");
		String hitMonth = condition.get("hitMonth");
		if(StringUtils.equals(hitYear, "allYear")&& StringUtils.equals(hitMonth, "allMonth")){
			System.err.println("첫번째 조건 들어왔다");
			System.out.println(hitYear);
			System.out.println(hitMonth);
			result = sqlSession.selectList("hit.selectAllHit");
			System.err.println(result.toString());
		}else if(!StringUtils.equals(hitYear, "allYear") && StringUtils.equals(hitMonth, "allMonth")){
			System.err.println("두번째 조건 들어왔다");
			System.out.println(hitYear);
			System.out.println(hitMonth);
			result = sqlSession.selectList("hit.selectYearHit", condition);
			System.err.println(result.toString());
		}else{
			System.err.println("세번째 조건 들어왔다");
			System.out.println(hitYear);
			System.out.println(hitMonth);
			result = sqlSession.selectList("hit.selectMonthHit" , condition);
			System.err.println(result.toString());
		}
		return result;
	}


}
