package client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server.ServerController;

public class UserApp {
	
	public UserApp() {
		frontController();
	}
	
	/* 클라이언트 화면 및 데이터 흐름 제어 */
	private void frontController() {
		Scanner scanner = new Scanner(System.in);
		String mainTitle = this.mainTitle(this.getToday(true, 0));
		String mainMenu = this.getMainMenu();
		boolean isLoop = true;
		String[] accessInfo = new String[2];
		boolean accessResult;
		ServerController ctl = new ServerController();
		TaskManagement task = null;
		String message = null;
		
		while(isLoop) {
			
			for(int idx=0;idx<accessInfo.length; idx++) {
				this.display(mainTitle);
				this.display(this.getAccessLayer(true, accessInfo[0]));
				accessInfo[idx] = this.userInput(scanner);
			}
			this.display(this.getAccessLayer(false, null));
			
			/* ClientData 생성 */
			String[] itemName = {"id", "password"};			
			/* 서버에 로그인 정보 전달 */
			accessResult = ctl.controller(this.makeClientData("1", itemName, accessInfo)).equals("1")? true: false;
			
			/* 서버로부터 받은 로그인 결과에 따른 화면 출력 */
			this.display(this.accessResult(accessResult));
			if(!accessResult) {
				/* 로그인 실패 */
				if(this.userInput(scanner).toUpperCase().equals("N")) {
					isLoop = false;
				}else {
					accessInfo[0] = null;
					accessInfo[1] = null;
				}	
			}else {
				/* 로그인 성공 */
				accessInfo[1] = null;
				while(isLoop) {
					String menuSelection = new String();
					while(true) {
						this.display(mainTitle);
						this.display(message != null? "[Message : " + message + " ]\n": "");
						this.display(mainMenu);
												
						menuSelection = this.userInput(scanner);
						/* 0~4 범위의 값이 아닌 경우 재입력 요구 */
						if(this.isInteger(menuSelection)) {
							if(this.isIntegerRange(Integer.parseInt(menuSelection), 0, 4)) {
								break;
							}else {
								message = "메뉴는 0~4 범위내로 선택해주세요";
							}
						}else {
							message = "메뉴는 숫자로 입력해주세요";
						}
					}
				   /* 0번 선택시 서버에 로그아웃 통보 후 프로그램 종료 */
					int month;
					if(menuSelection.equals("0")) {
						ctl.controller(this.makeClientData("-1", itemName, accessInfo));
						isLoop = false;
					}else{
						/* TaskManagement Class Call */
						task = new TaskManagement();
						String[] subItemName = {"startDate", "endDate", "visibleType"};
						if(menuSelection.equals("1")) {
							/* Service Code 생성 */
							String[] userSelection = { 
									"    +++++++++++++++++++++++++++++++++++++ Start Date : ", 
									"    +++++++++++++++++++++++++++++++++++++   End Date : ", 
									"    +++++++++++++++++++++++++ Total  Enable  Disable : "};
							String[] userInput = new String[3];
							month = 0;							
							while(true) {
								menuSelection = "11";
								
								this.display(mainTitle);
								this.display(message!=null? "   [ " + message + " ]\n": "");
								this.display(task.taskController(
										Integer.parseInt(menuSelection), accessInfo[0], month).toString());
								
								/* Start Date */
								char direction = 'p';
								for(int idx=0; idx<userInput.length; idx++) {
									this.display(userSelection[idx]);
									userInput[idx] = this.userInput(scanner).toUpperCase();
									
									if(this.isInteger(userInput[idx])) {
										if(!this.isIntegerRange(this.convertToInteger(userInput[idx]), 1, this.getLengthOfMonth(month))){
											message = "검색 가능한 일자가 아닙니다.";
											direction = 'c';
											break;
										}else {
											message = null;
											direction = 'p';
										}
									}else {
										if(userInput[idx].equals("P") || userInput[idx].equals("N")) {
											month += userInput[idx].equals("P")? -1 : 1;
											direction = 'c';
											break;
										}else if(userInput[idx].equals("Q")){
											direction = 'b';
											break;
										}else if(idx==userInput.length-1) {
											if(userInput[idx].equals("T") || userInput[idx].equals("E") || userInput[idx].equals("D")){
												message = null;
												direction = 'p';
											}else {
												message = "부적합한 메뉴입니다.";
												direction = 'c';
											}
										}else {
											message = "부적합한 메뉴입니다.";
											direction = 'c';
											break;
										}
										
									}
								}
								
								if(direction == 'b') break;
								if(direction == 'c') continue;
								
								userInput[0] = this.getDate(this.getToday(false, month), userInput[0], "");
								userInput[1] = this.getDate(this.getToday(false, month), userInput[1], "");
																
								/* STEP 2 */
								String clientData = this.makeClientData("12", subItemName, userInput)+ "&accessCode=" + accessInfo[0];
								this.display(mainTitle);
								this.display(task.taskController(clientData).toString().split("/")[0]);
								this.display("\n  ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷ ▶ ▷  More / Quit : ");
								if(this.userInput(scanner).toUpperCase().equals("Q")) break;
							}
						}else if(menuSelection.equals("2")){
							/* 신규 
							 * 
							 * 작성일 : 221124
							 * 개발자 : 임창용
							 * 메서드 기능설명 : Main 화면에서 2를 선택했을 때, 등록 기능 제공
							 * 
							 * */
							String[] userSelection = { 
									"    ++++++++++++++++++++++++++++++++++++ Select Date : ", 
									"    ─────────────────────────────────── Confirm(Y/N) : "};
							String[][] item = {{"TIME Schedule(HHmmss)      : ", null}, {"ITEM CONTENT(THINGS TO DO) : ", null}, {"END DATE(yyyyMMddHHmmss)   : ", null}};
							message = null;
							menuSelection += "1";
							month = 0;
							int displayIdx = 0;
							int idx = 0; // 반복문에서 사용한 idx값을 외부에서 사용하기 위해
							String[] selectValue = {null, null};
							
							while(true) {
								this.display(mainTitle);
								this.display(message!=null? "   [ " + message + " ]\n": "");
								this.display(task.taskController(
									Integer.parseInt(menuSelection), accessInfo[0], month).toString());
								this.display(userSelection[0]);
																
								if(selectValue[0] == null || selectValue[0].equals("P") || selectValue[0].equals("N")) {
									selectValue[0] = this.userInput(scanner).toUpperCase();
									
									if(selectValue[0].equals("Q")) {
										message = null;
										break;
									}else if(selectValue[0].equals("P") || selectValue[0].equals("N")){
										month += selectValue[0].equals("P")? -1:1;
										continue;
									}
									
								}else {
									this.display(selectValue[0] + "\n");
								}
								
								if(this.isInteger(selectValue[0])) {
									if(this.isIntegerRange(this.convertToInteger(selectValue[0]), 1, this.getLengthOfMonth(month))) {
										this.display("\n");
										String date = this.getDate(this.getToday(false, month), selectValue[0], null);
										this.display("     [ " + date  + " ]  Item Registration\n");
										this.display("     ───────────────────────────────────────────────────\n");
										
										for(idx=0; idx<item.length; idx++) {
											this.display("      " + item[idx][0]);
											if(item[idx][1]==null) {
												item[idx][1] = ((idx == 0)? date:"") + this.userInput(scanner).toUpperCase();
												if(item[idx][1].equals("Q")) break;
											}else{
												this.display(item[idx][1] + "\n" );
											}
											
											if(idx != 1) {
												if(!this.isDate(item[idx][1])) {
													item[idx][1] = null;
													message = "숫자로 입력해주세요";
													break;
												}else {
													item[idx][1] = this.convertToDate(item[idx][1]);
												}
											}
										}
										/* 사용자 입력이 완료되지 않은 상태 */
										if(idx != item.length) {
											if(item[idx][1] == null) continue;
											if(item[idx][1].equals("Q")) break;
										}
										
										displayIdx += 1;
										this.display(userSelection[displayIdx]);
										
										// Q Y N
										selectValue[1] = this.userInput(scanner).toUpperCase();
										if(selectValue[1].equals("Q") || selectValue[1].equals("N")) {
											message = null;
											for(idx=0;idx<item.length; idx++) {
												item[idx][1] = null;
											}
											idx = 0;
											if(selectValue[1].equals("N")) {
												displayIdx = 0;
												continue;
											}
											if(selectValue[1].equals("Q")) break;
										}else if(selectValue[1].equals("Y")) {
											String[] regItemName = {"startDate", "endDate", "content", "accessCode"};
											String[] regItemValue = {item[0][1], item[2][1], item[1][1], accessInfo[0]};
											//serviceCode=22&startDate=20221103121212&endDate=20221124000000&content=DDD&accessCode=changyong
											menuSelection = (Integer.parseInt(menuSelection) + 1) + "";
											if(ctl.controller(this.makeClientData(menuSelection, regItemName, regItemValue)).equals("등록성공")) break;
											message = null;
											continue;
											
										}else  {
											message = "부적합한 명령입니다.";
											selectValue[1] = null;
											displayIdx = 0;
											idx = 0;
											continue;
										}
										
									}else {
										message = "검색 가능한 일자가 아닙니다."; 
										idx = 0;
										continue;
									}
								} else {
									message = "숫자로 입력해주세요.";
									idx = 0;
									continue;
								}
								
								// break 
							}
							
						}else if(menuSelection.equals("3")){
							/* 신규 
							 * 
							 * 작성일 : 221124
							 * 개발자 : 임창용
							 * 메서드 기능 설명 : Main화면에서 3을 선택했을 때, 수정기능 제공
							 * 
							 * */
							menuSelection += "1";
							String[] selectValue = {null, null};
							month = 0;
							String[] userSelection = { 
									"    ++++++++++++++++++++++++++++++++++++ Select Date : ", 
									"    ─────────────────────────────────── Confirm(Y/N) : "};
							String selectList;
							
							while(true) {
								
								this.display(mainTitle);
								this.display(message!=null? "   [ " + message + " ]\n": "");
								this.display(task.taskController(
										Integer.parseInt(menuSelection), accessInfo[0], month).toString());
								this.display(userSelection[0]);
								
								if(selectValue[0] == null || selectValue[0].equals("P") || selectValue[0].equals("N")) {
									selectValue[0] = this.userInput(scanner).toUpperCase();
									
									if(selectValue[0].equals("Q")) {
										message = null;
										break;
									}else if(selectValue[0].equals("P") || selectValue[0].equals("N")){
										month += selectValue[0].equals("P")? -1:1;
										continue;
									}
									
								}else {
									this.display(selectValue[0] + "\n");
								}
								
								/* STEP 2 */
									selectValue[0] = this.getDate(this.getToday(false, month), selectValue[0], "");
									String clientData = this.makeClientData("13", subItemName, selectValue)+ "&accessCode=" + accessInfo[0];
									this.display(mainTitle);
									this.display(task.taskController(clientData).toString().split("/")[0]);
									this.display("   ─────────────────────────────────────── Select Num : ");
									selectList = userInput(scanner);
									ctl.controller(this.modifyListView(scanner, "accessCode="+accessInfo[0]+"&" + ((task.taskController(clientData).toString().split("/").length==2)?
											task.taskController(clientData).toString().split("/")[1]
													:task.taskController(clientData).toString().split("/")[1].split(":")[Integer.parseInt(selectList)-1])));
									this.display(task.taskController(clientData).toString().split("/")[0]);
									this.display("   ───────────────────────────────────────────────────\r\n"
											+ "   ─────────────────────────────────────── Check : ");
									selectValue[0] = this.userInput(scanner).toUpperCase();
									if(selectValue[0].equals("C")) {
										selectValue[0] = null;
										scanner.nextLine();
										continue;									
									}else if(selectValue[0].equals("Q")) break;									
								}
							
						}else if(menuSelection.equals("4")){
							menuSelection += "1";
							
						}
					}
				}
			}
		}
		
		this.display("\n\n  x-x-x-x-x-x-x-x-x-x- 프로그램을 종료합니다 -x-x-x-x-x-x-x-x-x-x");	
		scanner.close();
	}
	
