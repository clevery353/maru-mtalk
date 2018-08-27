package kr.co.marueng.mtalk.controller.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 조문경
 * @DATE 2018. 7. 11.
 */

@Controller
@RequestMapping("/member")
public class MemberRetrieveController {
	@Inject
	ImemberService service;
	
	public static String resultNumber;
	
	@RequestMapping(value = "/PushGetMemInfo", produces="application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public MemberVO PushGetInfo(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception
	{
		String mem_id = member.getMem_id();
		MemberVO meminfo = null;
		try{
			meminfo = service.memberLogin(mem_id);
			System.out.println("mem_name = "+meminfo.getMem_name());
			System.out.println("mem_img = "+meminfo.getMem_propic());
		}catch(Exception e){}

		return meminfo;
	}
	@RequestMapping(value = "/NumberConfirm", produces="application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody // 인증번호 일치하는지 검사
	public String NumberConfirm(HttpServletResponse resp, @RequestParam("numberinput")String numberinput)throws Exception 
	{
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		System.out.println("저장된 인증번호 : "+resultNumber);
		System.out.println("입력받은 인증번호 : "+numberinput);
		
		if(resultNumber.equals(numberinput)){
			map.put("result","success");
			strb.append(mapper.writeValueAsString(map));
			System.out.println("인증번호 일치해!");
		}else{
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}
		return strb.toString();
	}
	// 아이디 찾기
	@RequestMapping(value = "/IdSearch", produces="application/json; charset=UTF-8") // 2018-07-21 문경 수정
	@ResponseBody // 멤버 ID 찾기 
	public String memberIdSearch(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception
	{
		String mail = member.getMem_mail(); 
		String name = member.getMem_name();
		MemberVO mem; 
		String memberId = "";
		String status = "";
		boolean result = false;
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		hashmap.put("mem_name", name); 
		hashmap.put("mem_mail", mail); 
		
		try{
			mem = service.memberIdSearch(hashmap);
			memberId = mem.getMem_id();
			status = mem.getMem_status();
			
			System.out.println("ID : "+memberId);
			
			result = true;
		}catch(Exception e){ 
			e.printStackTrace();
			result = false;
		}
		
		if(result)
		{
			if(status.equals("Y")||status.equals("W"))
			{
				String host = "smtp.naver.com";
				final String user = "mun3189@naver.com";
				final String password = "chomk6253";

				String to = mail;
				
				// Get the session object
				Properties props = new Properties();
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.ssl.enable", "true");
				props.put("mail.smtp.port", "465");
				props.put("mail.smtp.ssl.trust", "smtp.naver.com");
				
				Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, password);
					}
				});

				// Compose the message
				try {
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(user));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

					// Subject
					message.setSubject("MTALK ID입니다. ");

					// Text
					message.setText("귀하의 아이디 입니다."+": ["+memberId+"]");

					// send the message
					Transport.send(message);
					
					map.put("result","success");
					strb.append(mapper.writeValueAsString(map));

				} catch (MessagingException e) {
					e.printStackTrace();
					map.put("result","failure");
					strb.append(mapper.writeValueAsString(map));
				}
			}else
			{
				map.put("result","failure_delete");
				strb.append(mapper.writeValueAsString(map));
			}
		}else{
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}
		
		return strb.toString();
	}
	
	//비밀번호찾기위해 아이디와 이메일 인증 과정
	@RequestMapping(value = "/PwSearch",  produces="application/json; charset=UTF-8") // 2018-07-21 문경 수정
	@ResponseBody // 멤버 비밀번호 새로 생성
	public String memberPwSearch(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception
	{
		String result_pw = ""; 
		String result_status = "";
		Boolean search_pw_result = false; 
		
		String id = member.getMem_id(); //나중에 폼에서 입력받아서 가져와야됨
		String mail = member.getMem_mail(); //나중에 폼에서 입력받아서 가져와야됨
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		System.out.println("id : "+id);
		System.out.println("mail : "+mail);
		
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		hashmap.put("mem_id", id); 
		hashmap.put("mem_mail", mail); 
		
		resultNumber = String.valueOf(randomNum(6));
		
		System.out.println("memberInfoUpdateController");
		
		MemberVO mem;
		
		try{
			mem = service.memberPwSearch(hashmap);
			result_pw = mem.getMem_pw();
			result_status = mem.getMem_status();
			System.out.println("result_pw : " + result_pw);
			search_pw_result = true;
		}catch(Exception e){
			e.printStackTrace();
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}		
		
		if(search_pw_result && result_pw != null && (result_status.equals("Y")||result_status.equals("W"))) // 만약 id와 mail이 가입내역에 존재한다면 메일로 인증번호를 발송
		{
			String host = "smtp.naver.com";
			final String user = "mun3189@naver.com";
			final String password = "chomk6253";

			String to = mail;

			// Get the session object
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.ssl.trust", "smtp.naver.com");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			});

			// Compose the message
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(user));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

				// Subject
				message.setSubject("MTALK 인증번호 입니다. ");

				// Text
				message.setText("새로운 비밀번호 생성을 위해 6자리 수를 인증확인에 넣어주세요"+": ["+resultNumber+"]");

				// send the message
				Transport.send(message);
				
				map.put("result","success");
				strb.append(mapper.writeValueAsString(map));

			} catch (MessagingException e) {
				e.printStackTrace();
				map.put("result","failure");
				strb.append(mapper.writeValueAsString(map));
			}
		}
		else{
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}
		return strb.toString();
	}
	
	public static int randomNum(int length) { //인증번호 만드는 함수
		 
	    String numStr = "1";
	    String plusNumStr = "1";
	 
	    for (int i = 0; i < length; i++) {
	        numStr += "0";
	 
	        if (i != length - 1) {
	            plusNumStr += "0";
	        }
	    }
	 
	    Random random = new Random();
	    int result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);
	 
	    if (result > Integer.parseInt(numStr)) {
	        result = result - Integer.parseInt(plusNumStr);
	    }
	 
	    return result;
	}
}
