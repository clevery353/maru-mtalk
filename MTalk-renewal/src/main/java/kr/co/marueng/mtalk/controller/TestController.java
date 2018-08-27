package kr.co.marueng.mtalk.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.marueng.mtalk.service.TestService;
import kr.co.marueng.mtalk.vo.TestVO;

@Controller
@RequestMapping("/test")
public class TestController {
      
      @Inject
      TestService TestService;
      
      @RequestMapping(method = RequestMethod.GET)
      public String goTestPage(Model model, HttpServletRequest request){
           
            
            return "testBoot";
      }
}