	/* 신규 
	 * 
	 * 작성일 : 221124
	 * 개발자 : 임창용
	 * 메서드 기능설명 : 수정에 필요한 화면출력, 수정할 데이터 수집, ClientData생성
	 * 파라미터 선정 이유
	 * - userInput을 Method안에서 받기 위해 scanner 필요
	 * - 수정할 recode에 대한 원본 데이터 뒤에 수정할 데이터를 붙여 clientData로 생성해주기 위해 originData 필요
	 * Return Type 선정 이유
	 * - clientData를 String type으로 리턴하기 위함
	 * 
	 * */
	private String modifyListView(Scanner scanner, String originData) {
		String selectMenu = null;
		String data = null;
		String result = null;
		StringBuffer buffer = new StringBuffer();
		this.display("	Sub Menu ------------------------------------------\r\n"
				+ "      Item   Start   End    Process   eNable  Feedback \n");
		this.display("    -------------------------------------- Select : ");
		
		while(true) {
			selectMenu = userInput(scanner);
			if(selectMenu.toUpperCase().equals("S")) {
				this.display("      [Start Date(YYYYMMDDhhmmss)] : ");
				data = userInput(scanner);
				if(isInteger(data)) {
					if(data.length()>=8) {
						buffer.append(",startDate="+convertToDate(data));						
					}else {
						this.display("[Error : 날짜형식을 맞춰주세요.]");
					}
				}else {
					this.display("[Error : 숫자만 입력해주세요.]");
				}
			}else if(selectMenu.toUpperCase().equals("E")) {
				this.display("      [End Date(YYYYMMDDhhmmss)] : ");
				data = userInput(scanner);
				if(isInteger(data)) {
					if(data.length()>=8) {
						buffer.append(",endDate="+convertToDate(data));						
					}else {
						this.display("[Error : 날짜형식을 맞춰주세요.]");
					}
				}else {
					this.display("[Error : 숫자만 입력해주세요.]");
				}
			}else if(selectMenu.toUpperCase().equals("I")) {
				this.display("      [Item(Contents)] : ");
				data = userInput(scanner);
				buffer.append(",contents="+data);
			}else if(selectMenu.toUpperCase().equals("P")) {
				this.display("      [Process(Status) B,D,E,P] : ");
				data = userInput(scanner);
				buffer.append(",status="+data);
			}else if(selectMenu.toUpperCase().equals("N")) {
				this.display("      [eNable(isActive)] A, I: ");
				data = userInput(scanner);
				buffer.append(",active="+data);
			}else if(selectMenu.toUpperCase().equals("F")) {
				this.display("      [Feedback] : ");
				data = userInput(scanner);
				buffer.append(",comment="+data);
			}else if(selectMenu.toUpperCase().equals("Q")) {
				result = "serviceCode=14&"+originData+"&"+buffer.toString().substring(1);
				break;
			}
			
			this.display("    ________________________________________ More : ");
		}
		
		return result;
		
	}
	
