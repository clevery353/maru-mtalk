package kr.co.marueng.mtalk.controller.friend;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.mtalk.service.friend.IfriendService;

@Controller
@RequestMapping("/friend")
public class FriendRetrieveController {
	@Inject
	private IfriendService service;
	
	public void setService(IfriendService service){
		this.service = service;
	}
	
	@RequestMapping(value="friendList", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Object> friendList(
			String mem_id
			){
			return service.retrieveFriendList(mem_id);
	}
}
