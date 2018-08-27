package kr.co.marueng.mtalk.service.admin.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.marueng.mtalk.dao.admin.member.IadminMemberDAO;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 곽현호
 * @DATE 2018. 7. 19.
 */
@Service
public class AdminMemberServiceImpl implements IadminMemberService {
	@Inject
	IadminMemberDAO adminMemberDAO;

	@Override
	public List<MemberVO> retrieveJoinMember() {
		return adminMemberDAO.retrieveJoinMember();
	}

	@Override
	public List<MemberVO> retrieveMember() {
		return adminMemberDAO.retrieveMember();
	}

	@Override
	public Map<String, Object> approveMember(String mem_id) {
		Map<String, Object> resultMap = new HashMap<>();

		List<MemberVO> joinMemberList = new ArrayList<>();
		String message = null;
		int rowCnt = adminMemberDAO.approveMember(mem_id);
		if (rowCnt > 0) {
			message = "성공";
		} else {
			message = "실패";
		}
		joinMemberList = adminMemberDAO.retrieveJoinMember();
		resultMap.put("message", message);
		resultMap.put("joinMemberList", joinMemberList);

		return resultMap;
	}

	@Override
	public Map<String, Object> rejectMember(String mem_id) {
		Map<String, Object> resultMap = new HashMap<>();

		List<MemberVO> joinMemberList = new ArrayList<>();
		String message = null;
		int rowCnt = adminMemberDAO.rejectMember(mem_id);
		if (rowCnt > 0) {
			message = "성공";
		} else {
			message = "실패";
		}
		joinMemberList = adminMemberDAO.retrieveJoinMember();
		resultMap.put("message", message);
		resultMap.put("joinMemberList", joinMemberList);

		return resultMap;
	}

	@Override
	public Map<String, Object> banMember(String mem_id) {
		Map<String, Object> resultMap = new HashMap<>();

		List<MemberVO> memberList = new ArrayList<>();
		String message = null;
		int rowCnt = adminMemberDAO.banMember(mem_id);
		if (rowCnt > 0) {
			message = "성공";
		} else {
			message = "실패";
		}
		memberList = adminMemberDAO.retrieveMember();
		resultMap.put("message", message);
		resultMap.put("memberList", memberList);

		return resultMap;
	}

}
