package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.executable.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.NegatedExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ComplementedValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.PolicyQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.executable.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.executable.op.graph.GraphOp.ARSET_OPERAND;
import static gov.nist.csd.pm.pap.AdminAccessRights.isAdminAccessRight;

public class CreateProhibitionStatement extends OperationStatement {

    private final Expression name;
    private final Expression subject;
    private final ProhibitionSubjectType subjectType;
    private final Expression accessRights;
    private final boolean isIntersection;
    private final Expression containers;

    public CreateProhibitionStatement(Expression name, Expression subject, ProhibitionSubjectType subjectType, Expression accessRights,
                                      boolean isIntersection, Expression containers) {
        super(new CreateProhibitionOp());
        this.name = name;
        this.subject = subject;
        this.subjectType = subjectType;
        this.accessRights = accessRights;
        this.isIntersection = isIntersection;
        this.containers = containers;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap)
            throws PMException {
        Value nameValue = this.name.execute(ctx, pap);

        // convert subject name to id
        Value subjectValue = this.subject.execute(ctx, pap);
        ProhibitionSubject prohibitionSubject;
        if (subjectType == ProhibitionSubjectType.PROCESS) {
            prohibitionSubject = new ProhibitionSubject(subjectValue.getStringValue());
        } else {
            long subjectId = pap.query().graph().getNodeId(subjectValue.getStringValue());
            prohibitionSubject = new ProhibitionSubject(subjectId);
        }

        Value permissionsValue = this.accessRights.execute(ctx, pap);
        List<Value> arrayValue = permissionsValue.getArrayValue();
        AccessRightSet ops = new AccessRightSet();
        for (Value v : arrayValue) {
            ops.add(v.getStringValue());
        }

        List<ContainerCondition> containerConditions = new ArrayList<>();
        for (Value container : containers.execute(ctx, pap).getArrayValue()) {
            boolean isComplement = container instanceof ComplementedValue;
            long containerId = pap.query().graph().getNodeId(container.getStringValue());

            containerConditions.add(new ContainerCondition(containerId, isComplement));
        }

        return Map.of(
                NAME_OPERAND, nameValue.getStringValue(),
                SUBJECT_OPERAND, prohibitionSubject,
                ARSET_OPERAND, ops,
                INTERSECTION_OPERAND, isIntersection,
                CONTAINERS_OPERAND, containerConditions
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String subjectStr = getSubjectStr();
        String indent = indent(indentLevel);
        return String.format(
                """
                %screate prohibition %s
                %s  deny %s %s
                %s  access rights %s
                %s  on %s of %s""",
                indent, name,
                indent, subjectStr, subject,
                indent, accessRights,
                indent, (isIntersection ? "intersection" : "union"), containers
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateProhibitionStatement that)) return false;
        return isIntersection == that.isIntersection && Objects.equals(name, that.name) && Objects.equals(subject, that.subject) && subjectType == that.subjectType && Objects.equals(accessRights, that.accessRights) && Objects.equals(containers, that.containers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, subjectType, accessRights, isIntersection, containers);
    }

    private String getSubjectStr() {
        String subjectStr = "";
        switch (subjectType) {
            case USER_ATTRIBUTE -> subjectStr = "UA";
            case USER -> subjectStr = "U";
            case PROCESS -> subjectStr = "process";
        }

        return subjectStr;
    }

    public static CreateProhibitionStatement fromProhibition(PolicyQuery policyQuery, Prohibition prohibition) throws PMException {
        List<Expression> accessRightsList = new ArrayList<>();
        for (String ar : prohibition.getAccessRightSet()) {
            if (isAdminAccessRight(ar)) {
                accessRightsList.add(new ReferenceByID(ar));
            } else {
                accessRightsList.add(new StringLiteral(ar));
            }
        }
        ArrayLiteral arList = new ArrayLiteral(accessRightsList, Type.string());

        List<Expression> containersList = new ArrayList<>();
        for (ContainerCondition cc : prohibition.getContainers()) {
            StringLiteral s = new StringLiteral(policyQuery.graph().getNodeById(cc.getId()).getName());
            if (cc.isComplement()) {
                containersList.add(new NegatedExpression(s));
            } else {
                containersList.add(s);
            }
        }

        ArrayLiteral containers = new ArrayLiteral(containersList, Type.string());

        ProhibitionSubjectType type;
        StringLiteral subjectName;
        if (prohibition.getSubject().isNode()) {
            Node subjectNode = policyQuery.graph().getNodeById(prohibition.getSubject().getNodeId());
            subjectName = new StringLiteral(subjectNode.getName());

            if (subjectNode.getType() == NodeType.UA) {
                type = ProhibitionSubjectType.USER_ATTRIBUTE;
            } else {
                type = ProhibitionSubjectType.USER;
            }
        } else {
            subjectName = new StringLiteral(prohibition.getSubject().getProcess());
            type = ProhibitionSubjectType.PROCESS;
        }

        return new CreateProhibitionStatement(
                new StringLiteral(prohibition.getName()),
                subjectName,
                type,
                arList,
                prohibition.isIntersection(),
                containers
        );
    }
}
