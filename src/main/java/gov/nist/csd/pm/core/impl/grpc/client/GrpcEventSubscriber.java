package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.proto.v1.epp.EPPServiceGrpc.EPPServiceBlockingStub;

public class GrpcEventSubscriber implements EventSubscriber {

    private EPPServiceBlockingStub eppStub;

    public GrpcEventSubscriber(EPPServiceBlockingStub eppStub) {
        this.eppStub = eppStub;
    }

    @Override
    public void processEvent(EventContext eventCtx) throws PMException {
        eppStub.processEvent(ToProtoUtil.toEventContextProto(eventCtx));
    }
}
