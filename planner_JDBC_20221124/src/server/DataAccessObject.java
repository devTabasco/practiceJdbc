package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;
import server.beans.ToDoBean;

/* Data File과의 통신 */
public class DataAccessObject {
	private PreparedStatement ps;
	private ResultSet rs;
	
	public DataAccessObject() {

	}

	/* CONNECTION CREATION */
	public Connection openConnection() {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.127:1521:xe", "changyong", "12345");
		} catch (ClassNotFoundException e) {
			System.out.println("Error : OracleDriver None");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error : Can not Connect");
			e.printStackTrace();
		}

		return connection;
	}

	/* CONNECTION Close */
	public void closeConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				if (this.rs != null && !this.rs.isClosed())
					this.rs.close();
				if (this.ps != null && !this.ps.isClosed())
					this.ps.close();
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Transaction Mgr */
	public void modifyTranStatus(Connection connection, boolean status) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.setAutoCommit(status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean setTransaction(boolean tran, Connection connection) {
		boolean result = false;
		try {
			if (tran) {
				connection.setAutoCommit(false);
				connection.commit();
				result = true;
			} else
				connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/* [SEL]ID의 존재여부 */
	public int isMemberId(Connection connection, MemberBean member) {
		rs = null;
		String query = "SELECT COUNT(*) AS ISMRID FROM MEMBERS WHERE MEMBER_ID = ?";
		int result = 0;

		try {
			this.ps = connection.prepareStatement(query);
			ps.setNString(1, member.getAccessCode());
			this.rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getInt("ISMRID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/* [SEL]ID와 PASSWORD의 일치여부 */
	public int isSame(Connection connection, MemberBean member) {
		this.rs = null;
		String query = "SELECT COUNT(*) AS ISACCESS FROM MEMBERS WHERE MEMBER_ID = ? AND PASSWORD = ?";
		int result = 0;

		try {
			this.ps = connection.prepareStatement(query);
			ps.setNString(1, member.getAccessCode());
			ps.setNString(2, member.getSecretCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getInt("ISACCESS");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/* [SEL]ACCESSSTATE */
	public int getAccessState(Connection connection, MemberBean member) {
		this.rs = null;
		String query = "SELECT SUM(AH_STATE) AS ISACCESS FROM ACCESSHISTORY WHERE AH_MRID = ?";
		int result = 0;

		try {
			this.ps = connection.prepareStatement(query);
			ps.setNString(1, member.getAccessCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getInt("ISACCESS");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/* [INS]ACCESSHISTORY */
	public int insAccessHistory(Connection connection, MemberBean member) {
		String query = "INSERT INTO AH(AH_MRID, AH_DATE, AH_STATE) " + "VALUES( ?, DEFAULT, ?)";
		int result = 0;

		try {
			this.ps = connection.prepareStatement(query);
			ps.setNString(1, member.getAccessCode());
			ps.setInt(2, member.getAccessType());
			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ToDoBean> getToDoDate(Connection connection, ToDoBean todo) {
		ArrayList<ToDoBean> toDoList = new ArrayList<ToDoBean>();
		ToDoBean tb = null;
		String query = "SELECT TO_CHAR(TD_DATE, 'YYYYMMDD') AS STARTDATE, "
				+ "TO_CHAR(TD_EDATE, 'YYYYMMDD') AS ENDDATE " + "FROM TODO "
				+ "WHERE TD_MRID = ? AND TO_CHAR(TD_DATE, 'YYYYMM') = ?";

		try {
			this.ps = connection.prepareStatement(query);
			this.ps.setNString(1, todo.getAccessCode());
			this.ps.setNString(2, todo.getStartDate());

			this.rs = this.ps.executeQuery();
			while (this.rs.next()) {
				tb = new ToDoBean();
				tb.setStartDate(this.rs.getNString("STARTDATE"));
				tb.setEndDate(this.rs.getNString("ENDDATE"));
				toDoList.add(tb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}

	public ArrayList<ToDoBean> getToDoList(Connection connection, ToDoBean todo) {
		ArrayList<ToDoBean> toDoList = new ArrayList<ToDoBean>();
		ToDoBean tb = null;
		
		String query = "SELECT * "
				+ "FROM TODOLIST "
				+ "WHERE MRID = ? "
				+ "AND (SUBSTR(STARTDATE, 1, 8) >= ? AND "
				+ "SUBSTR(ENDDATE, 1, 8) <= ?) " + (todo.getVisibleType() == null ? "" : "  AND ACTIVE = ?");
		

		try {
			this.ps = connection.prepareStatement(query);
			this.ps.setNString(1, todo.getAccessCode());
			this.ps.setNString(2, todo.getStartDate());
			this.ps.setNString(3, todo.getEndDate());
			if (todo.getVisibleType() != null) {
				this.ps.setNString(4, todo.getVisibleType());
			}

			this.rs = this.ps.executeQuery();
			while (this.rs.next()) {
				tb = new ToDoBean();
				tb.setAccessCode(this.rs.getNString("ACCESSCODE"));
				tb.setStartDate(this.rs.getNString("STARTDATE"));
				tb.setEndDate(this.rs.getNString("ENDDATE"));
				tb.setContents(this.rs.getNString("CONTENTS"));
				tb.setStatus(this.rs.getNString("STATUS"));
				tb.setActive(this.rs.getNString("ACTIVE").equals("A")? true:false);
				tb.setComments(this.rs.getNString("COMMENTS"));
				
				toDoList.add(tb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}
	
	/* 신규 
	 * 
	 * 작성일 : 221124
	 * 개발자 : 임창용
	 * 
	 * 메서드 기능 설명
	 * - 수정할 날짜를 선택받아 해당 날짜를 포함하는 TodoList 목록을 받아오는 Method.
	 * - 날짜 정보는 todoBean 담겨오고, DB에서 받아온 data는 ArrayList에 담아 return.
	 * 
	 * 파라미터 선정 이유
	 * - connection을 받아와 prepareStatement를 활용해야하기 때문
	 * - todoBean에 담긴 정보를 query에 담아주어야 하기 때문
	 * 
	 * return type 선정 이유
	 * - result set에 담긴 record를 bean에 담아 함께 전달해야하기에 ArrayList 사용
	 * 
	 * */
	public ArrayList<ToDoBean> getToDoList2(Connection connection, ToDoBean todo) {
		ArrayList<ToDoBean> toDoList = new ArrayList<ToDoBean>();
		ToDoBean tb = null;
		
		String query = "SELECT * "
				+ "FROM TODOLIST "
				+ "WHERE MRID = ? "
				+ "AND (SUBSTR(STARTDATE, 1, 8) <= ? AND "
				+ "SUBSTR(ENDDATE, 1, 8) >= ?) ";

		try {
			this.ps = connection.prepareStatement(query);
			this.ps.setNString(1, todo.getAccessCode());
			this.ps.setNString(2, todo.getStartDate());
			this.ps.setNString(3, todo.getStartDate());

			this.rs = this.ps.executeQuery();
			while (this.rs.next()) {
				tb = new ToDoBean();
				tb.setAccessCode(this.rs.getNString("ACCESSCODE"));
				tb.setStartDate(this.rs.getNString("STARTDATE"));
				tb.setEndDate(this.rs.getNString("ENDDATE"));
				tb.setContents(this.rs.getNString("CONTENTS"));
				tb.setStatus(this.rs.getNString("STATUS"));
				tb.setActive(this.rs.getNString("ACTIVE").equals("A")? true:false);
				tb.setComments(this.rs.getNString("COMMENTS"));
				
				toDoList.add(tb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}
	
	/* 신규 
	 * 
	 * 작성일 : 221124
	 * 개발자 : 임창용
	 * 
	 * 메서드 기능 설명
	 * - DB에 전달할 update 쿼리 생성 및 전달
	 * 
	 * 파라미터 선정 이유
	 * - connection을 받아와 prepareStatement를 활용해야하기 때문
	 * - todoBean에 담긴 정보를 query에 담아주어야 하기 때문
	 * - 원본 데이터를 Bean에 담고, 수정할 데이터는 modifyData라는 String에 담아 전달하기 위함
	 * 
	 * return type 선정 이유
	 * - update는 int type의 값(성공한 행의 갯수)을 return 하기 때문에 Method의 return type도 int형.
	 * 
	 * */
	public int updateTodo(Connection connection, ToDoBean todo, String modifyData) {
		//startDate=22222222,endDate=22222222
		String[] splitData = modifyData.split(",");
		StringBuffer query = new StringBuffer();
		String finalQuery = null;
		
		for(int i = 0;i<splitData.length;i++) {
			switch(splitData[i].split("=")[0]) {
			
			case "startDate":
				query.append(", TD_DATE = TO_DATE(?,'YYYYMMDDHH24MISS')");
				break;
			case "endDate":
				query.append(", TD_EDATE = TO_DATE(?,'YYYYMMDDHH24MISS')");
				break;
			case "contents":
				query.append(", TD_CONTENT = ?");
				break;
			case "status":
				query.append(", TD_STATE = ?");
				break;
			case "active":
				query.append(", TD_ACTIVATION = ?");
				break;
			case "comment":
				query.append(", TD_FEEDBACK = ?");
				break;
			}
			
		}
		
//		String query = "UPDATE TODO "
//				+ "SET " + "TD_FEEDBACK = 'Good!', TD_CONTENT = '영국여행' "
//				+ "WHERE TD_MRID = 'changyong' AND TD_DATE = TO_DATE('20221103000000') AND TD_EDATE = TO_DATE('20221117000000') AND TD_CONTENT = 'Good!'";
		int result = 0;
		
		
		finalQuery = "UPDATE TODO SET" + query.toString().substring(1) + 
				" WHERE TD_MRID = ? AND TO_CHAR(TD_DATE,'YYYYMMDDHH24MISS') = ? AND TO_CHAR(TD_EDATE, 'YYYYMMDDHH24MISS') = ? AND TD_CONTENT = ?";
		
		try {
			this.ps = connection.prepareStatement(finalQuery);
			for(int i = 0;i<splitData.length;i++) {
				switch(splitData[i].split("=")[0]) {
				case "startDate":
					ps.setNString(i+1, splitData[i].split("=")[1]);
					break;
				case "endDate":
					ps.setNString(i+1, splitData[i].split("=")[1]);
					break;
				case "contents":
					ps.setNString(i+1, splitData[i].split("=")[1]);
					break;
				case "status":
					ps.setNString(i+1, splitData[i].split("=")[1]);
					break;
				case "active":
					ps.setNString(i+1, splitData[i].split("=")[1]);
					break;
				case "comment":
					ps.setNString(i+1, splitData[i].split("=")[1]);
					break;
				}
				
			}
			ps.setNString(splitData.length+1, todo.getAccessCode());
			ps.setNString(splitData.length+2, todo.getStartDate());
			ps.setNString(splitData.length+3, todo.getEndDate());
			ps.setNString(splitData.length+4, todo.getContents());
			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/* 신규 [INS]TODOList 
	 * 
	 * 작성일 : 221124
	 * 개발자 : 임창용
	 * 
	 * 메서드 기능 설명
	 * - insert에 필요한 쿼리 생성 및 전달
	 * 
	 * 파라미터 선정 이유
	 * - connection을 받아와 prepareStatement를 활용해야하기 때문
	 * - todoBean에 담긴 정보를 query에 담아주어야 하기 때문
	 * 
	 * return type 선정 이유
	 * - insert 결과가 int형으로 넘어오기 때문에 그대로 return해주기 위함
	 * 
	 * */
	public int insertTodo(Connection connection, ToDoBean todo) {
		//TD_MRID, TD_DATE, TD_EDATE, TD_FEEDBACK, TD_CONTENT, TD_STATE, TD_ACTIVATION
		String query = "INSERT INTO TODO(TD_MRID, TD_DATE, TD_EDATE, TD_FEEDBACK, TD_CONTENT, TD_STATE, TD_ACTIVATION) VALUES(?, TO_DATE(?,'YYYYMMDDHH24MISS'), TO_DATE(?,'YYYYMMDDHH24MISS'), ?, ?, ?, ?)";
		int result = 0;
		
		try {
			this.ps = connection.prepareStatement(query);
			ps.setNString(1, todo.getAccessCode());
			ps.setNString(2, todo.getStartDate());
			ps.setNString(3, todo.getEndDate());
			ps.setNString(4, todo.getComments());
			ps.setNString(5, todo.getContents());
			ps.setNString(6, todo.getStatus());
			ps.setNString(7, todo.isActive()?"A":"I");
			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