	/* 정수 변환여부 체크 */
	private boolean isInteger(String value) {
		boolean isResult = true;
		try {
			Integer.parseInt(value);
		}catch(Exception e){
			isResult = false;// e.printStackTrace();
		}
		return isResult;
	}
	
	/* 문자 >> 정수 변환 */
	private int convertToInteger(String value) {
		return Integer.parseInt(value);
	}

	/* 정수의 범의 체크 */
	private boolean isIntegerRange(int value, int starting, int last) {
		return (value >= starting && value <= last)? true: false;
	}
	
	private String makeClientData(String serviceCode, String[] item, String[] userData) {
		StringBuffer clientData = new StringBuffer();
		clientData.append("serviceCode=" + serviceCode);
		for(int idx=0; idx<userData.length; idx++) {
			if(userData[idx] != null) {
				clientData.append("&");
				clientData.append(item[idx] + "=" + userData[idx]);
			}
		}
		return clientData.toString();
	}
	
	/* 프로그램 메인 타이틀 제작 */
	private String mainTitle(String date) {
		StringBuffer title = new StringBuffer();
		
		title.append("\n\n\n");
		title.append("  __________________________________________________________\n\n");
		title.append("     ◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷\n");
		title.append("        T A S K\n"); 
		title.append("        M A N A G E R                   " + date + "\n");
		title.append("     ◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷ TEAM : 김민규, 임창용, 염은진, 박건호\n");
		title.append("  __________________________________________________________\n");

		return title.toString();
	}
	
