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

import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.proto.v1.epp.EPPServiceGrpc;
import io.grpc.ManagedChannel;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side handle for submitting events to the remote Event Processing Point over gRPC.
 */
public class GrpcEPP {

    private static final Logger logger = LoggerFactory.getLogger(GrpcEPP.class);

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcEPP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    /**
     * Builds an {@link EventContext} from this handle's user/process and sends it to the remote EPP service.
     *
     * @param operation the name of the operation that triggered the event
     * @param args the operation's argument values, keyed by parameter name
     */
    public void processEvent(String operation, Map<String, Object> args) {
        new GrpcEventSubscriber(EPPServiceGrpc.newBlockingStub(managedChannel))
            .processEvent(
                new EventContext(
                    new EventContextUser(user, process),
                    operation,
                    args
                )
            );
    }
}
