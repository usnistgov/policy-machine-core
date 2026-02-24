package gov.nist.csd.pm.core.pap.pml.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilege;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PMLOperationSignature implements PMLStatementSerializable {

    private final OperationType type;
    private final String name;
    private final Type<?> returnType;
    private final List<FormalParameter<?>> formalParameters;
    private final List<FormalParameter<?>> eventParameters;
    private final List<RequiredCapability> reqCaps;

    public PMLOperationSignature(OperationType type, String name, Type<?> returnType, List<FormalParameter<?>> formalParameters,
                                 List<RequiredCapability> reqCaps) {
        this.type = type;
        this.name = name;
        this.returnType = returnType;
        this.formalParameters = formalParameters;
        this.eventParameters = new ArrayList<>(formalParameters);
        this.reqCaps = reqCaps;
    }

    public PMLOperationSignature(OperationType type, String name, Type<?> returnType, List<FormalParameter<?>> formalParameters,
                                 List<FormalParameter<?>> eventParameters, List<RequiredCapability> reqCaps) {
        this.type = type;
        this.name = name;
        this.returnType = returnType;
        this.formalParameters = formalParameters;
        this.eventParameters = eventParameters == null ? new ArrayList<>(formalParameters) : eventParameters;
        this.reqCaps = reqCaps;
    }

    public OperationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Type<?> getReturnType() {
        return returnType;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return formalParameters;
    }

    public List<FormalParameter<?>> getEventParameters() {
        return eventParameters;
    }

    public List<RequiredCapability> getReqCaps() {
        return reqCaps;
    }

    protected String serializeEventArgs() {
        if (eventParameters.equals(formalParameters)) {
            return "";
        }

        Set<String> formalParamNames = formalParameters.stream()
            .map(FormalParameter::getName)
            .collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder("@eventctx(");
        boolean first = true;
        for (FormalParameter<?> ep : eventParameters) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            if (formalParamNames.contains(ep.getName())) {
                sb.append(ep.getName());
            } else {
                sb.append(TypeStringer.toPMLString(ep.getType())).append(" ").append(ep.getName());
            }
        }
        sb.append(")\n");
        return sb.toString();
    }

    protected String serializeFormalArgs() {
        String pml = "";
        for (FormalParameter<?> formalParameter : getFormalParameters()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            String annotationStr = formalParameter instanceof NodeFormalParameter<?> ? "@node ": "";
            pml += String.format("%s%s %s",
                annotationStr,
                TypeStringer.toPMLString(formalParameter.getType()),
                formalParameter.getName());
        }
        return pml;
    }

    private String serializeReqCap(int indentLevel) {
        List<String> reqCapStrs = new ArrayList<>();
        for (RequiredCapability reqCap : getReqCaps()) {
            if (reqCap instanceof PMLRequiredCapabilityFunc pmlRequiredCapabilityFunc) {
                reqCapStrs.add(pmlRequiredCapabilityFunc.toFormattedString(indentLevel));
                continue;
            }

            List<RequiredPrivilege> requiredPrivileges = reqCap.getRequiredPrivileges();
            List<String> entries = new ArrayList<>();
            for (RequiredPrivilege requiredPrivilege : requiredPrivileges) {
                String key;
                AccessRightSet ars;

                switch (requiredPrivilege) {
                    case RequiredPrivilegeOnParameter requiredPrivilegeOnParameter -> {
                        key = requiredPrivilegeOnParameter.param().getName();
                        ars = requiredPrivilegeOnParameter.getRequired();
                    }
                    case RequiredPrivilegeOnNode requiredPrivilegeOnNode -> {
                        key = String.format("\"%s\"", requiredPrivilegeOnNode.getName());
                        ars = requiredPrivilegeOnNode.getRequired();
                    }
                }

                entries.add(String.format(
                    "%s: [%s]",
                    key,
                    ars.stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", "))
                ));
            }

            reqCapStrs.add(String.format("@reqcap({%s})", String.join(", ", entries)));
        }

        if (reqCapStrs.isEmpty()) {
            return "";
        }

        return String.format("%s%s\n", indent(indentLevel), String.join("\n", reqCapStrs));

    }

    protected String toString(String prefix, int indentLevel) {
        String eventArgsStr = serializeEventArgs();
        String reqCapStr = serializeReqCap(indentLevel);
        String argsStr = serializeFormalArgs();

        String indent = indent(indentLevel);
        return String.format(
            "%s%s%s%s %s(%s) %s",
            eventArgsStr,
            reqCapStr,
            indent,
            prefix,
            name,
            argsStr,
            returnType == null || returnType.equals(VOID_TYPE) ? "" : TypeStringer.toPMLString(returnType) + " "
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return toString(type.toString().toLowerCase(), indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PMLOperationSignature that = (PMLOperationSignature) o;
        return type == that.type && Objects.equals(name, that.name) && Objects.equals(returnType,
            that.returnType) && Objects.equals(formalParameters, that.formalParameters)
            && Objects.equals(eventParameters, that.eventParameters)
            && Objects.equals(reqCaps, that.reqCaps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, returnType, formalParameters, eventParameters, reqCaps);
    }

    public enum OperationType {
        ADMINOP,
        RESOURCEOP,
        QUERY,
        FUNCTION,
        ROUTINE
    }
}
