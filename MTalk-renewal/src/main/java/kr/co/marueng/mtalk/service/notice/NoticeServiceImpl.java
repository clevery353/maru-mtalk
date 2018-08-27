package kr.co.marueng.mtalk.service.notice;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.ServiceResult;
import kr.co.marueng.mtalk.dao.notice.InoticeDAO;
import kr.co.marueng.mtalk.vo.NoticeVO;
import kr.co.marueng.mtalk.vo.PagingVO;

@Service
public class NoticeServiceImpl implements InoticeService{
	@Inject
	InoticeDAO noticeDAO;
	
	@Override
	public Integer retrieveNoticeCount(PagingVO<NoticeVO> pagingVO) {
		return noticeDAO.selectNoticeCount(pagingVO);
	}

	@Override
	public List<NoticeVO> retrieveNoticeList(PagingVO<NoticeVO> pagingVO) {
		return noticeDAO.selectNoticeList(pagingVO);
	}

	@Override
	public NoticeVO retrieveNotice(String noti_id) {
		return noticeDAO.selectNotice(noti_id);
	}

	@Override
	public ServiceResult createNotice(NoticeVO notice) {
		ServiceResult result = null;
		int rowCnt = noticeDAO.insertNotice(notice);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public ServiceResult deleteNotice(String noti_id) {
		ServiceResult result = null;
		int rowCnt = noticeDAO.deleteNotice(noti_id);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public ServiceResult modifyNotice(NoticeVO notice) {
		ServiceResult result = null;
		int rowCnt = noticeDAO.updateNotice(notice);
		if(rowCnt<1){
			result = ServiceResult.FAILED;
		}else{
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public NoticeVO selectNoticeBiggerOne() {
		return noticeDAO.selectNoticeBiggerOne();
	}

}
