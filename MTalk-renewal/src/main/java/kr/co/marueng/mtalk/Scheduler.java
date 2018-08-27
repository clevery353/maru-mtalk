package kr.co.marueng.mtalk;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

	public static int member_count = 0;
	
	private Date today = new Date();
	private SimpleDateFormat sdf_today; //날짜 포맷을 위해 사용
	
	public static String date;
	
	public Scheduler()
	{
		sdf_today = new SimpleDateFormat("yyyyMMdd");
		date = sdf_today.format(today);
	}
	
	public void ReceivedSession_up()
	{
		member_count++;
		System.out.println("date : "+date+", hit : "+member_count);
	}
	
	public void ReceivedSession_down()
	{
		member_count--;
		System.out.println("date : "+date+", hit : "+member_count);
	}
	
	@Scheduled(cron = "0 59 23 * * *")
	public void DB_Access_Hit()
	{
		System.out.println("DB에 전송할 데이터들은 date : "+date+", 조회수 : "+member_count);
	}
}
