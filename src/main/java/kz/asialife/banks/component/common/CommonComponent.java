package kz.asialife.banks.component.common;


import kz.asialife.banks.CommonRequest;
import kz.asialife.banks.CommonResponse;
import oracle.jdbc.driver.OracleDriver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CommonComponent {

    public CommonResponse checkSession(CommonRequest request){
        CommonResponse response = new CommonResponse();
        String sessionId = request.getSessionId();
        if(sessionId == null){
            response.setSuccess(false);
            response.setMessage("Session ID is empty. Please try to sign in again! ");
            return response;
        }

        Connection conn = null;
        CallableStatement callableStatement = null;

        try {
            DriverManager.registerDriver(new OracleDriver());
            String url = "jdbc:oracle:thin:@10.0.0.10:1526:bsolife";
            conn = DriverManager.getConnection(url, "mlm", "mlm");

            String sql = "{ ? = call mlm.WEBSERVICE.activ_session(?) }";
            callableStatement = conn.prepareCall(sql);
            callableStatement.setString(2, request.getSessionId());

            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);

            callableStatement.execute();


            String sessionStatus = callableStatement.getString(1);

            //TODO in DB create session statuses by this names below

            if(sessionStatus.equals("BLOCKED")){
                response.setSuccess(false);
                response.setMessage("Sessiion is blocked");
            } else if(sessionStatus.equals("EXPIRED")){
                response.setSuccess(false);
                response.setMessage("Session is expired");
            } else if(sessionStatus.equals("ERROR")){
                response.setSuccess(false);
                response.setMessage("Session is empty");
            }

            callableStatement.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                callableStatement.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
