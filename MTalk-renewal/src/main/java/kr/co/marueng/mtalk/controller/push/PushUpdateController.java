package kr.co.marueng.mtalk.controller.push;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.service.push.IpushService;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.PushVO;

/* 
 * 알림 업데이트
 * 조문경
 * 
 * */

@Controller
@RequestMapping("/push")
public class PushUpdateController {

	@Inject
	IpushService service;
	
	@RequestMapping(value = "/MypushConfirm", method = RequestMethod.POST)
	@ResponseBody
	public String push_confirm(PushVO push, HttpServletRequest req, HttpServletResponse resp, Errors error,
			HttpSession session) throws Exception{
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		int result = 0;
		
		String push_mem = push.getPush_mem();
		
		try{
			result = service.push_confirm(push_mem);
			
			if(result > 0)
			{
				map.put("result", "success");//이미지가 저장되었을 때
				strb.append(mapper.writeValueAsString(map));
			}
			else{
				map.put("result", "failure");//이미지가 저장되었을 때
				strb.append(mapper.writeValueAsString(map));
			}
		}catch(Exception e){
			map.put("result", "failure");//이미지가 저장되었을 때
			strb.append(mapper.writeValueAsString(map));
		}	
		return strb.toString();
		}
}
