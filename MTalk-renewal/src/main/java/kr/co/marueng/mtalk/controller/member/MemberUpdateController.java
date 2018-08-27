package kr.co.marueng.mtalk.controller.member;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.marueng.mtalk.dao.chat.IChatDAO;
import kr.co.marueng.mtalk.service.member.ImemberService;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.MemberVO;

/**
 * @author 조문경
 * @DATE 2018. 7. 11.
 */

@Controller
@RequestMapping("/member")
public class MemberUpdateController {

	public static boolean email_confirm_bool = false;
	
	@Inject
	ImemberService service;
	
	@Inject
	IChatDAO chatDAO;
	
	@RequestMapping(value = "/MyInfoPwConfirm", method = RequestMethod.POST)
	@ResponseBody
	public String MyInfoPwConfirm(MemberVO member, HttpServletRequest req, HttpServletResponse resp, Errors error,
			HttpSession session) throws Exception
	{
		String mem_id = (String)session.getAttribute("mem_id");
		String mem_pw = member.getMem_pw();
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		boolean result = regPass(mem_pw);
		
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		hashmap.put("mem_id", mem_id);
		hashmap.put("mem_pw", mem_pw);
		if(result){
			try
			{
				service.memberUpdatePw(hashmap);
				map.put("result", "success");
				strb.append(mapper.writeValueAsString(map));
				session.removeAttribute("mem_id"); // 2018.07.23 문경 추가 - 비밀번호 변경시, 세션 삭제 -> 다시 로그인 창으로 이동
			}catch(Exception e)
			{
				map.put("result", "failure");
				strb.append(mapper.writeValueAsString(map));
			}
		}
		else{
			map.put("result", "failure_regPass");
			strb.append(mapper.writeValueAsString(map));
		}
		
		return strb.toString();
	}
	
	@RequestMapping(value = "/MyInfoPwUp", method = RequestMethod.POST)
	@ResponseBody
	public String MyInfoPwUp(MemberVO member, HttpServletRequest req, HttpServletResponse resp, Errors error,
			HttpSession session) throws Exception
	{
		
		String mem_id = (String)session.getAttribute("mem_id");
		String mem_pw = member.getMem_pw();
		String result_PW = null;
		System.out.println("mem_pw : "+mem_pw);
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		try{
			result_PW = service.memberMyInfoPwUp(mem_id);
			System.out.println("mem_PW : "+result_PW);
			if(result_PW.equals(mem_pw) && result_PW != null)
			{
				map.put("result", "success");//이미지가 저장되었을 때
				strb.append(mapper.writeValueAsString(map));
			}
			else
			{
				map.put("result", "failure");//이미지가 저장되었을 때
				strb.append(mapper.writeValueAsString(map));
			}
		}catch(Exception e)
		{
			map.put("result", "failure");//이미지가 저장되었을 때
			strb.append(mapper.writeValueAsString(map));
		}
		
		return strb.toString();
	}
	
	
	@RequestMapping(value = "/UpdateImage", method = RequestMethod.POST)
	@ResponseBody //프로필 사진 변경
	public String UpdateImage(MultipartHttpServletRequest req,
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
		 
		String mem_id  = (String)session.getAttribute("mem_id");
		
		 Map<String, Object>hashmap = new HashMap<String, Object>();
		 hashmap.put("mem_id", mem_id);
		 hashmap.put("mem_propic", originFileName);
		 
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
	
		 try{

			service.memberImageUpload(hashmap);
			
			map.put("filename", originFileName);//이미지가 저장되었을 때
			System.out.println("JSON filename = "+originFileName);
			strb.append(mapper.writeValueAsString(map));
			System.out.println(strb.toString());
			
		 }catch(Exception e){
			 map.put("filename", "failure");//이미지가 저장되었을 때
			strb.append(mapper.writeValueAsString(map));
		 }
		 
		 return strb.toString();
	}
	
	
	@RequestMapping(value = "/UpdatePw", produces="application/json; charset=UTF-8") //비밀번호 업데이트 시
	@ResponseBody
	public String UpdatePw( MemberVO vo, HttpServletRequest req, HttpServletResponse resp, Errors error,
			HttpSession session) throws Exception
	{
		String mem_pw = vo.getMem_pw();
		String mem_id = vo.getMem_id();
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		hashmap.put("mem_id", mem_id);
		hashmap.put("mem_pw", mem_pw);
		
		boolean pwresult = regPass(mem_pw);
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		if(pwresult)
		{
			try{
				service.memberUpdatePw(hashmap);
				map.put("result", "success");
				strb.append(mapper.writeValueAsString(map));
			}catch(Exception e){
				map.put("result", "failure");
				strb.append(mapper.writeValueAsString(map));
			}
		}else{
			map.put("result", "failure_regPass");
			strb.append(mapper.writeValueAsString(map));
		}
		
		
		return strb.toString();
	}
	
	
	@RequestMapping(value = "/emailConfirmre",produces="application/json; charset=UTF-8" ) //이메일 전송 및 인증
	@ResponseBody
	public String emailConfirmre(HttpServletRequest req, HttpServletResponse resp,MemberVO vo, Errors error,
			HttpSession session)throws Exception{

		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		String host = "smtp.naver.com";
		final String user = "mun3189@naver.com";
		final String password = "chomk6253";

		String to = vo.getMem_mail();
		int ranNum = randomNum(6);
		
			// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.trust", "smtp.naver.com");
			
		Session session2 = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

			// Compose the message
		try {
			MimeMessage message = new MimeMessage(session2);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

				// Subject
			message.setSubject("MTALK 인증번호입니다. ");

				// Text
			message.setText("귀하의 인증번호 입니다."+": ["+ranNum+"]");

				// send the message
			Transport.send(message);
			
			map.put("result",String.valueOf(ranNum));
			strb.append(mapper.writeValueAsString(map));
			
			email_confirm_bool = true;
		
		} catch (MessagingException e) {
			e.printStackTrace();
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}

		return strb.toString();
	}
	
	

