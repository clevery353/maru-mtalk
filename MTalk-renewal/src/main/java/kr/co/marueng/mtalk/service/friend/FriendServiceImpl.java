package kr.co.marueng.mtalk.service.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.dao.friend.IfriendDAO;
import kr.co.marueng.mtalk.dao.group.IgroupDAO;
import kr.co.marueng.mtalk.dao.member.ImemberDAO;
import kr.co.marueng.mtalk.dao.relations.IrelationsDAO;
import kr.co.marueng.mtalk.vo.FriendGroupVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

/**
 * @author 곽현호
 * @DATE 2018. 7. 6.
 */
@Service
public class FriendServiceImpl implements IfriendService{
	@Inject
	IfriendDAO friendDAO;
	@Inject
	IgroupDAO groupDAO;
	@Inject
	ImemberDAO memberDAO;
	@Inject
	IrelationsDAO relationsDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> retrieveFriendList(String mem_id) {
//		Map<String, Object> resultMap = new HashMap<>();
		List<Object> resultList = new ArrayList<>();
		Map<String, Object> friendListMap = new HashMap<>();
		List<FriendVO> friendList = new ArrayList<>();
		List<FriendVO> bookmarkFriendList = new ArrayList<>();
		List<FriendGroupVO> groupList = new ArrayList<>();
		
		friendListMap = friendDAO.selectFriendList(mem_id);
		groupList = groupDAO.selectGroupList(mem_id);
		
		friendList = (List<FriendVO>) friendListMap.get("friendList");
		bookmarkFriendList = (List<FriendVO>) friendListMap.get("bookmarkFriendList");
		
//		for(int i = 0; i<friendList.size(); i++){
//			friendList.get(i).setFri_friendmem_name(memberDAO.selectMemberName(friendList.get(i).getFri_friendmem())); 
//		}
//		for(int i = 0; i<bookmarkFriendList.size(); i++){
//			bookmarkFriendList.get(i).setFri_friendmem_name(memberDAO.selectMemberName(friendList.get(i).getFri_friendmem())); 
//		}
		
		resultList.add(friendList);
		resultList.add(bookmarkFriendList);
		resultList.add(groupList);
//		resultMap.put("friendList", friendList);
//		resultMap.put("bookmarkFriendList", bookmarkFriendList);
//		resultMap.put("groupList", groupList);
		return resultList;
	}

	@Override
	public ServiceResult createFriend(FriendVO friend) {
		RelationsVO relations = new RelationsVO();
		relations.setRel_firstMem(friend.getFri_firstmem());
		relations.setRel_secondMem(friend.getFri_friendmem());
		ServiceResult result = null;
		ServiceResult result1 = friendDAO.createFriend(friend);
		ServiceResult result2 = null;
		int rowCnt = relationsDAO.rejectFriend(relations);
		
		if(rowCnt<1){
			result2 = ServiceResult.FAILED;
		}else{
			result2 = ServiceResult.OK;
		}
		if(result1 == ServiceResult.OK&&result2 == ServiceResult.OK){
			result = ServiceResult.OK;
		}
		return result;
	}
	
	//친구 그룹에서 제외(기본그룹으로 이동)
	@Override
	public ServiceResult excludeFriend(FriendVO friend) {
		ServiceResult result = null;
		int rowCnt = friendDAO.excludeFriend(friend);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}
	
	//친구 삭제
	@Override
	public ServiceResult deleteFriend(FriendVO friend) {
		ServiceResult result = null;
		
		int rowCnt = friendDAO.deleteFriend(friend);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public ServiceResult addBookmark(FriendVO friend) {
		ServiceResult result = null;
		int rowCnt = friendDAO.addBookmark(friend);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public ServiceResult excludeBookmark(FriendVO friend) {
		ServiceResult result = null;
		int rowCnt = friendDAO.excludeBookmark(friend);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public ServiceResult moveGroup(FriendVO friend) {
		ServiceResult result = null;
		int rowCnt = friendDAO.moveGroup(friend);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public Integer checkAlreadyFriend(RelationsVO relations) {
		return friendDAO.checkAlreadyFriend(relations);
	}

}
