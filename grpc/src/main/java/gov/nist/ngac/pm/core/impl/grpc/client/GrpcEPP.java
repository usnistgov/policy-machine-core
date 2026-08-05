package gov.nist.ngac.pm.core.impl.grpc.client;

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
