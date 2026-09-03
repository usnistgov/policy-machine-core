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

import static gov.nist.ngac.pm.core.grpc.client.GrpcHeaders.buildHeaders;

import gov.nist.ngac.pm.core.grpc.util.FromProtoUtil;
import gov.nist.ngac.pm.core.grpc.util.ToProtoUtil;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdjudicateOperationResponse;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.AdminAdjudicationServiceGrpc.AdminAdjudicationServiceBlockingStub;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ExecutePMLRequest;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.ExecutePMLResponse;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.OperationRequest;
import gov.nist.ngac.pm.proto.v1.pdp.adjudication.RoutineRequest;
import io.grpc.ManagedChannel;
import io.grpc.stub.MetadataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Client-side handle for the admin adjudication gRPC service, scoped to a single user/process pair.
 */
public class GrpcAdminPDP {

    private final ManagedChannel managedChannel;
    private final String user;
    private final String process;

    public GrpcAdminPDP(ManagedChannel managedChannel, String user, String process) {
        this.managedChannel = managedChannel;
        this.user = user;
        this.process = process;
    }

    /**
     * Adjudicates a single admin operation by name against the remote PDP.
     *
     * @param name the name of the operation to adjudicate
     * @param args the operation's argument values, keyed by parameter name
     * @return the operation's return value, or null if it has none
     */
    public Object adjudicateOperation(String name, Map<String, Object> args) {
        AdminAdjudicationServiceBlockingStub stub = AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));

        OperationRequest request = OperationRequest.newBuilder()
            .setName(name)
            .putAllArgs(ToProtoUtil.toStringValueMapProto(args))
            .build();

        AdjudicateOperationResponse response = stub.adjudicateOperation(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }

    /**
     * Adjudicates a routine as a single batch of admin operations against the remote PDP.
     *
     * @param operations the ordered list of operations to execute as one routine
     */
    public void adjudicateRoutine(List<gov.nist.ngac.pm.core.pdp.adjudication.OperationRequest> operations) {
        AdminAdjudicationServiceBlockingStub stub = AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));

        List<OperationRequest> requestProtos = new ArrayList<>();
        for (gov.nist.ngac.pm.core.pdp.adjudication.OperationRequest req : operations) {
            requestProtos.add(OperationRequest.newBuilder()
                .setName(req.op())
                .putAllArgs(ToProtoUtil.toStringValueMapProto(req.args()))
                .build());
        }

        RoutineRequest request = RoutineRequest.newBuilder()
            .addAllOperations(requestProtos)
            .build();

        stub.adjudicateRoutine(request);
    }

    /**
     * Compiles and executes a PML script against the remote PDP as this user/process.
     *
     * @param pml the PML source to execute
     * @return the script's return value, or null if it has none
     */
    public Object executePML(String pml) {
        AdminAdjudicationServiceBlockingStub stub = AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process)));

        ExecutePMLRequest request = ExecutePMLRequest.newBuilder()
            .setPml(pml)
            .build();

        ExecutePMLResponse response = stub.executePML(request);

        if (response.hasValue()) {
            return FromProtoUtil.fromValue(response.getValue());
        }

        return null;
    }

    /**
     * Returns a modifier for issuing policy modification operations as this user/process.
     *
     * @return a modifier scoped to this handle's user/process
     */
    public GrpcPolicyModifier modify() {
        return new GrpcPolicyModifier(AdminAdjudicationServiceGrpc.newBlockingStub(managedChannel)
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(buildHeaders(user, process))));
    }
}
