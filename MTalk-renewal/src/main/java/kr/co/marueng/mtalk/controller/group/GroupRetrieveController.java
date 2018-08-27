package kr.co.marueng.mtalk.controller.group;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.mtalk.service.group.IgroupService;

@Controller
@RequestMapping("/group")
public class GroupRetrieveController {
	@Inject
	private IgroupService service;
	
	public void setService(IgroupService service){
		this.service = service;
	}
	
	@RequestMapping(value="list", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, String> groupList(
			String mem_id
			){
			return service.retrievegroupList(mem_id);
	}
}
