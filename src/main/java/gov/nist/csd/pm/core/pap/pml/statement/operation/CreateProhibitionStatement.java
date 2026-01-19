package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.core.pap.function.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.CONTAINERS_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.INTERSECTION_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.SUBJECT_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateProhibitionStatement extends OperationStatement {

    private final Expression<String> name;
    private final Expression<String> subject;
    private final ProhibitionSubjectType subjectType;
    private final Expression<List<String>> accessRights;
    private final boolean isIntersection;
    private final Expression<Map<String, Boolean>> containers;

    public CreateProhibitionStatement(Expression<String> name, Expression<String> subject, ProhibitionSubjectType subjectType,
                                      Expression<List<String>> accessRights, boolean isIntersection, Expression<Map<String, Boolean>> containers) {
        super(new CreateProhibitionOp());
        this.name = name;
        this.subject = subject;
        this.subjectType = subjectType;
        this.accessRights = accessRights;
        this.isIntersection = isIntersection;
        this.containers = containers;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = this.name.execute(ctx, pap);
        String subject = this.subject.execute(ctx, pap);
        AccessRightSet ops = new AccessRightSet(this.accessRights.execute(ctx, pap));

        ProhibitionSubject prohibitionSubject;
        if (subjectType == ProhibitionSubjectType.PROCESS) {
            prohibitionSubject = new ProhibitionSubject(subject);
        } else {
            long subjectId = pap.query().graph().getNodeId(subject);
            prohibitionSubject = new ProhibitionSubject(subjectId);
        }

        List<ContainerCondition> containerConditions = new ArrayList<>();
        for (var container : containers.execute(ctx, pap).entrySet()) {
            long containerId = pap.query().graph().getNodeId(container.getKey());

            containerConditions.add(new ContainerCondition(containerId, container.getValue()));
        }

        return new Args()
            .put(NAME_PARAM, name)
            .put(SUBJECT_PARAM, prohibitionSubject)
            .put(ARSET_PARAM, new ArrayList<>(ops))
            .put(INTERSECTION_PARAM, isIntersection)
            .put(CONTAINERS_PARAM, new ArrayList<>(containerConditions));
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
        if (this == o)
            return true;
        if (!(o instanceof CreateProhibitionStatement that))
            return false;
        return isIntersection == that.isIntersection && Objects.equals(name, that.name)
            && Objects.equals(subject, that.subject) && subjectType == that.subjectType
            && Objects.equals(accessRights, that.accessRights) && Objects.equals(containers,
            that.containers);
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
        List<Expression<String>> accessRightsList = new ArrayList<>();
        for (String ar : prohibition.getAccessRightSet()) {
            if (isAdminAccessRight(ar)) {
                accessRightsList.add(new VariableReferenceExpression<>(ar, STRING_TYPE));
            } else {
                accessRightsList.add(new StringLiteralExpression(ar));
            }
        }

        ArrayLiteralExpression<String> arList = ArrayLiteralExpression.of(accessRightsList, STRING_TYPE);

        Map<Expression<String>, Expression<Boolean>> containersMap = new HashMap<>();
        for (ContainerCondition cc : prohibition.getContainers()) {
            String contName = policyQuery.graph().getNodeById(cc.getId()).getName();
            StringLiteralExpression s = new StringLiteralExpression(contName);
            containersMap.put(s, new BoolLiteralExpression(cc.isComplement()));
        }

        MapLiteralExpression<String, Boolean> containers = MapLiteralExpression.of(containersMap, STRING_TYPE, BOOLEAN_TYPE);

        ProhibitionSubjectType type;
        StringLiteralExpression subjectName;
        if (prohibition.getSubject().isNode()) {
            Node subjectNode = policyQuery.graph().getNodeById(prohibition.getSubject().getNodeId());
            subjectName = new StringLiteralExpression(subjectNode.getName());

            if (subjectNode.getType() == NodeType.UA) {
                type = ProhibitionSubjectType.USER_ATTRIBUTE;
            } else {
                type = ProhibitionSubjectType.USER;
            }
        } else {
            subjectName = new StringLiteralExpression(prohibition.getSubject().getProcess());
            type = ProhibitionSubjectType.PROCESS;
        }

        return new CreateProhibitionStatement(
            new StringLiteralExpression(prohibition.getName()),
            subjectName,
            type,
            arList,
            prohibition.isIntersection(),
            containers
        );
    }
}
