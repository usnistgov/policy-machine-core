package gov.nist.csd.pm.core.impl.grpc.client;

import io.grpc.Metadata;

public class GrpcHeaders {

    public static final Metadata.Key<String> PM_USER_KEY =
        Metadata.Key.of("x-pm-user", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> PM_PROCESS_KEY =
        Metadata.Key.of("x-pm-process", Metadata.ASCII_STRING_MARSHALLER);

    public static Metadata buildHeaders(String accessEmail, String workflowId) {
        Metadata headers = new Metadata();
        headers.put(PM_USER_KEY, accessEmail);
        headers.put(PM_PROCESS_KEY, workflowId);
        return headers;
    }

}
