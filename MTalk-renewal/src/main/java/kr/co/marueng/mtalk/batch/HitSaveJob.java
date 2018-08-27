package kr.co.marueng.mtalk.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import kr.co.marueng.mtalk.dao.hit.IsaveHitAmountDAO;
import kr.co.marueng.mtalk.dao.hit.SaveHitAmountDAOImpl;
import kr.co.marueng.mtalk.listener.CustomSessionListener;

public class HitSaveJob  {
	IsaveHitAmountDAO dao; 
	
	@Autowired
	public void setSaveHitAmountDAO(SaveHitAmountDAOImpl dao){
		this.dao = dao;
	}
	
	Logger logger = LoggerFactory.getLogger(getClass());
	@Scheduled(cron="0 59 23 ? * *")
	public void saveHitAmountJob(){
		dao.saveAmount(CustomSessionListener.allUserCount);
		CustomSessionListener.allUserCount = 0;
	}
}
