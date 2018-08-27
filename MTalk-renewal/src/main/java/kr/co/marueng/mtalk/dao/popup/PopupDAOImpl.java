package kr.co.marueng.mtalk.dao.popup;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PopupDAOImpl implements IpopupDAO{
	@Inject
	SqlSessionTemplate sqlSession;
}
