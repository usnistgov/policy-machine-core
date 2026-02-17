package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.grpc.util.ToProtoUtil;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GrpcProhibitionsModifier implements ProhibitionsModification {

    private final AdminAdjudicationServiceBlockingStub blockingStub;

    public GrpcProhibitionsModifier(AdminAdjudicationServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public void createNodeProhibition(String name, long nodeId, AccessRightSet accessRightSet,
                                      Set<Long> inclusionSet, Set<Long> exclusionSet,
                                      boolean isConjunctive) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_node_prohibition")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name,
                "node_id", nodeId,
                "arset", new ArrayList<>(accessRightSet),
                "inclusion_set", new ArrayList<>(inclusionSet),
                "exclusion_set", new ArrayList<>(exclusionSet),
                "is_conjunctive", isConjunctive
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void createProcessProhibition(String name, long userId, String process,
                                         AccessRightSet accessRightSet, Set<Long> inclusionSet,
                                         Set<Long> exclusionSet, boolean isConjunctive) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("create_process_prohibition")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name,
                "user_id", userId,
                "process", process,
                "arset", new ArrayList<>(accessRightSet),
                "inclusion_set", new ArrayList<>(inclusionSet),
                "exclusion_set", new ArrayList<>(exclusionSet),
                "is_conjunctive", isConjunctive
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        OperationRequest request = OperationRequest.newBuilder()
            .setName("delete_prohibition")
            .setArgs(ToProtoUtil.toValueMapProto(Map.of(
                "name", name
            )))
            .build();

        blockingStub.adjudicateOperation(request);
    }
}
