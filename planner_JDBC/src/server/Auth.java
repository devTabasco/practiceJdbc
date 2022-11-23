package server;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;

/* 로그인, 로그아웃, 접속로그기록 */
public class Auth {
	
	public Auth() {
		
	}
		
	public boolean accessCtl(String clientData) {
		MemberBean member = (MemberBean)this.setBean(clientData);
		boolean accessResult = false;
		DataAccessObject dao = new DataAccessObject();
		Connection connection = null;
		
		/* Connection Open & setAutoCommit(false)*/
		connection = dao.openConnection();
		dao.modifyTranStatus(connection, false);
		/* p1 : id존재여부 */
		if(this.convertToBool(dao.isMemberId(connection,member))) {
			/* p2 : id와 password 일치여부 */
			if(this.convertToBool(dao.isSame(connection, member))) {
				/* p3 : AccessHistory 기록(1) */
				if(!this.convertToBool(dao.getAccessState(connection, member))) {
					member.setAccessType(1);
					accessResult = dao.setTransaction(this.convertToBool(dao.insAccessHistory(connection, member)), connection);
				}else {
					member.setAccessType(-1);
					if(this.convertToBool(dao.insAccessHistory(connection, member))) {
						member.setAccessType(1);
						accessResult = dao.setTransaction(this.convertToBool(dao.insAccessHistory(connection, member)), connection);
					}
				}
			}	
		}
		
		/* TRANSACTION MANAGEMENT */
		dao.modifyTranStatus(connection, true);
		dao.closeConnection(connection);
			
		return accessResult;
	}
	
	public void accessOut(String clientData) {
		MemberBean member = (MemberBean)this.setBean(clientData);
		DataAccessObject dao = new DataAccessObject();
		Connection connection = null;
		
		/* Connection Open & setAutoCommit(false)*/
		connection = dao.openConnection();
		dao.modifyTranStatus(connection, false);
		
		/* 아이디 존재여부 */
		if(this.convertToBool(dao.isMemberId(connection, member))) {
			/* p2 : 로그인 상태 여부 */
			if(this.convertToBool(dao.getAccessState(connection, member))) {
				/* p3 : AccessHistory 기록(1) */
				member.setAccessType(-1);
				dao.setTransaction(this.convertToBool(dao.insAccessHistory(connection, member)), connection);
			}
				
		}
		
		/* TRANSACTION MANAGEMENT */
		dao.modifyTranStatus(connection, true);
		dao.closeConnection(connection);
	}
	
	private boolean convertToBool(int value) {
		return value>0?true:false;
	}
	
	private String getDate(boolean isDate) {
		String pattern = (isDate)? "yyyyMMdd": "yyyyMMddHHmmss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}
	
	private Object setBean(String clientData) {
		Object object = null;
		String[] splitData = clientData.split("&");
		switch(splitData[0].split("=")[1]) {
		case "-1":
			object = new MemberBean();
			((MemberBean)object).setAccessCode(splitData[1].split("=")[1]);
			break;
		case "1":
			object = new MemberBean();
			((MemberBean)object).setAccessCode(splitData[1].split("=")[1]);
			((MemberBean)object).setSecretCode(splitData[2].split("=")[1]);

			break;
		}
				
		return object;
	} 
	
	
}
