package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GrpcGraphModifier implements GraphModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcGraphModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public long createPolicyClass(String name) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_policy_class")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of("name", name)))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);
        return response.getValue().getInt64Value();
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> assignments) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_user_attribute")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name,
                "descendants", new ArrayList<>(assignments)
            )))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);
        return response.getValue().getInt64Value();
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> assignments) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_object_attribute")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name,
                "descendants", new ArrayList<>(assignments)
            )))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);
        return response.getValue().getInt64Value();
    }

    @Override
    public long createObject(String name, Collection<Long> assignments) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_object")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name,
                "descendants", new ArrayList<>(assignments)
            )))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);
        return response.getValue().getInt64Value();
    }

    @Override
    public long createUser(String name, Collection<Long> assignments) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_user")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name,
                "descendants", new ArrayList<>(assignments)
            )))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);
        return response.getValue().getInt64Value();
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("set_node_properties")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "id", id,
                "properties", properties
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("delete_node")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "id", id
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("assign")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "ascendant", ascId,
                "descendants", new ArrayList<>(descendants)
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("deassign")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "ascendant", ascendant,
                "descendants", new ArrayList<>(descendants)
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("associate")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "ua", ua,
                "target", target,
                "arset", new ArrayList<>(accessRights)
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("dissociate")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "ua", ua,
                "target", target
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
