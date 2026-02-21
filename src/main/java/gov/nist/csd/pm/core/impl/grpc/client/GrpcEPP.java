package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.proto.v1.epp.EPPServiceGrpc;
import io.grpc.ManagedChannel;
import java.util.Map;

public class GrpcEPP {

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcEPP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    public void processEvent(String operation, Map<String, Object> args) throws PMException {
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
