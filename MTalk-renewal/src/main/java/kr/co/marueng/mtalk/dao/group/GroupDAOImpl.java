package kr.co.marueng.mtalk.dao.group;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.FriendGroupVO;

@Repository("groupDAO")
public class GroupDAOImpl implements IgroupDAO{
	@Inject
	SqlSessionTemplate sqlSession;

	@Override
	public List<FriendGroupVO> selectGroupList(String mem_id) {
		return sqlSession.selectList("group.selectGroupList", mem_id);
	}

	@Override
	public Integer insertGroup(FriendGroupVO group) {
		return sqlSession.insert("group.insertGroup", group);
	}

	@Override
	public Integer editName(FriendGroupVO group) {
		return sqlSession.update("group.editName", group);
	}

	@Override
	public Integer deleteGroup(FriendGroupVO group) {
		return sqlSession.delete("group.deleteGroup", group);
	}

	@Override
	public Integer checkGroupDuplication(FriendGroupVO group) {
		return sqlSession.selectOne("group.checkGroupDuplication", group);
	}
}