	private String getAccessLayer(boolean isItem, String accessCode) {
		StringBuffer accessLayer = new StringBuffer();
		
		if(isItem) {
			accessLayer.append("\n");
			accessLayer.append("     Access ++++++++++++++++++++++++++++++++++++++++++\n");
			accessLayer.append("     +        AccessCode          SecretCode\n");
			accessLayer.append("     + --------------------------------------------\n");
			accessLayer.append("     +         " + ((accessCode!=null)? accessCode+"\t\t": ""));
		}else {
			accessLayer.append("     +++++++++++++++++++++++++++++++++++ Connecting...");
		}
	    return accessLayer.toString();
	}
	
	/* 서버응답에 따른 로그인 결과 출력 */
	private String accessResult(boolean isAccess) {
		StringBuffer accessResult = new StringBuffer();
		
		accessResult.append("\n     >>>>>>>>>>>>>>>>>>>>>>>>> ");
		if(isAccess) {
			accessResult.append("Successful Connection\n"); 
		    accessResult.append("     Move after 2 sceonds...");
		}else {
			accessResult.append("Connection Failed\n");
			accessResult.append("     _______________________________ Retry(y/n) ? ");
		}
		
		return accessResult.toString(); 
	}
	
	/* 메인페이지 출력 */
	private String getMainMenu() {
		StringBuffer mainPage = new StringBuffer();
		
		mainPage.append("\n");
		mainPage.append("     [ MENU SELECTION ] __________________________________\n\n");
		mainPage.append("       1. TASK LIST		2. TASK REGISTRATION\n");
		mainPage.append("       3. MODIFY TASK		4. TASK STATS\n");
		mainPage.append("       0. DISCONNECT    \n");
		mainPage.append("     ________________________________________________ : ");
		   
		return mainPage.toString();
	}
	
