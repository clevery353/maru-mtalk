package kr.co.marueng.mtalk.controller.websocket;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import kr.co.marueng.mtalk.dao.admin.useData.IuseDataDAO;
import kr.co.marueng.mtalk.dao.chat.IChatDAO;
import kr.co.marueng.mtalk.dao.member.ImemberDAO;
import kr.co.marueng.mtalk.dao.push.IpushDAO;
import kr.co.marueng.mtalk.vo.ChatAttachVO;
import kr.co.marueng.mtalk.vo.ChatBubbleVO;
import kr.co.marueng.mtalk.vo.FriendVO;
import kr.co.marueng.mtalk.vo.MemberVO;
import kr.co.marueng.mtalk.vo.PushVO;
import kr.co.marueng.mtalk.vo.UseDataVO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Positions;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.server.standard.SpringConfigurator;

@Singleton
@ServerEndpoint(value = "/socket", configurator = SpringConfigurator.class)
// ***웹소켓에서 스프링사용시 반드시 설정해야한다.
public class WebSocket {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private IChatDAO chatDAO;
	
	@Inject
	private ImemberDAO memberDAO;
	
	@Inject
	private IpushDAO pushDAO;
	
	@Inject
	private IuseDataDAO adminUseDataDAO; // 0721 소은추가 
	
	// 소켓세션들 리스트
	private List<Session> sessionList = new ArrayList<Session>();
	// 소켓세션 맵
	@Resource(name = "socketMap")
	private Map<String, Session> socketMap;
	// 바이너리 Out스트림
	private BufferedOutputStream bos;
	// 서버 파일저장경로
//	 private String path = "C:/Users/hhkwak/Documents/MTalk_server/";
	 private String path = "C:/Users/방소은/Documents/MTalk_Server/";
//	 private String path = "/Users/soeunbang/Documents/mtalk_server/";

	// 서버 썸네일이미지 저장경로
	private static String imgPath = "";
	// 파일명
	private String fileName = "";
	
	// 서버에 저장될 유니크 파일명
	private String unipueSaveName = "";
	// 현재 파일 채팅방 아이디 임시저장
	private String fileRoomId = "";
	// 현재 파일 보낸사람 아이디 임시저장
	private String fileMemId = "";

	private static File file;
	// 이미지파일이면 url 존재
	private String chatattach_url;

