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

package gov.nist.ngac.pm.core.pap.pml.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilege;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.ngac.pm.core.pap.pml.type.TypeStringer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The compiled signature of a PML operation. Renders back to PML source via {@link #toFormattedString}.
 */
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

    public List<FormalParameter<?>> getRequiredFormalParameters() {
        return formalParameters.stream().filter(FormalParameter::isRequired).toList();
    }

    public List<FormalParameter<?>> getEventParameters() {
        return eventParameters;
    }

    public List<RequiredCapability> getReqCaps() {
        return reqCaps;
    }

    /**
     * Formats the @EventCtx(...) annotation, or an empty string if none is needed.
     */
    protected String serializeEventArgs() {
        if (eventParameters.equals(formalParameters)) {
            return "";
        }

        Set<String> formalParamNames = formalParameters.stream()
            .map(FormalParameter::getName)
            .collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder("@EventCtx(");
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

    /**
     * Formats the operation's formal parameter list for PML source, prefixing node parameters with the
     * "@Node" annotation and suffixing optional ones with "?".
     */
    protected String serializeFormalArgs() {
        String pml = "";
        for (FormalParameter<?> formalParameter : getFormalParameters()) {
            if (!pml.isEmpty()) {
                pml += ", ";
            }

            String annotationStr = formalParameter instanceof NodeFormalParameter<?> ? "@Node ": "";
            pml += String.format("%s%s %s%s",
                annotationStr,
                TypeStringer.toPMLString(formalParameter.getType()),
                formalParameter.getName(),
                formalParameter.isRequired() ? "" : "?"
            );
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

            reqCapStrs.add(String.format("@ReqCap({%s})", String.join(", ", entries)));
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

    /**
     * The kind of operation a {@link PMLOperationSignature} describes.
     */
    public enum OperationType {
        ADMINOP,
        RESOURCEOP,
        QUERY,
        FUNCTION,
        ROUTINE
    }
}
