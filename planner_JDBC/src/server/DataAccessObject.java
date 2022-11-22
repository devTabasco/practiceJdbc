package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;
import server.beans.ToDoBean;

/* Data File과의 통신 */
public class DataAccessObject {
	
	private PreparedStatement ps;
	
	String[] fileInfo = {"D:\\JSFramework\\planner\\src\\database\\MEMBERS.txt", 
			"D:\\JSFramework\\planner\\src\\database\\ACCESSHISTORY.txt",
			"D:\\JSFramework\\planner\\src\\database\\TODO.txt"};
	public DataAccessObject() {
		
	}
	
	//connection creation
	
	public Connection createConnection() {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("idbc:oracle:thin:@192.168.0.127:1521:xe","changyongdev","1234");
		} catch (ClassNotFoundException e) {
			System.out.println("Error : OracleDriver None");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error : Can not Connect");
			e.printStackTrace();
		}
		
		return connection;
	}
	
	//connection close
	
	public void closeConnection(Connection connection) {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//transaction MGR
	public boolean transaction(boolean transaction, Connection connection) {
		boolean result = false;
		try {
			if(transaction) {
				connection.commit();
				result = true;
			}else {
				connection.rollback();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//select ID
	public int isMemberId(MemberBean member) {
		int result = 0;
		
		return result;
	}
	//select ID / PW check
	public int isSame(MemberBean member) {
		int result = 0;
		
		return result;
	}
	
	//AccessHistory insert
	public int insertAcessHistory(MemberBean member) {
		int result = 0;
		
		return result;
	}
	
	/* 회원정보 수집 */
	public ArrayList<MemberBean> readDatabase(int fileIdx) {
		String line;
		ArrayList<MemberBean> memberList = null;
		MemberBean member;
		BufferedReader buffer = null;
		
		
		try {
			buffer = new BufferedReader(new FileReader(
							new File(fileInfo[fileIdx])));
			memberList = new ArrayList<MemberBean>();
			 
			while((line = buffer.readLine())!= null) {
				String[] record = line.split(",");
				member = new MemberBean();
				member.setAccessCode(record[0]);
				member.setSecretCode(record[1]);
				member.setUserName(record[2]);
				member.setPhoneNumber(record[3]);
				member.setActivation(Integer.parseInt(record[4]));
				memberList.add(member);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("파일이 존재하지 않습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			memberList = null;
			System.out.println("파일로부터 데이터를 가져올 수 없습니다.");
			e.printStackTrace();
		}finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return memberList;
	}
	
	/* 접속기록 작성 */
	public boolean writeAccessHistory(AccessHistoryBean accessInfo) {
		boolean result = false;
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(new File(this.fileInfo[accessInfo.getFileIdx()]), true));
			writer.write(accessInfo.getAccessCode());
			writer.write(",");
			writer.write(accessInfo.getAccessDate());
			writer.write(",");
			writer.write(accessInfo.getAccessType()+"");
			writer.newLine();
			writer.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {writer.close();} catch (IOException e) { e.printStackTrace();}
		}	
		return result;
	}
	
	/* TODO List 읽어오기 */
	public ArrayList<ToDoBean> getToDoList(ToDoBean searchInfo) {
		ArrayList<ToDoBean> dayList = null;
		ToDoBean toDo = null;
		String line;
		BufferedReader buffer = null;
		int date, recordCount=1;
		int[] dateRange = new int[2];
		
		LocalDate userDate = LocalDate.of(Integer.parseInt(searchInfo.getStartDate().substring(0, 4)), 
				Integer.parseInt(searchInfo.getStartDate().substring(4)), 1);
		
		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIdx()])));
			while((line=buffer.readLine()) != null) {
				if(recordCount == 1) dayList = new ArrayList<ToDoBean>();
				
				String[] record = line.split(",");
				date = Integer.parseInt(searchInfo.getStartDate());
				dateRange[0] = Integer.parseInt(record[1].substring(0, 8));
				dateRange[1] = Integer.parseInt(record[2].substring(0, 8));
								
				if(date > dateRange[0]/100) dateRange[0] = Integer.parseInt(date + "01");
				if(date < dateRange[1]/100) {
					dateRange[1] = Integer.parseInt(date + "" + userDate.lengthOfMonth());
				}
				
				for(int idx=dateRange[0]; idx<=dateRange[1]; idx++) {
					if(recordCount != 1) {
						if(this.duplicateCheck(dayList, idx+"")) {
							continue;
						}
					}
					toDo = new ToDoBean();
					toDo.setStartDate(idx+"");
					dayList.add(toDo);
				}
				
				recordCount++;
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (buffer != null) buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return dayList;
	}
	
	/* 특정기간의 할일 목록 가져오기 */
	public ArrayList<ToDoBean> getToDoList2(ToDoBean searchInfo){
		ArrayList<ToDoBean> toDoList = new ArrayList<ToDoBean>();
		ToDoBean toDo = null;
		BufferedReader reader = null;
		String line = null;
		String[] record = null;
		
		try {
			 reader = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIdx()])));
			
			while((line=reader.readLine()) != null) {
				record = line.split(",");
				/* 조건 적용 1. accessCode 2. visibleType */
				if(!record[0].equals(searchInfo.getAccessCode())) continue;
				if(searchInfo.getVisibleType() != null) {
					if(!record[5].equals(searchInfo.getVisibleType())) continue;
				}
				/* 선택날짜가 등록된 할 일의 날짜범위에 포함 여부 및 추출 */
				for(int searchDate=Integer.parseInt(searchInfo.getStartDate().substring(0, 8)); 
						searchDate<=Integer.parseInt(searchInfo.getEndDate().substring(0, 8)); 
						searchDate++) {
					for(int toDoDate=Integer.parseInt(record[1].substring(0, 8));
							toDoDate<=Integer.parseInt(record[2].substring(0, 8)); 
							toDoDate++) {
						if(searchDate == toDoDate) {
							toDo = new ToDoBean();
							toDo.setAccessCode(searchDate + "");
							toDo.setStartDate(record[1]);
							toDo.setEndDate(record[2]);
							toDo.setContents(record[3]);
							toDo.setStatus(record[4]);
							toDo.setActive(record[5].equals("A")? true: false);
							toDo.setComments(record[6]);
							toDoList.add(toDo);
						}
					}
				} 
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("파일이 없어요~");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("입출력이 안돼요~");
			e.printStackTrace();
		} finally {
			if (reader != null)	try {reader.close();} catch (IOException e) {e.printStackTrace();}	
		}
		
		return toDoList;
	}
	
	private boolean duplicateCheck(ArrayList<ToDoBean> dayList, String compareValue) {
		boolean result = false;
		for(int listIdx=0; listIdx<dayList.size(); listIdx++) {
			if(compareValue.equals(dayList.get(listIdx).getStartDate())) {
				result = true;
				break;
			}
		}
		return result;
	}
}
