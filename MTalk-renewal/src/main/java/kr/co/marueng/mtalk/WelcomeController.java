package kr.co.marueng.mtalk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class WelcomeController {
	
	@RequestMapping("/")
	public String indexAccess(){
		return "welcome";
	}
}
