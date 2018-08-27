package kr.co.marueng.mtalk.dao.push;

import kr.co.marueng.mtalk.vo.PushVO;

/* 
 * 알림DAO
 * 조문경
 * 
 * */

public interface IpushDAO {
	public void push_insert(PushVO vo);
	public PushVO push_get(String mem);
	public int push_confirm(String mem);
}
