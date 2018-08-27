package kr.co.marueng.mtalk.dao.hit;

public interface IsaveHitAmountDAO {
	/**
	 * 금일 총 방문자 수 저장
	 * @param todayCount 
	 * @return
	 */
	public void saveAmount(int todayCount);
}
