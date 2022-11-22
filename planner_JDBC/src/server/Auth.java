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
		DataAccessObject dao = new DataAccessObject();
		boolean accessResult = false;
		Connection connection = null;
		
		connection =  dao.createConnection();
		
		//id존재 여부 파악
		if(this.convertToBool(dao.isMemberId(member))){
			if(this.convertToBool(dao.isSame(member))){
				//Access History Write(type = 1)
				member.setAccessType(1);
				if(this.convertToBool(dao.insertAcessHistory(member))){
					accessResult = true;				
				}
			}
		}
		
		//transaction Management
		dao.closeConnection(connection);
		
		return accessResult;
	}
	
	public boolean convertToBool(int result) {
		return result>0?true:false;
	}
	
	public void accessOut(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		AccessHistoryBean history = (AccessHistoryBean)this.setBean(clientData);
		history.setFileIdx(1);
		history.setAccessDate(this.getDate(false));
		history.setAccessType(-1);
		
		dao.writeAccessHistory(history);
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
			object = new AccessHistoryBean();
			((AccessHistoryBean)object).setAccessCode(splitData[1].split("=")[1]);
			break;
		case "1":
			object = new MemberBean();
			((MemberBean)object).setAccessCode(splitData[1].split("=")[1]);
			((MemberBean)object).setSecretCode(splitData[2].split("=")[1]);

			break;
		}
				
		return object;
	} 
	
	/* AccessCode 존재여부 판단 */
	private boolean compareAccessCode(String code, ArrayList<MemberBean> memberList) {
		boolean result = false;
		for(MemberBean member : memberList) {
			if(code.equals(member.getAccessCode())) {
				result = true;
				break;
			}
		}

		return result;
	}
	
	/* AccessCode와 SecretCode의 비교 */
	private boolean isAuth( MemberBean member, ArrayList<MemberBean> memberList) {
		boolean result = false;
		for(MemberBean memberInfo : memberList) {
			if(member.getAccessCode().equals(memberInfo.getAccessCode())) {
				if(member.getSecretCode().equals(memberInfo.getSecretCode())) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
}
