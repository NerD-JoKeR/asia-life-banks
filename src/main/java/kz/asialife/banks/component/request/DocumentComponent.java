package kz.asialife.banks.component.request;


import kz.asialife.banks.CommonResponse;
import kz.asialife.banks.ReqDocumentRequest;
import kz.asialife.banks.ReqDocumentResponse;
import kz.asialife.banks.component.common.CommonComponent;
import oracle.jdbc.driver.OracleDriver;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DocumentComponent extends CommonComponent {

    public ReqDocumentResponse document(ReqDocumentRequest request) {

        ReqDocumentResponse commonResponse = checkSession(request, new ReqDocumentResponse());
        if(commonResponse != null){
            return commonResponse;
        }

        ReqDocumentResponse response = new ReqDocumentResponse();

        Connection conn = null;
        CallableStatement callableStatement = null;

        try {

            DriverManager.registerDriver(new OracleDriver()); //oracle driver

            String url = "TODO correct conn url"; //connection to DB

            conn = DriverManager.getConnection(url, "log", "pass");

            String sql = "{ ? = call mlm.WEBSERVICE.reg_doc_bank(?) }";  // connected to webserevice and call method from LIC

            callableStatement = conn.prepareCall(sql);

            callableStatement.setString(2, request.getInfo());

            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);

            callableStatement.execute();

            //this is the main line to the return response

            response.setState(callableStatement.getString(1));
            response.setSuccess(true);
            response.getMessage();

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
