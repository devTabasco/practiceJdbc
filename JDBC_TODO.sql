/* JDBC(JAVA DATA BASE CONNECTION)
   JAVA  ---------------------------------------- ORACLE
     ┖     JDBC ++++++++++++++++++++++++ OJDBC    ┚                
     
   CONNECT TO ORACLE
   P1. JDBC DRIVER LOAD     :  
                Class.forName(oracle.jdbc.driver.OracleDriver);
                
   P2. Connection Creation  :  Using DriverManager + with DB Info
                url : jdbc:oracle:thin:@db_Addr:db_port:db_serviceName
                
                Connection DriverManager.getConnection(url, name, pwd);
                
   P3-1. Using Connection + with DML || Query
       Statement Def            : Speed↑  Injection Attack↑  Development↓
     + PreparedStatement Def    : Speed↓  Injection Attack↓  Development↑
       CallableStatement Def    : Speed↑  Injection Attack↓  Development↑
   + P3-2. Param Setting
   
   P4. DML Execution        : PreparedStatement.executeUpdate();
       Query Execution      : PreparedStatement.executeQuery();
       
   P5. Result Processing    :
     5-1.  DML      -> int
     5-2.  Query    -> ResultSet 
                        --> Loop record fetch  
                            --> extract column data >> Set Bean
                            --> Add to List 

   P6. Close
     5-1 >>                    PreparedStatement Close >> Connection Close
     5-2 >> ResultSet Close >> PreparedStatement Close >> Connection Close
     
*/

CREATE OR REPLACE VIEW TODOLIST
AS
 SELECT TD_MRID AS MRID,
        TO_CHAR(TD_DATE, 'YYYYMMDD') AS ACCESSCODE, 
        TO_CHAR(TD_DATE, 'YYYYMMDDHH24MISS') AS STARTDATE, 
        TO_CHAR(TD_EDATE, 'YYYYMMDDHH24MISS') AS ENDDATE, 
        TD_CONTENT AS CONTENTS, 
        TD_STATE AS STATUS, 
        TD_ACTIVATION AS ACTIVE, 
        TD_FEEDBACK AS COMMENTS 
 FROM TODO;
 GRANT SELECT ON changyong.TODOLIST TO tododba;
 
 SELECT * 
 FROM TODOLIST
 WHERE MRID = 'changyong' 
   AND (SUBSTR(STARTDATE, 1, 8) >= '20221224' AND 
        SUBSTR(ENDDATE, 1, 8) <= '20221225');

 FROM TODOLIST 
 WHERE ACCESSCODE = 'HOON' AND SUBSTR(STARTDATE, 1, 6) = '202211' 
   AND ACTIVE = 'A';
/* TODO TABLE PK 수정*/
ALTER TABLE TODO
DROP CONSTRAINT TD_MRID_DATE_PK;
ALTER TABLE TODO
ADD CONSTRAINT TD_MRID_DATE_EDATE_CONTENT_PK 
    PRIMARY KEY(TD_MRID, TD_DATE, TD_EDATE, TD_CONTENT);

ALTER TABLE ACCESSHISTORY
DROP CONSTRAINT AH_MRID_DATE_PK;
ALTER TABLE ACCESSHISTORY
ADD CONSTRAINT AH_MRID_DATE_STATE_PK PRIMARY KEY(AH_MRID, AH_DATE, AH_STATE);

delete
from ACCESSHISTORY;
commit;

INSERT INTO TODO(TD_MRID, TD_DATE, TD_EDATE, TD_FEEDBACK, TD_CONTENT, TD_STATE, TD_ACTIVATION) VALUES('changyong', TO_DATE('20221224000000'), TO_DATE('20221224000000'), 'NONE', '테스트', 'B', 'A');
commit;

ALTER TABLE ACCESSHISTORY DROP CONSTRAINT AH_MRID_DATE_PK;
commit;

SELECT * 
FROM TODOLIST
WHERE MRID = 'changyong' AND (SUBSTR(STARTDATE, 1, 8) <= '20221224' AND SUBSTR(ENDDATE, 1, 8) >= '20221224');

select *
from TODO;

UPDATE TODO
SET TD_DATE = '20221103000000'
;
select *
from TODO
WHERE TD_MRID = 'changyong' AND TO_CHAR(TD_DATE,'YYYYMMDDHH24MISS') = '20221103000000' AND TO_CHAR(TD_EDATE, 'YYYYMMDDHH24MISS') = '20221117000000' AND TD_CONTENT = '영국여행';
WHERE TD_MRID = 'changyong' AND TD_DATE = TO_DATE('20221102000000') AND TD_EDATE = TO_DATE('20221117000000') AND TD_CONTENT = '영국여행';