	@RequestMapping(value = "/emailConfirm",produces="application/json; charset=UTF-8" ) //이메일 전송 및 인증
	@ResponseBody
	public String emailConfirm(HttpServletRequest req, HttpServletResponse resp,MemberVO vo, Errors error,
			HttpSession session)throws Exception{

		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		
		String host = "smtp.naver.com";
		final String user = "mun3189@naver.com";
		final String password = "chomk6253";

		String to = vo.getMem_mail();
		int ranNum = randomNum(6);
		
			// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.trust", "smtp.naver.com");
			
		Session session2 = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

			// Compose the message
		try {
			MimeMessage message = new MimeMessage(session2);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

				// Subject
			message.setSubject("MTALK 인증번호입니다. ");

				// Text
			message.setText("귀하의 인증번호 입니다."+": ["+ranNum+"]");

				// send the message
			Transport.send(message);
			
			map.put("result",String.valueOf(ranNum));
			strb.append(mapper.writeValueAsString(map));
			email_confirm_bool = true;
		
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return strb.toString();
	}
	
	@RequestMapping(value = "/InfoUpdate", produces="application/json; charset=UTF-8") // 멤버 정보 전체 업데이트 
	@ResponseBody
	public String process(HttpServletRequest req, HttpServletResponse resp, MemberVO member, Errors error,
			HttpSession session) throws Exception {
	
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();	
		
		String mem_id = (String)session.getAttribute("mem_id");
		MemberVO vo = service.memberLogin(mem_id); // 문경 추가 - 기존의 사용자 이름(변경 전 이름)을 얻기위해 - 07.23
		String pre_name = vo.getMem_name(); //사용자의 기존 이름을 가져옴
		
		String mem_name = member.getMem_name(); // 사용자의 새로운 이름을 가져옴
		String mem_protext = member.getMem_protext();
		
		System.out.println("이전 이름 : "+pre_name + "이후 이름 : "+mem_name);
		
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		hashmap.put("mem_id", mem_id);
		hashmap.put("mem_name", mem_name);
		hashmap.put("mem_protext", mem_protext);

		System.out.println("memberInfoUpdateController");
		
			try{
				service.memberInfoUpload(hashmap);
				
				/* 0725 - 문경 수정 */
				if(!pre_name.equals(mem_name)){
					List<ChatRoomVO> list = new ArrayList<ChatRoomVO>();
					list = chatDAO.ChatMemNameUpdate(mem_id);
					String chatroom_name_new = ""; // 최종 방이름이 들어갈 변수
					for(ChatRoomVO cvo : list){ //사용자가 들어가있는 채팅방이름 리스트로 뽑아옴.
						String chatroom_id = cvo.getChatroom_id();
						String chatroom_name = cvo.getChatroom_name();
						String lastname = "";
						
						String[] usernames = chatroom_name.split(", ");
						
						for(String s : usernames){
							if(chatroom_name_new != "") chatroom_name_new += ", "; 
							if(s.equals(pre_name)){ // 만약 문자열중 이전이름과 일치하는 문자가 있으면 
								s = mem_name; // 그 문자를 바뀐 이름으로 교체
							}
							chatroom_name_new += s; // 새로운 문자열에 바뀐이름과 다른 이름들을 다 넣음. 
						}
						
						ChatRoomVO insertvo = new ChatRoomVO();		
						insertvo.setChatroom_id(chatroom_id);
						insertvo.setChatroom_name(chatroom_name_new);
						insertvo.setMem_id(mem_id);
						
						int updateresult = chatDAO.UpdateRoomName_mem(insertvo);
						if(updateresult > 0) System.out.println("result : "+updateresult); //변경된 이름을 chatroom DB에 업데이트
						
						System.out.println("변경 전 방 이름 : "+chatroom_name + "///, 변경 후 방이름 : "+chatroom_name_new);
						chatroom_name_new = "";
					}
				}
				map.put("result","success");
				strb.append(mapper.writeValueAsString(map));
				
			}catch(Exception e){
				map.put("result", "failure");
				strb.append(mapper.writeValueAsString(map));
			}
		
		return strb.toString();
	}
	
	@RequestMapping(value = "/mailUpdate", produces="application/json; charset=UTF-8") // 멤버 이메일 업데이트 - 2018.07.24 문경 추가
	@ResponseBody
	public String mailUpdate(MemberVO member, HttpSession session) throws JsonProcessingException{
		
		StringBuffer strb = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();	
		
		int res = service.mailupdate(member);
		System.out.println("res 갯수 : "+res);
		if(res>0){
			map.put("result","success");
			strb.append(mapper.writeValueAsString(map));
		}
		else{
			map.put("result","failure");
			strb.append(mapper.writeValueAsString(map));
		}
		return strb.toString();
	}
	/*
	@RequestMapping(value = "/InfoUpdate")
	public String memberEnroll(HttpServletRequest request, Model model)
	{
		MemberVO vo = new MemberVO();
		vo.setMem_id("a001");
		vo.setMem_pw("1234");
		vo.setMem_name("munkeong2");
		vo.setMem_mail("mun31888");
		vo.setMem_protext("munkeong is tomcat");
		vo.setMem_type("user");
		vo.setMem_status("y");
		
		System.out.println("memberInfoUpdateController");
		System.out.println("member_Id : "+vo.getMem_id());
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("mem_id", "a002");
		map.put("mem_name", "munkeong3");
		map.put("mem_pw", "2345");
		map.put("mem_protext", "apache");
		map.put("mem_mail", "apache22");
		
		System.out.println("memberInfoUpdateController");
		
		service.memberInfoUpload(map);
		
		return "index";
	}*/
	public static boolean regPass(String pass){
        if (pass==null) return false;
        boolean b = Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$",pass.trim());
        return b;
	}
	public static int randomNum(int length) {
		 
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
