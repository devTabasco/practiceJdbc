/* JDBC(JAVA DATA BASE CONNECTION)
   JAVA  ---------------------------------------- ORACLE
     ¦Å     JDBC ++++++++++++++++++++++++ OJDBC    ¦Ã                
     
   CONNECT TO ORACLE
   P1. JDBC DRIVER LOAD     :  
                Class.forName(oracle.jdbc.driver.OracleDriver);
                
   P2. Connection Creation  :  Using DriverManager + with DB Info
                url : jdbc:oracle:thin:@db_Addr:db_port:db_serviceName
                
                Connection DriverManager.getConnection(url, name, pwd);
                
   P3-1. Using Connection + with DML || Query
       Statement Def            : Speed¡è  Injection Attack¡è  Development¡é
     + PreparedStatement Def    : Speed¡é  Injection Attack¡é  Development¡è
       CallableStatement Def    : Speed¡è  Injection Attack¡é  Development¡è
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