package gov.nist.csd.pm.core.impl.grpc.client;

import gov.nist.csd.pm.core.impl.grpc.util.FromProtoUtil;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Function;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.proto.v1.pdp.query.GetAllOperationSignaturesRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetAllOperationSignaturesResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.GetOperationSignatureRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetOperationSignatureResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.GetResourceAccessRightsRequest;
import gov.nist.csd.pm.proto.v1.pdp.query.GetResourceAccessRightsResponse;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import gov.nist.csd.pm.proto.v1.pdp.query.Signature;
import io.grpc.StatusRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrpcOperationsQuerier implements OperationsQuery {

    private final PolicyQueryServiceBlockingStub blockingStub;

    public GrpcOperationsQuerier(PolicyQueryServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    @Override
    public AccessRightSet getResourceAccessRights() {
        GetResourceAccessRightsResponse response = blockingStub.getResourceAccessRights(
            GetResourceAccessRightsRequest.newBuilder().build());
        return new AccessRightSet(response.getAccessRightsList());
    }

    @Override
    public Collection<Operation<?>> getOperations() {
        GetAllOperationSignaturesResponse response = blockingStub.getAllOperationSignatures(
            GetAllOperationSignaturesRequest.newBuilder().build());

        List<Operation<?>> operations = new ArrayList<>();
        for (Signature sig : response.getSignatureList()) {
            operations.add(toOperation(sig));
        }
        return operations;
    }

    @Override
    public Collection<String> getOperationNames() {
        GetAllOperationSignaturesResponse response = blockingStub.getAllOperationSignatures(
            GetAllOperationSignaturesRequest.newBuilder().build());

        List<String> names = new ArrayList<>();
        for (Signature sig : response.getSignatureList()) {
            names.add(sig.getName());
        }
        return names;
    }

    @Override
    public Operation<?> getOperation(String name) {
        GetOperationSignatureResponse response = blockingStub.getOperationSignature(
            GetOperationSignatureRequest.newBuilder().setName(name).build());
        return toOperation(response.getSignature());
    }

    @Override
    public boolean operationExists(String operationName) {
        try {
            blockingStub.getOperationSignature(
                GetOperationSignatureRequest.newBuilder().setName(operationName).build());
            return true;
        } catch (StatusRuntimeException e) {
            return false;
        }
    }

    private static Operation<?> toOperation(Signature sig) {
        return switch (sig.getOperationType()) {
            case RESOURCE -> toResourceOperation(sig);
            case ADMIN -> toAdminOperation(sig);
            case ROUTINE -> toRoutine(sig);
            case QUERY -> toQueryOperation(sig);
            case FUNCTION -> toFunction(sig);
            default -> toAdminOperation(sig);
        };
    }

    @SuppressWarnings("unchecked")
    private static ResourceOperation<?> toResourceOperation(Signature sig) {
        String name = sig.getName();
        Type<?> returnType = resolveReturnType(sig);
        List<FormalParameter<?>> params = FromProtoUtil.fromProtoParams(sig.getParamsList());

        return new ResourceOperation(name, returnType, params, List.of()) {
            @Override
            public Object execute(PolicyQuery query, Args args) {
                throw new UnsupportedOperationException(
                    "operation '" + name + "' cannot be executed via gRPC stub proxy");
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static AdminOperation<?> toAdminOperation(Signature sig) {
        String name = sig.getName();
        Type<?> returnType = resolveReturnType(sig);
        List<FormalParameter<?>> params = FromProtoUtil.fromProtoParams(sig.getParamsList());

        return new AdminOperation(name, returnType, params, List.of()) {
            @Override
            public Object execute(PAP pap, Args args) {
                throw new UnsupportedOperationException(
                    "operation '" + name + "' cannot be executed via gRPC stub proxy");
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static Routine<?> toRoutine(Signature sig) {
        String name = sig.getName();
        Type<?> returnType = resolveReturnType(sig);
        List<FormalParameter<?>> params = FromProtoUtil.fromProtoParams(sig.getParamsList());

        return new Routine(name, returnType, params) {
            @Override
            public Object execute(PAP pap, Args args) {
                throw new UnsupportedOperationException(
                    "operation '" + name + "' cannot be executed via gRPC stub proxy");
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static QueryOperation<?> toQueryOperation(Signature sig) {
        String name = sig.getName();
        Type<?> returnType = resolveReturnType(sig);
        List<FormalParameter<?>> params = FromProtoUtil.fromProtoParams(sig.getParamsList());

        return new QueryOperation(name, returnType, params, List.of()) {
            @Override
            public Object execute(PolicyQuery query, Args args) {
                throw new UnsupportedOperationException(
                    "operation '" + name + "' cannot be executed via gRPC stub proxy");
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static Function<?> toFunction(Signature sig) {
        String name = sig.getName();
        Type<?> returnType = resolveReturnType(sig);
        List<FormalParameter<?>> params = FromProtoUtil.fromProtoParams(sig.getParamsList());

        return new Function(name, returnType, params) {
            @Override
            public Object execute(Args args) {
                throw new UnsupportedOperationException(
                    "operation '" + name + "' cannot be executed via gRPC stub proxy");
            }
        };
    }

    private static Type<?> resolveReturnType(Signature sig) {
        return sig.hasReturnType()
            ? FromProtoUtil.fromProtoParamType(sig.getReturnType())
            : BasicTypes.VOID_TYPE;
    }
}