	private String pri_mem_id; // 문경 추가 - 친구신청시, 알림을 보낼때 누구로부터 친구신청이 왔는지 알리기 위해.
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());
		sessionList.add(session);
		MemberVO member = new MemberVO();
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose::" + session.getId());
		sessionList.remove(session);
		for (Object o : socketMap.keySet()) {
			if (socketMap.get(o).equals(session)) {
				socketMap.remove(o);
			}
		}
	}

		
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("onMessage::From=" + session.getId() + " Message="
				+ message);
		
		JSONObject json = new JSONObject(message);
		if (json.getString("type").equals("init")) { // 사용자가 로그인시 웹소켓 연결, 확인하지
														// 않은 채팅이 있는지 조회
			String mem_id = json.getString("mem_id");
			pri_mem_id = mem_id;
			socketMap.put(mem_id, session);
			String lastChatDate = chatDAO.myRoomLastChatDate(mem_id);
			String lastReadDate = chatDAO.myLastReadDate(mem_id);
			Date cdate;
			
			if (lastChatDate != null && lastReadDate != null) {
				try {
					cdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(lastChatDate);
					Date rdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(lastReadDate);
					int resDate = cdate.compareTo(rdate);
					String m = "";
					if (resDate > 0) {
						m = "new";
					} else {
						m = "not";
					}
					try {
						synchronized(session) {
							session.getBasicRemote().sendText("setnew:" + m ); // 친구의 회원목록에 내가 있는지 체크하기 위해 전송
						
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			
			
		} else if (json.getString("type").equals("read")) { // 채팅말풍선 읽었을때
			String room_id = json.getString("text");
			// 해당 대화방에 해당하는 회원 아이디 명단
			List<String> chatMemberList = chatDAO.chatMemberList(room_id);
			for (String mem : chatMemberList) {
				Session ss = socketMap.get(mem);
				if (ss != null) {
					// 해당 대화방에 session들에게 말풍선 정보 message push
					try {
						synchronized(ss) {
							ss.getBasicRemote().sendText("readmsg:" + room_id);							
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (json.getString("type").equals("message")) { // 채팅메시지 작성했을때
			ChatBubbleVO chatBubbleVO = new ChatBubbleVO();
			chatBubbleVO.setChatbubble_text(json.getString("text"));
			chatBubbleVO.setMem_id(json.getString("mem_id"));
			chatBubbleVO.setChatroom_id(json.getString("chatroom_id"));
			chatBubbleVO.setChatbubble_attach("0");

			int result = chatDAO.chatbubbleInsert(chatBubbleVO);
			String room_id = json.getString("chatroom_id");
			
			
			//********* 소은추가 0721 시작
			String mem_id = json.getString("mem_id"); 
			String size = json.getString("text").getBytes().length+"";
			
			UseDataVO useDataVO = new UseDataVO();
			useDataVO.setUse_mem(mem_id);
			useDataVO.setUse_qty(size);
			int useres = adminUseDataDAO.insertUsedata(useDataVO);
			//********* 소은추가 0721 끝 
			
			
			// 해당 대화방에 해당하는 회원 아이디 명단
			List<String> chatMemberList = chatDAO.chatMemberList(room_id);
			for (String mem : chatMemberList) {
				Session ss = socketMap.get(mem);
				if (ss != null) {
					// 해당 대화방에 session들에게 말풍선 정보 message push
					try {
						synchronized(ss) {
							ss.getBasicRemote().sendText("newchat:" + room_id);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (json.getString("type").equals("outMember")) { // 채팅참여자가 나갔을때
			// 채팅방을 나갔을때 해당 채팅방 참여자들에게 푸시
			String room_id = json.getString("chatroom_id");
			List<String> chatMemberList = chatDAO.chatMemberList(room_id);
			for (String mem : chatMemberList) {
				Session ss = socketMap.get(mem);
				if (ss != null) {
					try {
						synchronized(ss) {
							ss.getBasicRemote().sendText("newchat:" + room_id);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (json.getString("type").equals("fileName")) { // 파일명 먼저 받음
			// 클라이언트에서 파일을 전송하기전 파일이름을 "file name:aaa.aaa" 형식으로 보낸다.
			fileName = json.getString("text");
			System.err.println(fileName+"파일이다");
			fileRoomId = json.getString("chatroom_id");
			fileMemId = json.getString("mem_id");
			
			unipueSaveName = doMakeUniqueFileName(path, fileName);
			
			file = new File(path + unipueSaveName);
			try {
				bos = new BufferedOutputStream(new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else if (json.getString("type").equals("end")) { // 파일송신 끝
			// 이미지파일이면 썸네일 웹리소스로도 저장
			System.err.println("fileName" + fileName);
			if (fileName.endsWith(".jpg")	|| fileName.endsWith(".jpeg")) { // png 현재 지원안됨
				try {
					URI uri = this.getClass().getResource("/").toURI();
					imgPath = uri.getPath().substring(0,
							uri.getPath().length() - 8)
							+ "views/resources/chatImg/";
					String saveFileName = resizeImage(file, imgPath);
					chatattach_url = imgPath + saveFileName;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				bos.flush();
				bos.close();
				// 파일 메타데이터 추출
				Path p = Paths.get(path, unipueSaveName);
				BasicFileAttributes attrb;
				try {
					attrb = Files.readAttributes(p, BasicFileAttributes.class);
					ChatBubbleVO chatBubbleVO = new ChatBubbleVO();
					chatBubbleVO.setChatroom_id(fileRoomId);
					chatBubbleVO.setChatbubble_text(fileName);
					chatBubbleVO.setMem_id(fileMemId);
					chatBubbleVO.setChatbubble_attach("1");
					int result = chatDAO.chatbubbleInsert(chatBubbleVO);
					String bubble_id = "";
					if (result > 0) {
						bubble_id = chatDAO.currBubbleGetLastId(chatBubbleVO);
						ChatAttachVO chatAttachVO = new ChatAttachVO();
						chatAttachVO.setChatattach_size(attrb.size() + "");
						chatAttachVO.setChatattach_original(fileName);
						chatAttachVO.setChatattach_path(path + unipueSaveName);
						chatAttachVO.setChatbubble_id(bubble_id);
						unipueSaveName = "";
						//********* 소은추가 0721 시작
						String mem_id = json.getString("mem_id"); 
						String size = attrb.size() + "";
						
						UseDataVO useDataVO = new UseDataVO();
						useDataVO.setUse_mem(mem_id);
						useDataVO.setUse_qty(size);
						int useres = adminUseDataDAO.insertUsedata(useDataVO);
						//********* 소은추가 0721 끝 
						
						if (chatattach_url != null) {
							chatAttachVO.setChatattach_url(chatattach_url);
							chatattach_url = null;
						}
						int chatAttach_id = chatDAO
								.chatAttachInsert(chatAttachVO);
						if (chatAttach_id > 0) {
							// 해당 대화방에 해당하는 회원 아이디 명단
							List<String> chatMemberList = chatDAO.chatMemberList(fileRoomId);
							for (String mem : chatMemberList) {
								Session ss = socketMap.get(mem);
								if (ss != null) {
									// 해당 대화방에 session들에게 말풍선 정보 message push
									try {
										synchronized(ss) {
											ss.getBasicRemote().sendText(
													"newchat:" + fileRoomId);
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					} else {
						System.err.println("말풍선 insert 실패");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (json.getString("type").equals("filedownload")) { // 파일저장 클릭시
			String chatbubble_id = json.getString("text");
			ChatAttachVO chatAttachVO = chatDAO.chatAttachSelect(chatbubble_id);
			String downpath = chatAttachVO.getChatattach_path();
			System.err.println(downpath);
			File file = new File(downpath);
			System.err.println(file.exists() + ", " + file.length() + ", "
					+ file.getAbsolutePath());
			byte[] fileBytes = new byte[(int) file.length()];
			System.err.println(file.length());
			try (BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));) {
				bis.read(fileBytes);
				ByteBuffer buf = ByteBuffer.wrap(fileBytes);
				System.err.println(buf.toString());
				session.getBasicRemote().sendBinary(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (json.getString("type").equals("typing")) { // 상대방이 채팅 입력중일때
			String fri_name = json.getString("text");
			System.err.println("여기 들어옴");
			System.err.println(json.getString("chatroom_id"));
			String room_id = json.getString("chatroom_id");
			List<String> chatMemberList = chatDAO.chatMemberList(room_id);
			for (String mem : chatMemberList) {
				Session ss = socketMap.get(mem);
				if (ss != null) {
					try {
						synchronized(session) {
							ss.getBasicRemote().sendText("typingchat:" + room_id+", "+fri_name);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if(json.getString("type").equals("submit_add_friend")) { //친구신청 // 사용자가 온라인중이면 알림을 띄워줌. 없을 경우 db에 저장함.
			System.out.println("여긴들어옴");
			String findId = json.getString("text");
			System.out.println("친구신청할려는 아이디 : "+findId);
			Session ss = socketMap.get(findId);
			if(ss != null)
			{
				try {
					synchronized(session) {
						ss.getBasicRemote().sendText("submit_add_friend:" + pri_mem_id);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				PushVO vo = new PushVO();
				vo.setPush_mem(findId);
				vo.setPush_status("n");
				vo.setPush_type("f");
				try{
					pushDAO.push_insert(vo);
				}catch(Exception e){}
				
			}
			
		}
		else if(json.getString("type").equals("chatroom_creat")){ //테스트 미완료
			String chatroom_id = json.getString("text"); // 채팅방 사람들의 id를 가져오는 부분
			
			List<String>chat_mem = new ArrayList<String>();
			String chat_p[] = chatroom_id.split("&");
			
			for(String cm : chat_p){
				chat_mem.add(cm);
				System.out.println(cm + ", ");
			} // 멤버 아이디들 하나씩 분리
			for(String mem : chat_mem){
				Session ss = socketMap.get(mem);
				if(ss != null)
				{
					try{
						synchronized(session) {
							ss.getBasicRemote().sendText("chatroom_creat:" + chatroom_id);
						}
					}catch(Exception e){}
				}
			}
			/* 문경 추가 - 친구 목록에서 더블클릭해서 대화방이 생겼을 때 주는 푸쉬알림 - 2018.07.21 */
		}else if(json.getString("type").equals("onefriend_creat")){
			String friend_apply_room = json.getString("text");
			
			Session ss = socketMap.get(friend_apply_room);
			if(ss != null){
				try{
					synchronized(session) {
						ss.getBasicRemote().sendText("onefriend_creat:" + pri_mem_id); // 채팅방이 생성됬을 때 채팅방 생성 알림을 보내줌.
					}
				}catch(Exception e){}
			}
		}else if(json.getString("type").equals("userLogin")){
			String mem_id = json.getString("text");
			// 접속한 회원의 친구중에 현재 접속중인 사용자가 있는지 체크
			 /* 문경 수정 - (이사왔어용) 사용자가 안읽은 알림이있으면 n아이콘을 띠워줌. 07.25 */
			String push_status = null;
			String push_type = null;
			String result_push = null;
			
			try{
				PushVO vo = pushDAO.push_get(mem_id);
				push_status = vo.getPush_status();
				push_type = vo.getPush_type();
				
				System.out.println("push_status : "+push_status);
				System.out.println("push_type : "+push_type);
				if(vo != null)
				{
					if(push_status.equals("n") && push_type.equals("f")){
						result_push = "new";
					}
					else{
						result_push = "not";
					}
				}
				
				try{
					synchronized(session) {
						session.getBasicRemote().sendText("pushnew:" + result_push ); // 친구의 회원목록에 내가 있는지 체크하기 위해 전송
					}
				}catch(Exception e){}
			}catch(Exception e){}
			
			 /* 문경 수정 - 처음 사용자가 접속했을 시 사용자가 확인하지 않은 친구 신청이 있을시 new를 전송 07.24*/
			List<FriendVO>fvo = (List<FriendVO>)memberDAO.alertLogin(mem_id); //사용자가 로그인했을시, 사용자의 친구들에게 푸쉬알림을 띄워줌.
			for(FriendVO mem : fvo){
				Session ss = socketMap.get(mem.getFri_friendmem());
				if(ss != null){
					try{
						ss.getBasicRemote().sendText("friendTo:" + mem_id);
					}catch(Exception e){e.printStackTrace();}
				}
			}
		}else if(json.getString("type").equals("sendMessagepush")){
			String mem_id = json.getString("text");
			
		}
	}

	// 바이너리 데이터 수신시
	@OnMessage
	public void processUpload(ByteBuffer msg, boolean last, Session session)
			throws IOException {

		while (msg.hasRemaining()) {
			try {
				bos.write(msg.get());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnError
	public void onError(Throwable t) {
		this.logger.error("web socket error!", t);
		System.out.println("onError::" + t.getMessage());
	}

	// 이미지 리사이징 return saveFileName
	public String resizeImage(File file, String savePath) throws EOFException {
		String saveFileName = "";
		BufferedImage bufferedImage = null;
		try {
			saveFileName = doMakeUniqueFileName(savePath, fileName);
			bufferedImage = ImageIO.read(file);
			System.err.println("파일세이브"+saveFileName);
			
			String[] type = fileName.split("\\.");
			String t = "jpg";
			if("png".equals(type[type.length-1])){
				t = "png";
			}
			
			Thumbnails.of(file)
				.outputFormat(t)
				.size(300, 200)
				.addFilter(new Canvas(300, 200, Positions.CENTER, Color.WHITE))
				.outputQuality(1.0)
				.toFile(new File(savePath+saveFileName));

			System.err.println("저장경로 : " + savePath+saveFileName);
		} catch (IOException e) {
			try {
				bufferedImage = ImageIO.read(file);
				String[] type = fileName.split("\\.");
				saveFileName = doMakeUniqueFileName(savePath, fileName);
				File excFile = new File(savePath+saveFileName);
				ImageIO.write(bufferedImage, type[type.length-1], excFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.err.println("파일이름 : "+saveFileName);
		return saveFileName;
	}

	// 유니크한 파일명 생성
	public static String doMakeUniqueFileName(String serverPath, String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf("."));
		String uniqueFileName = null;
		boolean flag = true;
		while (flag) {
			uniqueFileName = getUniqueFileName();
			flag = doCheckFileExists(serverPath + uniqueFileName + extension);
		}
		return uniqueFileName + extension;
	}
	
	private static boolean doCheckFileExists(String fullPath) {
		return new File(fullPath).exists();
	}

	private static String getUniqueFileName() {
		return UUID.randomUUID().toString();
	}
	
	// 투명이미지 복원
	public class ThumbnailsImgFilter implements ImageFilter {
	    @Override
	    public BufferedImage apply(BufferedImage img) {
	        int w = img.getWidth();
	        int h = img.getHeight();
	        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphic = newImage.createGraphics();
	        graphic.setColor(Color.white);
	        graphic.fillRect(0, 0, w, h);
	        graphic.drawRenderedImage(img, null);
	        graphic.dispose();
	        return newImage;
	    }
	}

	
	
}
