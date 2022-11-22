package client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import server.ServerController;

public class TaskManagement {
	
	private ServerController server;
	
	public TaskManagement() {
		server = new ServerController();
	}

	public Object taskController(int selection, String accessCode, int month) {
		Object result = null;
		switch(selection) {
		case 11: case 21:
			result = this.makeTaskCalendarCtl(accessCode, month);
			break;
		}
		
		return result;
	}
	
	public Object taskController(String clientData) {
		Object result = null;
		switch(clientData.split("&")[0].split("=")[1]) {
		case "12":
			result = this.getTaskListCtl(clientData);
			break;
		case "21":
			result = this.TaskRegistrationCtl(clientData);
			break;
		case "31":
			result = this.setModifyTaskCtl(clientData);
			break;
		case "41":
			result = this.getTaskStatCtl(clientData);
			break;
		}
		return result;
	}

	/* 특정 달의 Task Calendar 생성하기 */
	private Object makeTaskCalendarCtl(String accessCode, int month) {	
		LocalDate today = LocalDate.now().plusMonths(month);
		
		int[] taskDays = this.getTaskDays(this.server.controller("serviceCode=9&accessCode="+ accessCode + 
						 "&date=" + today.format(DateTimeFormatter.ofPattern("yyyyMM"))));

		return this.makeCalendar(taskDays, today);
	}
	
	/* 등록된 할일 리스트 가져오기 */
	private Object getTaskListCtl(String clientData) {
		String serverData = this.server.controller(clientData);
		return !serverData.equals("")? this.makeTodoList(this.convertTwoArray(serverData.split(":"))): "\n   등록된 일정이 없습니다.\n";
	}
	
	/* 할일 등록 하기 */
	private Object TaskRegistrationCtl(String clientData) {
		
		return null;
	}
	/* 등록된 할일 수정하기 */
	private Object setModifyTaskCtl(String clientData) {
		return null;
	}
	/* 할일에 대한 통계 만들기 */
	private Object getTaskStatCtl(String clientData) {
		return null;
	}
	
	private String makeTodoList(String[][] toDoList) {
		/* 항목 정렬 */
		StringBuffer buffer = new StringBuffer();
		
		/* 출력양식 만들기 */
		String now;
		int itemCount = 0, beginIdx = 0;
		while(true) {
			now = toDoList[beginIdx][0];
			itemCount = this.itemCount(toDoList, now);
				
			buffer.append("   [ " + now.substring(0, 4) +". "+ now.substring(4, 6) + ". " + now.substring(6) + " ]");
			buffer.append("  Total " + itemCount + " Items\n");
			buffer.append("   ─────────────────────────────────────────────────────\n");
			buffer.append("     ITEM\n");
			buffer.append("     └─   Start Date\tEnd Date\tProcess\tEnable\n");
			buffer.append("   ─────────────────────────────────────────────────────\n");
			
			for(int itemIdx=beginIdx; itemIdx<(beginIdx+itemCount); itemIdx++) {
				buffer.append(itemIdx>beginIdx?"     ------------------------------------------------\n":"");
				buffer.append("     " + toDoList[itemIdx][1].substring(8, 12));
				buffer.append(" " + toDoList[itemIdx][3] + "\n");
				buffer.append("     └─   " + this.formattedDate(toDoList[itemIdx][1]) + "\t" );
				buffer.append(this.formattedDate(toDoList[itemIdx][2]) + "\t");
				buffer.append(this.processingValue(toDoList[itemIdx][4]) + "\t");
				buffer.append(toDoList[itemIdx][5].equals("true")? "활성 \n":"삭제\n");
				buffer.append((!toDoList[itemIdx][6].equals("NONE"))?"     └─   " + toDoList[itemIdx][6]+ "\n":"");
			}
			buffer.append("   ─────────────────────────────────────────────────────\n");
			
			if((beginIdx+itemCount) == toDoList.length) break;
			buffer.append("\n");
			beginIdx += itemCount;
		}

		return buffer.toString();
	}
	
