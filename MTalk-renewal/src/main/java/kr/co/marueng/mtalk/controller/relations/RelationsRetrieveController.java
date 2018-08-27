package kr.co.marueng.mtalk.controller.relations;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.marueng.mtalk.service.relations.IrelationsService;
import kr.co.marueng.mtalk.vo.MemberVO;

@Controller
@RequestMapping("/relations")
public class RelationsRetrieveController {
	@Inject
	private IrelationsService service;
	
	public void setService(IrelationsService service){
		this.service = service;
	}
	
	@RequestMapping(value="relationsList", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<MemberVO> relationsList(
			String mem_id
			){
			return service.retrieveRelations(mem_id);
	}
}
