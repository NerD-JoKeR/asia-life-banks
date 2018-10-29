package kz.asialife.banks.component.authorization;


import kz.asialife.banks.AuthorizationWSRequest;
import kz.asialife.banks.AuthorizationWSResponse;
import oracle.jdbc.driver.OracleDriver;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class AuthorizationWSComponent {

    public AuthorizationWSResponse authorizeWS(AuthorizationWSRequest request) {

        AuthorizationWSResponse response = new AuthorizationWSResponse();

        Connection conn = null;
        CallableStatement callableStatement = null;

        try {

            DriverManager.registerDriver(new OracleDriver());

            String url = "TODO correct conn url";

            conn = DriverManager.getConnection(url, "log", "pass");

            String sql = "{ ? = call mlm.WEBSERVICE.get_session(?,?,?) }";

            callableStatement = conn.prepareCall(sql);

            callableStatement.setString(2, request.getLogin());
            callableStatement.setString(3, request.getPassword());

            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);

            callableStatement.execute();

            //this is the main line
            response.setSessionId(callableStatement.getString(1));

            String answer = callableStatement.getString(4);

            if (answer.equals("1")){
                answer = "true";
                response.setState(answer);
            } else{
                answer = "false";
                response.setState(answer);
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
        return response;

    }
}