	/* 1차원 배열 --> 2차원 배열화 */
	private String[][] convertTwoArray(String[] record){
		String[][] toDoList = null;
		String temp;
		
		/* Sort */
		for(int idx=0; idx<record.length ; idx++) {
			if(idx != record.length-1) {
				for(int subIdx=idx+1; subIdx<record.length ; subIdx++) {
					if(Integer.parseInt(record[idx].substring(0, 8)) > Integer.parseInt(record[subIdx].substring(0, 8))) {
						temp = record[idx];
						record[idx] = record[subIdx];
						record[subIdx] = temp;
					}
				}
			}
		}
		
		/* 정렬된 데이터의 2차원 배열 객체화 */
		toDoList = new String[record.length][];
		for(int idx=0; idx<record.length ; idx++) {
			toDoList[idx] = record[idx].split(",");
		}
		return toDoList;
	}
	
	/* 진행상황 */
	private String processingValue(String text) {
		String result = null;
		switch(text) {
		case "B":
			result = "진행전";
			break;
		case "P":
			result = "보류 ";
			break;
		case "I":
			result = "진행중";
			break;
		case "C":
			result = "완료 ";
			break;
		}
		return result;
	}
	/* 출력 형식을 적용한 날짜 */
	private String formattedDate(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
	}
	/* 특정일의 할일 건수 구하기 */
	private int itemCount(String[][] list, String compareValue) {
		int itemCount = 0;
		for(int idx=0; idx<list.length; idx++) {
			if(compareValue.equals(list[idx][0])) {
				itemCount += 1;
			}
		}
		return itemCount;
	}
		
	/* 서버로부터 전달받은 문자열을 할일이 등록되어있는 날짜로 분리하기 */
	private int[] getTaskDays(String serverData) {
		int[] taskDays = null;
		if(!serverData.equals("")) {
			String[] splitData = serverData.split(":");
			taskDays = new int[splitData.length];

			for(int idx=0; idx<taskDays.length; idx++) {
				taskDays[idx] = Integer.parseInt(splitData[idx]);
			}
		}
		return taskDays;
	}
	
	/* 특정 달의 할일이 등록되어있는 날짜를 특정 달의 달력에 표시하기 */
	private String makeCalendar(int[] taskDays, LocalDate today) {
		StringBuffer calendar = new StringBuffer();
		int dayOfWeek = LocalDate.of(today.getYear(), today.getMonthValue(), 1).getDayOfWeek().getValue();		
		int lastDay = today.lengthOfMonth();
		boolean isTask = false;
		
		dayOfWeek = (dayOfWeek==7)? 1:dayOfWeek+1;
		
		calendar.append("\n");
		calendar.append("    +++++++++++ Previous  [ " + today.format(DateTimeFormatter.ofPattern("yyyy. MM.")) + " ]  Next +++++++++++\n");
		calendar.append("        SUN    MON    TUE    WED    THU    FRI    SAT\n");
		calendar.append("      ");
		for(int dayIdx=1-(dayOfWeek-1); dayIdx<=lastDay; dayIdx++) {
			if(dayIdx<1) {
				calendar.append("       ");
			}else {
				calendar.append(dayIdx<10? "   " + dayIdx : "  " + dayIdx);
				if(taskDays != null) {
					for(int taskDayIdx=0; taskDayIdx<taskDays.length; taskDayIdx++) {
						if(dayIdx == taskDays[taskDayIdx]) {
							isTask = true;
							break;
						}
					}
				}
				calendar.append(isTask?"+  ": "   ");
				isTask = false;
			}
			calendar.append((dayIdx+(dayOfWeek-1))%7==0? "\n      " : "");
			calendar.append(dayIdx==lastDay? "\n": "");
		}
		
		return calendar.toString();
	}
}
