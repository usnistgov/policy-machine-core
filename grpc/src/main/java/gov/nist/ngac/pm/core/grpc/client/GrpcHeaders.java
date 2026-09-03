/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.grpc.client;

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
