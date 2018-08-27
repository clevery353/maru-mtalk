package kr.co.marueng.mtalk.dao.relations;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.RelationsVO;

@Repository("relationsDAO")
public class RelationsDAOImpl implements IrelationsDAO {
	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public List<RelationsVO> selectRelations(String mem_id) {
		return sqlSession.selectList("relations.selectRelations", mem_id);
	}

	@Override
	public Integer updateRelations(RelationsVO relations) {
		return sqlSession.update("relations.updateRelations", relations);
	}

	@Override
	public Integer rejectFriend(RelationsVO relations) {
		return sqlSession.delete("relations.rejectRelations", relations);
	}

	@Override
	public Integer addFriend(RelationsVO relations) {
		return sqlSession.insert("relations.addFriend",relations);
	}

	@Override
	public Integer checkAlreadySendRelations(RelationsVO relations) {
		return sqlSession.selectOne("relations.checkAlreadySend",relations);
	}

	@Override
	public Integer checkAlreadyReceiveRelations(RelationsVO relations) {
		return sqlSession.selectOne("relations.checkAlreadyReceive",relations);
	}
	// 0718 소은추가 모든회원의 친구신청관계 확인(회원탈퇴시 삭제해야함)
	@Override
	public List<RelationsVO> allMemRelations(RelationsVO relations) {
		List<RelationsVO> list = null;
		try{
			list = sqlSession.selectList("relations.allMemRelations", relations);			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list; 
	}
	// 0718 소은추가 회원탈퇴시 해당 관계에 상태를 D로 바꾸어 탈퇴한 회원임을 표시한다
	@Override
	public int outMemRelations(RelationsVO relations) {
		return sqlSession.update("relations.updateDelRelations", relations);
	}

}
