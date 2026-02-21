package gov.nist.csd.pm.core.impl.grpc.client;

import static gov.nist.csd.pm.core.impl.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.csd.pm.core.pap.query.AccessQuery;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.ObligationsQuery;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.query.SelfAccessQuery;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;

public class GrpcQueryPDP implements PolicyQuery {

    private GrpcPolicyQuerier grpcPolicyQuerier;

    public GrpcQueryPDP(ManagedChannel managedChannel, String user, String process) {
        this.grpcPolicyQuerier = new GrpcPolicyQuerier(
            PolicyQueryServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process))));
    }

    @Override
    public AccessQuery access() {
        return grpcPolicyQuerier.access();
    }

    public SelfAccessQuery selfAccess(UserContext userCtx) {
        return grpcPolicyQuerier.selfAccess(userCtx);
    }

    @Override
    public GraphQuery graph() {
        return grpcPolicyQuerier.graph();
    }

    @Override
    public ProhibitionsQuery prohibitions() {
        return grpcPolicyQuerier.prohibitions();
    }

    @Override
    public ObligationsQuery obligations() {
        return grpcPolicyQuerier.obligations();
    }

    @Override
    public OperationsQuery operations() {
        return grpcPolicyQuerier.operations();
    }
}
