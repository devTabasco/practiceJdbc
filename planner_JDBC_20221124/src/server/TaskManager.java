package server;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

import server.beans.ToDoBean;

public class TaskManager {
	
	public TaskManager() {
		
	}
	
	
	public String getTodoDateCtl(String clientData) {
		ArrayList<ToDoBean> toDoList = null;
		DataAccessObject dao = new DataAccessObject();
		ToDoBean todo = ((ToDoBean)this.setBean(clientData));
	   	    
		Connection connection = dao.openConnection();
		toDoList = dao.getToDoDate(connection, todo);				
		dao.closeConnection(connection);
		
		this.modifyToDoList(toDoList);
		
		return this.convertServerData(this.makeDayList(toDoList)); 	
	}
	
	public String getToDoListCtl(String clientData) {
		String result = null;
		DataAccessObject dao = new DataAccessObject();
		ToDoBean todo = ((ToDoBean)this.setBean(clientData));
		//serviceCode=13&startDate=20221112&accessCode=changyong
		
		Connection connection = dao.openConnection();
		if(clientData.split("&")[0].split("=")[1].equals("12")) {
			result = this.convertServerData2(dao.getToDoList(connection, todo));			
		}else if(clientData.split("&")[0].split("=")[1].equals("13")) {
			/* 신규 
			 * 
			 * 작성일 : 221124
			 * 개발자 : 임창용
			 * 메서드 기능설명
			 * - Modify 시 선택한 날짜 정보를 받아와 dao.getToDoList2에 전달
			 * - dao에서 전달받은 ArrayList를 convertServerData2를 통해 String으로 변환
			 * 
			 * */
			result = this.convertServerData2(dao.getToDoList2(connection, todo));
		}
		
		dao.closeConnection(connection);
		
		return result;
	}
	
	public boolean updateTodo(String clientData) {
		boolean result = false;
		DataAccessObject dao = new DataAccessObject();
		ToDoBean todo = ((ToDoBean)this.setBean(clientData));
		
		Connection connection = dao.openConnection();
		result = this.convertToBool(dao.updateTodo(connection, todo,clientData.split("&")[3]));					
		dao.closeConnection(connection);
		
		return result;
	}
	
	/* DAO로부터 전달받은 ToDoList의 EDate의 값을 수정 */
	private void modifyToDoList(ArrayList<ToDoBean> toDoList) {
		String date = null;
		for(ToDoBean todo : toDoList) {
			date = todo.getStartDate().substring(0, 6);
			
			if( !date.equals(todo.getEndDate().substring(0, 6))) {
				// date를 LocalDate 변환  --> 해당월의 마지막 날 구하기
				// --> endDate를 해당월의 마지막 날로 변경
				todo.setEndDate(
						date + LocalDate.of(Integer.parseInt(date.substring(0, 4)), 
						Integer.parseInt(date.substring(4, 6)),	1).lengthOfMonth());
			}
		}
	}
	
	private ArrayList<Integer> makeDayList(ArrayList<ToDoBean> toDoList){
		ArrayList<Integer> dayList = new ArrayList<Integer>();
		boolean isSave;
		for(ToDoBean date : toDoList) {
			
			for(int dateIdx=Integer.parseInt(date.getStartDate().substring(6)); 
						dateIdx <= Integer.parseInt(date.getEndDate().substring(6)); 
						dateIdx++) {
				isSave = true;
				for(int listIdx=0; listIdx<dayList.size(); listIdx++) {

					if(dayList.get(listIdx) == dateIdx) {				
						isSave = false;
						break;
					}
				}
				if(isSave) dayList.add(dateIdx);
			}
		}
		return dayList;
	}
	
	/* 신규 
	 * 작성일 : 221124
	 * 개발자 : 임창용, 염은진
	 * 
	 * 메서드 기능설명
	 * - ClientData의 정보를 Bean에 담기
	 * - connection Open & Close
	 * - DAO에 Bean, Connection 전달 > DB에 입력 준비
	 * 
	 * 파라미터 설정 이유
	 * - clientData를 전달받아 setBean을 통해 Bean에 데이터 담기 위함
	 * 
	 * Return Type 설정이유
	 * - todoList 등록 이후 transaction 결과를 boolean으로 받아오기 때문에 boolean으로 그대로 return
	 * 
	 * */
	public boolean createListCtl(String clientData) {
		//serviceCode=22&startDate=20221103121212&endDate=20221124000000&content=DDD&accessCode=changyong
		DataAccessObject dao = new DataAccessObject();
		boolean result = false;
		boolean insertResult = false;
		ToDoBean todo = ((ToDoBean)this.setBean(clientData));
		
		Connection connection = dao.openConnection();
		insertResult = convertToBool(dao.insertTodo(connection, todo));
		if(insertResult) {
			result = dao.setTransaction(insertResult, connection);
		}
		dao.closeConnection(connection);
		
		return result;
	}
	
