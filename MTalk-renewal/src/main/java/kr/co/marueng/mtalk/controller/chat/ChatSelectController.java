package kr.co.marueng.mtalk.controller.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import kr.co.marueng.mtalk.service.chat.IChatService;
import kr.co.marueng.mtalk.service.notice.InoticeService;
import kr.co.marueng.mtalk.vo.ChatAttachVO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.ChatReadVO;
import kr.co.marueng.mtalk.vo.ChatRoomVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.NoticeVO;
import kr.co.marueng.mtalk.vo.OgtagVO;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 방소은
 * 작성일 : 2018.07.09
 * 채팅조회 관련 컨트롤러
 */
@Controller
@RequestMapping("/chat")
public class ChatSelectController {

	@Inject
	IChatService chatService;
	
	@Inject
	InoticeService noticeService;
	/**
	 * 채팅목록조회
	 * @param mem_id
	 * @return 채팅방VO List반환
	 */
	@RequestMapping("list")
	@ResponseBody
	public List<ChatRoomVO> chatApply(HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		List<ChatRoomVO> chatRoomList = chatService.chatSelectList(mem_id);
		List<ChatReadVO> myReadList = chatService.myReadList(mem_id);
		for(ChatRoomVO vo : chatRoomList){
			for(ChatReadVO vo2 : myReadList){
				if(vo.getChatroom_id().equals(vo2.getChatroom_id())){
					vo.setMemReadDate(vo2.getChatread_date());					
				}
			}
		}
		return chatRoomList;
	}
	/**
	 * 모든 채팅내용 말풍선 조회
	 * @param chatroom_id
	 * @param session
	 * @return
	 */
	@RequestMapping("allBubble")
	@ResponseBody
	public Map<String, Object> roomAllBubbleSelect(String chatroom_id, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		List<ChatBubbleVO> allBubbleList = chatService.roomAllBubble(chatroom_id, mem_id);
		List<ChatReadVO> chatreadList = chatService.chatreadDate(chatroom_id);
		NoticeVO notice = noticeService.selectNoticeBiggerOne();
		Map<String, Object> chatSelectMap = new HashMap<>();
		chatSelectMap.put("allBubbleList", allBubbleList);
		chatSelectMap.put("chatreadList", chatreadList);
		chatSelectMap.put("latelyNotice", notice);
		return chatSelectMap;
	}
	/**
	 * 이미지 썸네일 가져오기
	 * @param chatbubble_id
	 * @param session
	 * @return
	 */
	@RequestMapping("image")
	@ResponseBody
	public Map<String, String> attachSelectOne(String chatbubble_id, HttpSession session){
		ChatAttachVO attachVO = chatService.attachSelectOne(chatbubble_id);
		Map<String, String> result = new HashMap<>();
		if(attachVO.getChatattach_url()!=null){
			String[] urlarr = attachVO.getChatattach_url().split("/");
			String url = "/MTalk/res/chatImg/"+urlarr[urlarr.length-1];
			result.put("url", url);
		}else{
			result.put("url", "");
		}
		return result;
	}
	
