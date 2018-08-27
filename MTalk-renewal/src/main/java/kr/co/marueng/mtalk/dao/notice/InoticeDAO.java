package kr.co.marueng.mtalk.dao.notice;

import java.util.List;

import kr.co.marueng.mtalk.vo.NoticeVO;
import kr.co.marueng.mtalk.vo.PagingVO;

public interface InoticeDAO {

	/**
	 * 페이징 처리를 위한 공지사항 게시물의 수 select
	 * @param pagingVO
	 * @return
	 */
	public Integer selectNoticeCount(PagingVO<NoticeVO> pagingVO);

	/**
	 * 공지사항 게시물을 조회
	 * @param pagingVO
	 * @return
	 */
	public List<NoticeVO> selectNoticeList(PagingVO<NoticeVO> pagingVO);

	/**
	 * 공지사항 상세 조회
	 * @param noti_id
	 * @return
	 */
	public NoticeVO selectNotice(String noti_id);

	/**
	 * 공지사항 생성
	 * @param notice
	 * @return
	 */
	public int insertNotice(NoticeVO notice);

	/**
	 * 공지사항 삭제
	 * @param noti_id
	 * @return
	 */
	public int deleteNotice(String noti_id);

	/**
	 * 공지사항 수정
	 * @param notice
	 * @return
	 */
	public int updateNotice(NoticeVO notice);
	
	/**
	 * 공지사항 가장 최신글 1개 조회(채팅창에 최근공지사항글 띄워줌)
	 * @return NoticeVO
	 */
	public NoticeVO selectNoticeBiggerOne();
}
