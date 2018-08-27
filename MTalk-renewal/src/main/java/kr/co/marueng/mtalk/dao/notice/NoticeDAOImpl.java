package kr.co.marueng.mtalk.dao.notice;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.co.marueng.mtalk.vo.NoticeVO;
import kr.co.marueng.mtalk.vo.PagingVO;

@Repository("noticeDAO")
public class NoticeDAOImpl implements InoticeDAO{
	@Inject
	SqlSessionTemplate sqlSession;

	@Override
	public Integer selectNoticeCount(PagingVO<NoticeVO> pagingVO) {
		return sqlSession.selectOne("notice.selectNoticeCount", pagingVO);
	}

	@Override
	public List<NoticeVO> selectNoticeList(PagingVO<NoticeVO> pagingVO) {
		return sqlSession.selectList("notice.selectNoticeList", pagingVO);
	}

	@Override
	public NoticeVO selectNotice(String noti_id) {
		return sqlSession.selectOne("notice.selectNotice", noti_id);
	}

	@Override
	public int insertNotice(NoticeVO notice) {
		return sqlSession.insert("notice.insertNotice", notice);
	}

	@Override
	public int deleteNotice(String noti_id) {
		return sqlSession.delete("notice.deleteNotice", noti_id);
	}

	@Override
	public int updateNotice(NoticeVO notice) {
		return sqlSession.update("notice.updateNotice", notice);
	}

	@Override
	public NoticeVO selectNoticeBiggerOne() {
		return sqlSession.selectOne("notice.selectNoticeBiggerOne");
	}
}
