package kr.co.marueng.mtalk.service.notice;

import java.util.List;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.vo.NoticeVO;
import kr.co.marueng.mtalk.vo.PagingVO;

public interface InoticeService {

	public Integer retrieveNoticeCount(PagingVO<NoticeVO> pagingVO);

	public List<NoticeVO> retrieveNoticeList(PagingVO<NoticeVO> pagingVO);

	public NoticeVO retrieveNotice(String noti_id);

	public ServiceResult createNotice(NoticeVO notice);

	public ServiceResult deleteNotice(String noti_id);

	public ServiceResult modifyNotice(NoticeVO notice);
	/**
	 * 공지사항 최신글 1개 추출(채팅창에 띄움)
	 * @return NoticeVO
	 */
	public NoticeVO selectNoticeBiggerOne();
}
