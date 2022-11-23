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
}