	/* ServerData : ToDoDate */
	private String convertServerData(ArrayList<Integer> list) {
		StringBuffer serverData = new StringBuffer();
		
		for(int day : list) {
			serverData.append(day + ":");
		}
		
		/* 마지막으로 추가된 항목 삭제 */
		if(serverData.length() != 0) {
			serverData.deleteCharAt(serverData.length()-1);
		}
		return serverData.toString();
	}
	
	/* ServerData : ToDoList */
	private String convertServerData2(ArrayList<ToDoBean> list) {
		StringBuffer serverData = new StringBuffer();
		
		for(ToDoBean todo : list) {
			serverData.append(todo.getAccessCode() != null? todo.getAccessCode() + ",":"");
			serverData.append(todo.getStartDate() != null? todo.getStartDate() + "," :"");
			serverData.append(todo.getEndDate() != null? todo.getEndDate() + ",":"");
			serverData.append(todo.getContents() != null? todo.getContents() + ",":"");
			serverData.append(todo.getStatus() != null? todo.getStatus() + ",":"");
			serverData.append(todo.isActive()  + ",");
			serverData.append(todo.getComments() != null? todo.getComments() + ",":"");
			if(serverData.charAt(serverData.length()-1) == ',') {
				serverData.deleteCharAt(serverData.length()-1);
			}
			serverData.append(":");
		}
		
		if(serverData.length() > 0 &&  serverData.charAt(serverData.length()-1) == ':') {
			serverData.deleteCharAt(serverData.length()-1);
		}
		
		return serverData.toString();
	}
		
	private Object setBean(String clientData) {
		Object object = null;
		String[] splitData = clientData.split("&");
		switch(splitData[0].split("=")[1]) {
		case "9":
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[1].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[2].split("=")[1]);
			break;
		case "12":
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[4].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[1].split("=")[1]);
			((ToDoBean)object).setEndDate(splitData[2].split("=")[1]);
			String visibleType = splitData[3].split("=")[1];
			if(!visibleType.equals("T")) {
				((ToDoBean)object).setVisibleType(visibleType.equals("E")? "A": "I");
			}
			break;
		/* 신규 
		 * 
		 * 작성일 : 221124
		 * 개발자 : 임창용, 박건호
		 * 
		 * 기능 설명
		 * - 수정할 날짜 정보가 담긴 clientData 정보를 Bean에 담기
		 * 
		 * */
		case "13":
			//serviceCode=13&startDate=20221112&accessCode=changyong
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[2].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[1].split("=")[1]);
			break;
		case "14":
			//serviceCode=14&accessCode=changyong&20221103,20221103000000,20221117000000,Good!,B,true,영국여행&startDate=20221212000000
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[1].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[2].split(",")[1]);
			((ToDoBean)object).setEndDate(splitData[2].split(",")[2]);
			((ToDoBean)object).setContents(splitData[2].split(",")[3]);
			((ToDoBean)object).setStatus(splitData[2].split(",")[4]);
			((ToDoBean)object).setActive(splitData[2].split(",")[5].equals("true")?true:false);			
			((ToDoBean)object).setComments(splitData[2].split(",")[6]);
			break;
		/* 신규 
		 * 
		 * 작성일 : 221124
		 * 개발자 : 임창용, 염은진
		 * 
		 * 기능설명
		 * - 등록관련 clientData를 Bean에 담기
		 * - 이때, Default 값으로 넣어줄 데이터도 함께 담기
		 * 
		 * */
		case "22":
			//serviceCode=22&startDate=20221103121212&endDate=20221124000000&content=DDD&accessCode=changyong
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[4].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[1].split("=")[1]);
			((ToDoBean)object).setEndDate(splitData[2].split("=")[1]);
			((ToDoBean)object).setContents(splitData[3].split("=")[1]);
			((ToDoBean)object).setStatus("B");
			((ToDoBean)object).setActive(true);
			((ToDoBean)object).setComments("NONE");
			break;
		}
		return object;
	} 
	
	/* 신규 
	 * 
	 * 작성일 : 221124
	 * 개발자 : 임창용
	 * 
	 * 메서드 기능 설명
	 * - update, insert 시 DataBase에서 넘어오는 int값을 boolean으로 변경하는 method
	 * 
	 * 파라미터 선정 이유
	 * - int형을 변환하기 위함
	 * 
	 * return type 선정 이유
	 * - int형을 boolean형으로 변환하기 위함
	 * 
	 * */
	private boolean convertToBool(int value) {
		return value>0?true:false;
	}

}
