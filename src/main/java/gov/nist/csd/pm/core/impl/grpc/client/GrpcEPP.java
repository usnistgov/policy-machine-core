package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.proto.v1.epp.EPPServiceGrpc;
import io.grpc.ManagedChannel;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void processEvent(String operation, Map<String, Object> args) {
        try {
            new GrpcEventSubscriber(EPPServiceGrpc.newBlockingStub(managedChannel))
                .processEvent(
                    new EventContext(
                        new EventContextUser(user, process),
                        operation,
                        args
                    )
                );
        } catch (PMException e) {
            logger.warn("error processing operation {}", operation, e);
        }
    }
}