	@RequestMapping("chatmem")
	@ResponseBody
	public List<String> chatMemberList(String chatroom_id){
		List<String> chatMemberList = chatService.chatMemberList(chatroom_id);
		return chatMemberList;
	}
	/**
	 * 채팅창 OGTag 내용 크롤링
	 * @param url
	 * @return 
	 */
	@RequestMapping("ogtag")
	@ResponseBody
	public Map<String, List<String>>  getOgtag(String inputurl){
		Document doc;
		Map<String, List<String>> result = new HashMap<String,List<String>>();
		
		OgtagVO ogtag = chatService.selectOgtag(inputurl);
		System.err.println(ogtag);
		if(ogtag==null){ // DB조회시 없으면 새로 크롤링, 그리고 조회된 값을 DB에 insert
			try {
				doc = Jsoup.connect(inputurl).get();
				Elements ogElements = doc.select("meta[property^=og], meta[name^=og]");
				for (Element e : ogElements) {
					String target = e.hasAttr("property") ? "property":"name";
					
					if(!result.containsKey(e.attr(target))){
						result.put(e.attr(target), new ArrayList<String>());
					}
					result.get(e.attr(target)).add(e.attr("content"));
				}
				
				if(result.get("og:url")==null){
					List<String> list1 = new ArrayList<String>();
					list1.add(inputurl);
					result.put("og:url", list1); // url도 사용자가 입력했던 url로 한다
				}
				if(result.get("og:image")==null){ 
					ogElements = doc.select("img[src]");
					List<String> list2 = new ArrayList<String>();
					if(ogElements.size() > 0){ // og이미지가 없을때 사이트내 첫번째 이미지로 가져온다
						list2.add(ogElements.get(0).attr("src"));
						result.put("og:image", list2);
					}else{ // 사이트내 이미지가 없으면 상위 root로 가서 가져온다
						String rooturl = StringUtils.substringBetween(inputurl, "/([a-zA-Z]+tp://[a-zA-Z0-9._-]+)/gi");
						doc = Jsoup.connect(rooturl).get();
						ogElements = doc.select("img[src]");
						String src = ogElements.get(0).attr("src");
						if(src.startsWith("/")){
							src = rooturl+src;
						}
						list2.add(ogElements.get(0).attr("src"));
						result.put("og:image", list2);
					}
					// ogtag의 이미지가 상대경로이면
					if(result.get("og:image") != null){
						String src = result.get("og:image").get(0);
						if(src.startsWith("/")){
							String rooturl = result.get("og:url").get(0);
							src = rooturl+src;
							result.get("og:image").set(0, src);
						}
					}
				}
				if(result.get("og:title")==null){
					ogElements = doc.select("title");
					List<String> list3 = new ArrayList<String>();
					list3.add(ogElements.get(0).text());
					result.put("og:title", list3);
				}
				if(result.get("og:description")==null){
					ogElements = doc.select("title");
					List<String> list4 = new ArrayList<String>();
					list4.add(ogElements.get(0).text());
					result.put("og:description", list4); // 없어서 만드는경우는 설명도 타이틀과 같다
				}
				List<String> list4 = new ArrayList<>();
				list4.add(inputurl);
				result.put("inputurl", list4); // 사용자가 입력한 input url 추가
				int insertRes = chatService.insertOgtag(result);
			} catch (Exception e){
				List<String> illegal1 = new ArrayList<String>();
				illegal1.add(inputurl);
				result.put("inputurl", illegal1);
				result.put("og:url", illegal1);
				List<String> illegal2 = new ArrayList<String>();
				illegal2.add("링크");
				result.put("og:title", illegal2);
				result.put("og:image", null);
				List<String> illegal3 = new ArrayList<String>();
				illegal3.add("여기를 눌러 링크를 확인하세요");
				result.put("og:description", illegal3);
				int insertRes = chatService.insertOgtag(result);
				return result;
			}
		}else{
			List<String> image = new ArrayList<String>();
			image.add(ogtag.getOgtag_image());
			result.put("og:image", image);
			
			List<String> title = new ArrayList<String>();
			title.add(ogtag.getOgtag_title());
			result.put("og:title", title);
			
			List<String> urllist = new ArrayList<String>();
			urllist.add(ogtag.getOgtag_url());
			result.put("og:url", urllist);
			
			List<String> desc = new ArrayList<String>();
			desc.add(ogtag.getOgtag_description());
			result.put("og:description", desc);
			System.err.println("여기는 DB에 있을시");
		}
		return result;
	}
	/**
	 * 채팅방에 채팅참여자 목록 확인
	 * @param chatroom_id
	 * @return 참여자들의 프로필정보 리스트
	 */
	@RequestMapping("memList")
	@ResponseBody
	public List<MemberVO> currentChatMemList(String chatroom_id, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		List<MemberVO> chatMemList = chatService.currentChatMemList(mem_id, chatroom_id);
		return chatMemList;
	}
	
	/**
	 * 채팅방 검색
	 * @param search_txt
	 * @return 검색조건에 해당 채팅방 리스트
	 * ***여기에서 반환되는 목록은 chatroom_id만 있다. 프론트에서 해당 방만 걸러서 보여준다.
	 */
	@RequestMapping("roomsearch")
	@ResponseBody
	public List<ChatRoomVO> chatroomSearch(String search_txt, String search_type, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		List<ChatRoomVO> chatRoomList = chatService.chatroomSearch(mem_id, search_txt);
		return chatRoomList;
	}
	
	/**
	 * 채팅내용저장
	 * @param chatroom_id
	 * @return List<ChatBubbleVO>
	 */
	@RequestMapping("chatsave")
	@ResponseBody
	public List<ChatBubbleVO> chatbubbleAllSave(String chatroom_id, HttpSession session){
		String mem_id = (String) session.getAttribute("mem_id");
		List<ChatBubbleVO> chatRoomList = chatService.chatbubbleAllSave(mem_id, chatroom_id);
		return chatRoomList;
	}
	
	/**
	 * 문경 추가 0723
	 * 채팅방 인원수 가져옴.
	 * @param chatroom_id
	 * @return 채팅방 참여 명수
	 */
	@RequestMapping("chatmemberCnt")
	@ResponseBody
	public int ChatMemberCnt(int chatroom_id, HttpSession session){
		int result_count; //결과 값 저장
		result_count = chatService.chatmemberCount(chatroom_id);
		return result_count;
	}
	
}
