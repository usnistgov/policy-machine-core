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
import gov.nist.ngac.pm.core.pap.modification.OperationsModification;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.OperationRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * A {@link OperationsModification} that submits each operation as an admin adjudication request over
 * gRPC.
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
