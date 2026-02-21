package gov.nist.csd.pm.core.impl.grpc.client;

import static gov.nist.csd.pm.core.impl.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.csd.pm.proto.v1.pdp.adjudication.ResourceAdjudicationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;
import java.util.Map;

public class GrpcResourcePDP {

    private final ManagedChannel managedChannel;

    public GrpcResourcePDP(ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
    }

    public Adjudicator withUser(String user, String process) {
        return new Adjudicator(managedChannel, user, process);
    }

    public static class Adjudicator {

        private final ManagedChannel managedChannel;
        private final String user;
        private final String process;

        public Adjudicator(ManagedChannel managedChannel, String user, String process) {
            this.managedChannel = managedChannel;
            this.user = user;
            this.process = process;
        }

        public void adjudicateOperation(String name, Map<String, Object> args) {
            ResourceAdjudicationServiceGrpc.ResourceAdjudicationServiceBlockingStub stub =
                ResourceAdjudicationServiceGrpc.newBlockingStub(managedChannel)
                    .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));
            GrpcResourceAdjudicationService service = new GrpcResourceAdjudicationService(stub);

            service.adjudicateResourceOperation(name, args);
        }
    }
}
