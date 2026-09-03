/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.grpc.client;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.core.pap.modification.GraphModification;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A {@link GraphModification} that submits each operation as an admin adjudication request over gRPC.
 */
public class GrpcGraphModifier implements GraphModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcGraphModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public long createPolicyClass(String name) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_policy_class")
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of("name", name)))
            .build();

        AdjudicateOperationResponse response = blockingStub.adjudicateOperation(request);
        return response.getValue().getInt64Value();
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> assignments) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_user_attribute")
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
                "id", id
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("assign")
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
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
            .putAllArgs(ToProtoUtil.toStringValueMapProto(Map.of(
                "ua", ua,
                "target", target
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
