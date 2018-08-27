package kr.co.marueng.mtalk;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {
	
	@RequestMapping("/index")
	public String indexAccess(HttpSession session){
		String goPage = "";
		String userType = (String)session.getAttribute("mem_type");
		System.err.println("indexcontroller:"+userType);
		if(userType!=null){
			if(userType!="member"){
				goPage = "errors/error2";
			}else{
				goPage="index";
			}
		}else{
			goPage="errors/error2";
		}
		return goPage;
	}
	@RequestMapping("/page")
	public String pageAccess(){
		return "page";
	}
	@RequestMapping("/admin/page3")
	public String adminPage3Access(){
		return "includee_admin/page3";
	}

}
