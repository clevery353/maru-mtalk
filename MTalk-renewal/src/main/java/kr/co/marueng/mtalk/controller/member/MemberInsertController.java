package kr.co.marueng.mtalk.controller.member;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 조문경
 * @DATE 2018. 7. 11.
 */

@Controller
@RequestMapping("/member")
public class MemberInsertController {
	@Inject
	ImemberService service;
	
	private static int mail_submit_number;
	
	@RequestMapping(value = "/recheckpw", method = RequestMethod.POST)
	@ResponseBody 
	public String recheckpw(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception 
	{
		String mem_pw = member.getMem_pw();
		StringBuffer stbr = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		
		if(regPass(mem_pw)){
			map.put("result", mem_pw);
		}else{
			map.put("result", "N");
		}
		stbr.append(mapper.writeValueAsString(map));
		
		return stbr.toString();
	}
	
	@RequestMapping(value = "/EnrollMember", method = RequestMethod.POST) // 2018-07-21 문경 수정
	@ResponseBody 
	public String EnrollMember(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception 
	{
		String mem_id = member.getMem_id();
		String mem_pw = member.getMem_pw();
		String mem_name = member.getMem_name();
		String mem_mail = member.getMem_mail();
		String mem_propic = member.getMem_propic();
		String mem_protext = member.getMem_protext();
		String mem_type = "회원";
		String mem_status = "W";
		
		System.out.println("pro_pic : " + mem_propic);
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		MemberVO vo = new MemberVO();
		vo.setMem_id(mem_id);
		vo.setMem_pw(mem_pw);
		vo.setMem_name(mem_name);
		vo.setMem_mail(mem_mail);
		vo.setMem_propic(mem_propic);
		vo.setMem_protext(mem_protext);
		vo.setMem_type(mem_type);
		vo.setMem_status(mem_status);
		try{
			service.memberEnroll(vo);
				map.put("result","success");
				strb.append(mapper.writeValueAsString(map));
		}catch(Exception e){
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}
		
		return strb.toString();
	}
	
	@RequestMapping(value = "/EnrollUpdateImage", method = RequestMethod.POST) // 2018-07-21 문경 수정
	@ResponseBody //회원가입시 폴더에 이미지만 올려두는 과정.
	public String EnrollUpdateImage(MultipartHttpServletRequest req,
		    HttpServletResponse res, HttpSession session, HttpServletRequest request) throws Exception
	{
		
		 Iterator<String> itr = req.getFileNames();
		 MultipartFile mpf = req.getFile(itr.next()); //ajax로부터 파일을 받아오기 위해 사

		 String originFileName = mpf.getOriginalFilename(); // 파일의 이름을 저장
		 System.out.println("filename : "+originFileName);
		 
		 String root_path = request.getSession().getServletContext().getRealPath("/");
		 System.out.println("rootpath : "+root_path);
		 String attach_path = "WEB-INF/views/resources/ProfileImageUpload/"; // 이미지 업로드 디렉토리입니다.
		 
		File f = new File(root_path + attach_path + originFileName);
		mpf.transferTo(f);
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
	
		 try{
			map.put("result", originFileName);//이미지가 저장되었을 때 프론트로 반환
			strb.append(mapper.writeValueAsString(map));
			
		 }catch(Exception e){
			 map.put("filename", "failure");//이미지가 저장되었을 때
			strb.append(mapper.writeValueAsString(map));
		 }
		 
		 return strb.toString();
	}
//	
//	@RequestMapping(value = "/mailNumberConfirm", produces="application/json; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody 
//	public String mailNumberConfirm(HttpServletResponse resp, @RequestParam("mem_number")String mem_number)throws Exception
//	{
//		StringBuffer strb = new StringBuffer();
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, String> map = new HashMap<>();
//		
//		if(mem_number.equals(String.valueOf(mail_submit_number))){
//			System.out.println("memnumber : "+String.valueOf(mail_submit_number));
//			map.put("result","success");
//			strb.append(mapper.writeValueAsString(map));
//		}else{
//			map.put("result","failure");
//			strb.append(mapper.writeValueAsString(map));
//		}
//		
//		return strb.toString();
//	}
//	
	@RequestMapping(value = "/mailSubmit", produces="application/json; charset=UTF-8", method = RequestMethod.POST) // 2018-07-21 문경 수정
	@ResponseBody 
	public String mailSubmit(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception //이메일 인증을 위해 입력된 메일로 인증번호를 보냄.
	{
		String mem_mail = member.getMem_mail();
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		int number = randomNum(6);
		
		
		try{
			String host = "smtp.naver.com";
			final String user = "mun3189@naver.com";
			final String password = "chomk6253";

			String to = mem_mail;
			
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
				message.setSubject("MTALK 이메일 인증입니다. ");

				// Text
				message.setText("인증번호 입니다."+": ["+number+"]");

				// send the message
				Transport.send(message);
				
				map.put("result",String.valueOf(number));
				strb.append(mapper.writeValueAsString(map));

			} catch (MessagingException e) {
				e.printStackTrace();
				map.put("result","failure");
				strb.append(mapper.writeValueAsString(map));
			}
			
		}catch(Exception e){
			e.printStackTrace();
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}
		return strb.toString();
	}
	
	//회원가입 - 비밀번호 유효성 검사
	@RequestMapping(value = "/PwCheck", produces="application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody 
	public String memberPwCheck(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session)throws Exception
	{
		
		String mem_pw = member.getMem_pw();
		
		StringBuffer stbr = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		
		if(regPass(mem_pw)){
			map.put("result", mem_pw);
		}else{
			map.put("result", "N");
		}
		
		try{
			stbr.append(mapper.writeValueAsString(map));
		}catch(JsonProcessingException e){}
		
		return stbr.toString();
	}
	
	// 회원가입 - 아이디 중복검사
	@RequestMapping(value = "/IdCheck", produces="application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody 
	public String memberIdCheck(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session2)throws Exception //회원가입 시, 아이디 중복검사
	{
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		String mem_id = member.getMem_id();
		boolean regid = regId(mem_id);
		int result = 0;
		
		try{
			result = service.memberIdCheck(mem_id);
			
			if(result == 0 && regid){
				map.put("result","success");
				strb.append(mapper.writeValueAsString(map));
			}
			else{
				map.put("result","failure");
				strb.append(mapper.writeValueAsString(map));
			}
		}catch(Exception e){
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
	
	//핸드폰번호 정규식
		public static boolean regPhone(String phone){
	        if (phone==null) return false;
	        boolean b = Pattern.matches("^\\d{3}-\\d{3,4}-\\d{4}$",phone.trim());
	        return b;
		}
		//비밀번호 정규식
		public static boolean regPass(String pass){
	        if (pass==null) return false;
	        boolean b = Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$",pass.trim());
	        return b;
		}
		//아이디 정규식
		public static boolean regId(String id){
	        if (id==null) return false;
	        boolean b = Pattern.matches("^[a-zA-Z]+[a-zA-Z0-9].{3,8}$",id.trim());
	        return b;
		}
}
