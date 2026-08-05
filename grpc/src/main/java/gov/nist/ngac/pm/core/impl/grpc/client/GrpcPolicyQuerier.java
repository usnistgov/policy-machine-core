package gov.nist.ngac.pm.core.impl.grpc.client;

import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc;
import gov.nist.ngac.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;

public class GrpcPolicyQuerier implements PolicyQuery {

    private PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub stub;

    public GrpcPolicyQuerier(PolicyQueryServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public GrpcAccessQuerier access() {
        return new GrpcAccessQuerier(stub);
    }

    public GrpcSelfAccessQuerier selfAccess(UserContext userCtx) {
        return new GrpcSelfAccessQuerier(stub);
    }

    @Override
    public GrpcGraphQuerier graph() {
        return new GrpcGraphQuerier(stub);
    }

    @Override
    public GrpcProhibitionsQuerier prohibitions() {
        return new GrpcProhibitionsQuerier(stub);
    }

    @Override
    public GrpcObligationsQuerier obligations() {
        return new GrpcObligationsQuerier(stub);
    }

    @Override
    public GrpcOperationsQuerier operations() {
        return new GrpcOperationsQuerier(stub);
    }
}
