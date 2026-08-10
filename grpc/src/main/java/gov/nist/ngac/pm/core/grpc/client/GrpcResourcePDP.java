package gov.nist.ngac.pm.core.grpc.client;

import static gov.nist.ngac.pm.core.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ResourceAdjudicationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;
import java.util.Map;

/**
 * Client-side handle for the resource adjudication gRPC service, scoped to a single user/process pair.
 */
public class GrpcResourcePDP {

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcResourcePDP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    /**
     * Adjudicates a single resource operation by name against the remote PDP.
     *
     * @param name the name of the operation to adjudicate
     * @param args the operation's argument values, keyed by parameter name
     * @return the operation's return value, or null if it has none
     */
    public Object adjudicateOperation(String name, Map<String, Object> args) {
        ResourceAdjudicationServiceGrpc.ResourceAdjudicationServiceBlockingStub stub =
            ResourceAdjudicationServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));
        GrpcResourceAdjudicationService service = new GrpcResourceAdjudicationService(stub);

        return service.adjudicateResourceOperation(name, args);
    }
}
