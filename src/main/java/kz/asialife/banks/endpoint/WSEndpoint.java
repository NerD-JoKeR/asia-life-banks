package kz.asialife.banks.endpoint;

import kz.asialife.banks.*;
import kz.asialife.banks.component.authorization.AuthorizationWSComponent;
import kz.asialife.banks.component.request.DocumentComponent;
import kz.asialife.banks.component.request.PaymentComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class WSEndpoint {
    private static final String NAMESPACE_URI = "http://asialife.kz/banks";


    @Autowired
    private AuthorizationWSComponent authorizationWSComponent;

    @Autowired
    private DocumentComponent documentComponent;

    @Autowired
    private PaymentComponent paymentComponent;



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "authorizationWSRequest")
    @ResponsePayload
    public AuthorizationWSResponse authorizationWSRequest(@RequestPayload AuthorizationWSRequest request) {
        return authorizationWSComponent.authorizeWS(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "reqDocumentRequest")
    @ResponsePayload
    public ReqDocumentResponse reqDocumentRequest(@RequestPayload ReqDocumentRequest request) {
        return documentComponent.document(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "paymentRequest")
    @ResponsePayload
    public PaymentResponse paymentRequest(@RequestPayload PaymentRequest request) {
        return paymentComponent.payment(request);
    }
}
