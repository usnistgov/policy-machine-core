package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.ArrayList;
import java.util.Map;

public class GrpcOperationsModifier implements OperationsModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcOperationsModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("set_resource_access_rights")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "arset", new ArrayList<>(resourceAccessRights)
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        ExecutePMLRequest request = ExecutePMLRequest.newBuilder()
            .setPml(operation.toString())
            .build();

        blockingStub.executePML(request);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("delete_operation")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
