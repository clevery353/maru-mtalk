package kr.co.marueng.mtalk.service.push;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.push.IpushDAO;

/* 
 * 알림 Service
 * 조문경
 * 
 * */

@Service
public class PushServiceImple implements IpushService {
	
	@Inject
	IpushDAO pushDAO;

	/* 사용자가 알림을 읽었을 때 발생 */
	@Override
	public int push_confirm(String push_mem) {
		// TODO Auto-generated method stub
		return pushDAO.push_confirm(push_mem);
	}
	
}