	/* 날짜시간 출력 : LocalDateTime Class + DateTimeFormatter Class */
	private String getToday(boolean isDate, int month) {
		String pattern = (isDate)? "yyyy. MM. dd.": "yyyyMM";
		return LocalDateTime.now().plusMonths(month).format(DateTimeFormatter.ofPattern(pattern)); 
	}
	
	/* 선택한 일자에 해당하는 날짜 구하기 */
	private String getDate(String yearMonth, String day, String time) {
		String date = (day.length() == 1)? yearMonth+"0"+day: yearMonth+day;
		return date += (time == null)? "" : time;
	} 
	
	/* 날짜 변환 가능 여부 */
	private boolean isDate(String value) {
		boolean result = false;
		int year, month, day=1, hour=0, minute=0, second=0;
		int length = value.length(); 
		if( (length >= 8 && length <= 14) && length%2 == 0) {
			year = Integer.parseInt(value.substring(0, 4));
			month = Integer.parseInt(value.substring(4, 6));
			if(length == 8) day = Integer.parseInt(value.substring(6));
			if(length > 8) day = Integer.parseInt(value.substring(6, 8));
			if(length == 10) hour = Integer.parseInt(value.substring(8));
			if(length > 10) hour = Integer.parseInt(value.substring(8, 10));
			if(length == 12) minute = Integer.parseInt(value.substring(10));
			if(length > 12) minute = Integer.parseInt(value.substring(10, 12));
			if(length == 14) second = Integer.parseInt(value.substring(12));
			
			try {
				LocalDateTime.of(year, month, day, hour, minute, second);
				result = true;
			}catch(Exception e) {e.printStackTrace();}
		}
		
		return result;
	}
	
	/* 날짜 형식 변환(yyyyMMdd, yyyyMMddHH, yyyyMMddHHmm, yyyyMMddHHmmss) */
	private String convertToDate(String value) {
		String formattedDate = value;
		
		switch(value.length()) {
		case 8:
			formattedDate += "000000";
			break;
		case 10:
			formattedDate += "0000";
			break;
		case 12:
			formattedDate += "00";
			break;
		}
		
		return formattedDate;
	}
	
	/* 선택한 달의 일수 구하기 */
	private int getLengthOfMonth(int month) {
		return LocalDate.now().plusMonths(month).lengthOfMonth();
	}
	/* 사용자 입력 */
	private String userInput(Scanner scanner) {
		return scanner.next();
	}
	/* 화면 출력 */
	private void display(String text) {
		System.out.print(text);
	}
}
