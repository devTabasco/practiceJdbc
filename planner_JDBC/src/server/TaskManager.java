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
		
		Connection connection = dao.openConnection();
		result = this.convertServerData2(dao.getToDoList(connection, todo));
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
		}
				
		return object;
	} 

}
