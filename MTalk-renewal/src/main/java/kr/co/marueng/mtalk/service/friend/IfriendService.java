package kr.co.marueng.mtalk.service.friend;

import java.util.List;
import java.util.Map;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.RelationsVO;

public interface IfriendService {
	/**
	 * 친구목록조회
	 * @param mem_id
	 * @return
	 */
	public List<Object> retrieveFriendList(String mem_id);
	
	public ServiceResult createFriend(FriendVO friend);

	public ServiceResult excludeFriend(FriendVO friend);

	public ServiceResult deleteFriend(FriendVO friend);

	public ServiceResult addBookmark(FriendVO friend);

	public ServiceResult excludeBookmark(FriendVO friend);

	public ServiceResult moveGroup(FriendVO friend);

	public Integer checkAlreadyFriend(RelationsVO relations);
}
