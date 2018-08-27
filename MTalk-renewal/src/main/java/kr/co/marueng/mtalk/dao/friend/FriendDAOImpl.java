package kr.co.marueng.mtalk.dao.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.vo.FriendGroupVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

@Repository("friendDAO")//서비스임플의 변수명과 동일
public class FriendDAOImpl implements IfriendDAO{
	@Inject
	SqlSessionTemplate sqlSession;

	//친구목록을 즐겨찾기 되어있는 친구들과 모두의 친구들을 동시에 조회
	@Override
	public Map<String, Object> selectFriendList(String mem_id) {
		Map<String, Object> resultMap = new HashMap<>();
		List<MemberVO> bookmarkFriendList = new ArrayList<>();
		List<MemberVO> friendList = new ArrayList<>();
		
		bookmarkFriendList = sqlSession.selectList("friend.selectBookmarkFriendList", mem_id);
		friendList = sqlSession.selectList("friend.selectFriendList", mem_id);
		
		resultMap.put("bookmarkFriendList", bookmarkFriendList);
		resultMap.put("friendList",friendList);
		return resultMap;
	}

	//친구 수락시 각 회원의 테이블에 친구리스트를 만들어줌
	@Override
	public ServiceResult createFriend(FriendVO friend) {
		ServiceResult result = null;
		Integer rowCnt = null;
		Integer rowCnt2 = null;
		
		rowCnt = sqlSession.insert("friend.insertFriendList1", friend);
		rowCnt2 = sqlSession.insert("friend.insertFriendList2", friend);
		if(rowCnt<1||rowCnt2<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	//친구를 그룹에서 제외(기본그룹으로 이동)
	@Override
	public Integer excludeFriend(FriendVO friend) {
		return sqlSession.update("friend.excludeFriend", friend);
	}

	
	//친구삭제
	@Override
	public Integer deleteFriend(FriendVO friend) {
		Integer result = 0;
		int rowCnt1 = sqlSession.delete("friend.deleteFriend1",friend);
		int rowCnt2 = sqlSession.delete("friend.deleteFriend2",friend);
		if(rowCnt1<1 || rowCnt2<1){
			result = 0;
		}else{
			result = 2;
		}
		return result;
	}

	//즐겨찾기 추가
	@Override
	public Integer addBookmark(FriendVO friend) {
		return sqlSession.update("friend.addBookmark", friend);
	}

	//즐겨찾기에서 제외
	@Override
	public Integer excludeBookmark(FriendVO friend) {
		return sqlSession.update("friend.excludeBookmark",friend);
	}

	//그룹 삭제시 친구 그룹을 기본으로 변경
	@Override
	public Integer excludeGroup(FriendGroupVO group) {
		return sqlSession.update("friend.excludeGroup",group);
	}

	//친구의 그룹을 변경
	@Override
	public Integer moveGroup(FriendVO friend) {
		return sqlSession.update("friend.moveGroup",friend);
	}
	
	//친구신청시 이미 친구인지 확인(relations부터 옴)
	@Override
	public Integer checkAlreadyFriend(RelationsVO relations) {
		return sqlSession.selectOne("friend.checkAlreadyFriend", relations);
	}
	// 친구여부 확인 0717 소은추가
	@Override
	public FriendVO isFriend(FriendVO friend) {
		List<FriendVO> list = sqlSession.selectList("friend.isFriend", friend);
		if(list.size() > 1){
			return list.get(0);
		}else{
			return null;
		}
	}
}
