package gov.nist.ngac.pm.core.impl.grpc.client;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.core.pap.modification.OperationsModification;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * {@link OperationsModification} implementation that submits each operation as an admin adjudication
 * request over gRPC.
 */
public class GrpcOperationsModifier implements OperationsModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcOperationsModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("set_resource_access_rights")
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
                "name", name
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
