package gov.nist.csd.pm.core.impl.grpc.pap;

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
import gov.nist.csd.pm.proto.v1.pdp.query.*;
import gov.nist.csd.pm.proto.v1.pdp.query.PolicyQueryServiceGrpc.PolicyQueryServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Operation<?>> operations = new ArrayList<>();

        for (Signature sig : blockingStub.getResourceOperationSignatures(
                GetResourceOperationSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            operations.add(toResourceOperation(sig));
        }
        for (Signature sig : blockingStub.getAdminOperationSignatures(
                GetAdminOperationSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            operations.add(toAdminOperation(sig));
        }
        for (Signature sig : blockingStub.getRoutineSignatures(
                GetRoutineSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            operations.add(toRoutine(sig));
        }
        for (Signature sig : blockingStub.getQuerySignatures(
                GetQuerySignaturesRequest.newBuilder().build()).getSignaturesList()) {
            operations.add(toQueryOperation(sig));
        }
        for (Signature sig : blockingStub.getFunctionSignatures(
                GetFunctionSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            operations.add(toFunction(sig));
        }

        return operations;
    }

    @Override
    public Collection<String> getOperationNames() {
        Set<String> names = new HashSet<>();

        for (Signature sig : blockingStub.getResourceOperationSignatures(
                GetResourceOperationSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            names.add(sig.getName());
        }
        for (Signature sig : blockingStub.getAdminOperationSignatures(
                GetAdminOperationSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            names.add(sig.getName());
        }
        for (Signature sig : blockingStub.getRoutineSignatures(
                GetRoutineSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            names.add(sig.getName());
        }
        for (Signature sig : blockingStub.getQuerySignatures(
                GetQuerySignaturesRequest.newBuilder().build()).getSignaturesList()) {
            names.add(sig.getName());
        }
        for (Signature sig : blockingStub.getFunctionSignatures(
                GetFunctionSignaturesRequest.newBuilder().build()).getSignaturesList()) {
            names.add(sig.getName());
        }

        return names;
    }

    @Override
    public Operation<?> getOperation(String name) {
        if (!getOperationNames().contains(name)) {
            return null;
        }

        try {
            GetResourceOperationSignatureResponse response = blockingStub.getResourceOperationSignature(
                GetResourceOperationSignatureRequest.newBuilder().setName(name).build());
            return toResourceOperation(response.getSignature());
        } catch (Exception e) {
        }

        try {
            GetAdminOperationSignatureResponse response = blockingStub.getAdminOperationSignature(
                GetAdminOperationSignatureRequest.newBuilder().setName(name).build());
            return toAdminOperation(response.getSignature());
        } catch (StatusRuntimeException e) {
        }

        try {
            GetRoutineSignatureResponse response = blockingStub.getRoutineSignature(
                GetRoutineSignatureRequest.newBuilder().setName(name).build());
            return toRoutine(response.getSignature());
        } catch (StatusRuntimeException e) {
        }

        try {
            GetQuerySignatureResponse response = blockingStub.getQuerySignature(
                GetQuerySignatureRequest.newBuilder().setName(name).build());
            return toQueryOperation(response.getSignature());
        } catch (StatusRuntimeException e) {
        }

        try {
            GetFunctionSignatureResponse response = blockingStub.getFunctionSignature(
                GetFunctionSignatureRequest.newBuilder().setName(name).build());
            return toFunction(response.getSignature());
        } catch (StatusRuntimeException e) {
        }

        throw new IllegalStateException("operation \"" + name + "\" was in getOperationNames() but was not found.");
    }

    @Override
    public boolean operationExists(String operationName) {
        return getOperations().stream().map(Operation::getName).toList().contains(operationName);
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
