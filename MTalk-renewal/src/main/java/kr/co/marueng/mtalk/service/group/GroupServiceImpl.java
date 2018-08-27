package kr.co.marueng.mtalk.service.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.dao.friend.IfriendDAO;
import kr.co.marueng.mtalk.dao.group.IgroupDAO;
import kr.co.marueng.mtalk.vo.FriendGroupVO;

@Service
public class GroupServiceImpl implements IgroupService{
	@Inject
	IgroupDAO groupDAO;
	@Inject
	IfriendDAO friendDAO;
	
	@Override
	public ServiceResult createGroup(FriendGroupVO group) {
		ServiceResult result = null;
		int rowCnt = groupDAO.insertGroup(group);
		if(rowCnt>0){
			result = ServiceResult.OK;
		}else{
			result = ServiceResult.FAILED;
		}
		return result;
	}

	@Override
	public ServiceResult editName(FriendGroupVO group) {
		ServiceResult result = null;
		int rowCnt = groupDAO.editName(group);
		if(rowCnt>0){
			result = ServiceResult.OK;
		}else{
			result = ServiceResult.FAILED;
		}
		return result;
	}

	@Override
	public ServiceResult deleteGroup(FriendGroupVO group) {
		ServiceResult result = null;
		int rowCnt1 = friendDAO.excludeGroup(group);
		int rowCnt2 = groupDAO.deleteGroup(group);
		
		return result;
	}

	@Override
	public Integer checkGroupDuplication(FriendGroupVO group) {
		return groupDAO.checkGroupDuplication(group);
	}

	@Override
	public Map<String, String> retrievegroupList(String mem_id) {
		List<FriendGroupVO> resultList = groupDAO.selectGroupList(mem_id);
		Map<String, String> result = new HashMap<>();
		for(int i = 0; i<resultList.size(); i++){
			result.put(resultList.get(i).getFriendGroup_id(), resultList.get(i).getFriendGroup_name());
		}
		return result;
	}
	
}
