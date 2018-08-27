package kr.co.marueng.mtalk.dao.admin.member;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.MemberVO;

@Repository("adminMemberDAO")
public class AdminMemberDAOImpl implements IadminMemberDAO {
	@Inject
	SqlSessionTemplate sqlSession;
	
	@Override
	public List<MemberVO> retrieveJoinMember() {
		return sqlSession.selectList("member.selectJoinMemberList");
	}

	@Override
	public List<MemberVO> retrieveMember() {
		return sqlSession.selectList("member.selectMemberList");
	}

	@Override
	public Integer approveMember(String mem_id) {
		return sqlSession.update("member.approveMember", mem_id);
	}

	@Override
	public int rejectMember(String mem_id) {
		return sqlSession.update("member.rejectMember", mem_id);
	}

	@Override
	public int banMember(String mem_id) {
		return sqlSession.update("member.banMember",mem_id);
	}

}
