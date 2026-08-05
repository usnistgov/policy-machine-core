package gov.nist.ngac.pm.core.impl.grpc.client;

import gov.nist.ngac.pm.core.common.event.EventSubscriber;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.proto.v1.epp.EPPServiceGrpc.EPPServiceBlockingStub;

/**
 * {@link EventSubscriber} that forwards received events to a remote EPP service over gRPC.
 */
public class GrpcEventSubscriber implements EventSubscriber {

    private EPPServiceBlockingStub eppStub;

    public GrpcEventSubscriber(EPPServiceBlockingStub eppStub) {
        this.eppStub = eppStub;
    }

    @Override
    public void processEvent(EventContext eventCtx) {
        eppStub.processEvent(ToProtoUtil.toEventContextProto(eventCtx));
    }
}
