package server;

/* 클라이언트 요청에 따른 서비스 분기 */
public class ServerController {
	
	public ServerController() {}
	
	public String controller(String clientData) {
		

		String result = null;
		String serviceCode = (clientData.split("&")[0]).split("=")[1];
				
		if(serviceCode.equals("1")) {
			result = new Auth().accessCtl(clientData)? "1": "0";
		}else if(serviceCode.equals("-1")) {
			new Auth().accessOut(clientData);
			result = "로그아웃 완료";
		}else if(serviceCode.equals("9")) {
			result = new TaskManager().getTodoDateCtl(clientData);
		}else if(serviceCode.equals("12")) { 
			result = new TaskManager().getToDoListCtl(clientData);	
		}else if(serviceCode.equals("13")) { /* 신규 */
			result = new TaskManager().getToDoListCtl(clientData);
		}else if(serviceCode.equals("14")) { /* 신규 */
			result = new TaskManager().updateTodo(clientData)?"업데이트성공":"업데이트실패";
		}else if(serviceCode.equals("22")) { 
			//serviceCode=22&startDate=20221103121212&endDate=20221124000000&content=DDD&accessCode=changyong
			/* 신규 
			 * 작성일 : 221124
			 * 개발자 : 임창용
			 * 메서드 기능설명
			 * - serviceCode가 22인 경우 위와 같은 clientData를 createListCtl로 전달
			 * - boolean type으로 return 받아 성공,실패 여부 확인
			 * 
			 * */
			
			result = new TaskManager().createListCtl(clientData)?"등록성공":"등록실패";
		}
		
		return result; 
	}
}
