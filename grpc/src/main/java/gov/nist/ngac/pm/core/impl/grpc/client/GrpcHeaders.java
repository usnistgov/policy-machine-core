package gov.nist.ngac.pm.core.impl.grpc.client;

import io.grpc.Metadata;

/**
 * Builds the gRPC metadata headers used to carry the acting user and process on every request.
 */
public class GrpcHeaders {

    public static final Metadata.Key<String> PM_USER_KEY =
        Metadata.Key.of("x-pm-user", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> PM_PROCESS_KEY =
        Metadata.Key.of("x-pm-process", Metadata.ASCII_STRING_MARSHALLER);

    /**
     * Builds the {@link Metadata} headers identifying the acting user and process for a gRPC call.
     *
     * @param user the acting user, sent as the {@link #PM_USER_KEY} header
     * @param process the acting process, sent as the {@link #PM_PROCESS_KEY} header
     * @return the populated gRPC metadata to attach to the call
     */
    public static Metadata buildHeaders(String user, String process) {
        Metadata headers = new Metadata();
        headers.put(PM_USER_KEY, user);
        headers.put(PM_PROCESS_KEY, process);
        return headers;
    }

}
