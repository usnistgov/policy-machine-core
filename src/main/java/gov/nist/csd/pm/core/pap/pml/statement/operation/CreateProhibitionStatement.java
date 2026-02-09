package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp.PROCESS_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp.USER_ID_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.EXCLUSION_SET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.INCLUSION_SET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.IS_CONJUNCTIVE_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.NODE_ID_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightValidator;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateNodeProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateProhibitionStatement extends OperationStatement {

    public static CreateProhibitionStatement nodeProhibition(Expression<String> name,
                                                             Expression<String> nodeName,
                                                             Expression<List<String>> accessRights,
                                                             Expression<List<String>> inclusionSet,
                                                             Expression<List<String>> exclusionSet,
                                                             boolean isConjunctive) {
        return new CreateProhibitionStatement(new CreateNodeProhibitionOp(), name, nodeName, null,
            accessRights, inclusionSet, exclusionSet, isConjunctive);
    }

    public static CreateProhibitionStatement processProhibition(Expression<String> name,
                                                                Expression<String> nodeName,
                                                                Expression<String> process,
                                                                Expression<List<String>> accessRights,
                                                                Expression<List<String>> inclusionSet,
                                                                Expression<List<String>> exclusionSet,
                                                                boolean isConjunctive) {
        return new CreateProhibitionStatement(new CreateProcessProhibitionOp(), name, nodeName, process,
            accessRights, inclusionSet, exclusionSet, isConjunctive);
    }

    private final Expression<String> nameExpr;
    private final Expression<String> nodeExpr;
    private final Expression<String> processExpr;
    private final Expression<List<String>> accessRightsExpr;
    private final Expression<List<String>> inclusionSetExpr;
    private final Expression<List<String>> exclusionSetExpr;
    private final boolean isConjunctive;

    private CreateProhibitionStatement(Operation<?> op,
                                       Expression<String> nameExpr,
                                       Expression<String> nodeExpr,
                                       Expression<String> processExpr,
                                       Expression<List<String>> accessRightsExpr,
                                       Expression<List<String>> inclusionSetExpr,
                                       Expression<List<String>> exclusionSetExpr,
                                       boolean isConjunctive) {
        super(op);
        this.nameExpr = nameExpr;
        this.nodeExpr = nodeExpr;
        this.processExpr = processExpr;
        this.accessRightsExpr = accessRightsExpr;
        this.inclusionSetExpr = inclusionSetExpr;
        this.exclusionSetExpr = exclusionSetExpr;
        this.isConjunctive = isConjunctive;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        boolean isNodeProhibition = processExpr == null;

        String name = this.nameExpr.execute(ctx, pap);
        String nodeName = this.nodeExpr.execute(ctx, pap);
        long nodeId = pap.query().graph().getNodeId(nodeName);
        AccessRightSet arset = new AccessRightSet(this.accessRightsExpr.execute(ctx, pap));
        AccessRightValidator.validateAccessRights(pap.query().operations().getResourceAccessRights(), arset);

        List<String> inclusionSetNames = inclusionSetExpr.execute(ctx, pap);
        List<Long> inclusionIds = new ArrayList<>();
        for (String incName : inclusionSetNames) {
            inclusionIds.add(pap.query().graph().getNodeId(incName));
        }

        List<String> exclusionSetNames = exclusionSetExpr.execute(ctx, pap);
        List<Long> exclusionIds = new ArrayList<>();
        for (String excName : exclusionSetNames) {
            exclusionIds.add(pap.query().graph().getNodeId(excName));
        }

        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(ARSET_PARAM, new ArrayList<>(arset))
            .put(INCLUSION_SET_PARAM, inclusionIds)
            .put(EXCLUSION_SET_PARAM, exclusionIds)
            .put(IS_CONJUNCTIVE_PARAM, isConjunctive);

        if (isNodeProhibition) {
            args.put(NODE_ID_PARAM, nodeId);
        } else {
            String process = processExpr.execute(ctx, pap);
            args.put(USER_ID_PARAM, nodeId)
                .put(PROCESS_PARAM, process);
        }

        return args;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        boolean isNodeProhibition = processExpr == null;

        String conjType = isConjunctive ? "conj" : "disj";
        String entityType = isNodeProhibition ? "node" : "process";
        String indent = indent(indentLevel);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%screate %s %s prohibition %s\n",
            indent, conjType, entityType, nameExpr.toFormattedString(0)));
        sb.append(String.format("%sdeny %s",
            indent, nodeExpr.toFormattedString(0)));
        if (!isNodeProhibition) {
            sb.append(String.format(" process %s", processExpr.toFormattedString(0)));
        }
        sb.append("\n");
        sb.append(String.format("%sarset %s\n",
            indent, accessRightsExpr.toFormattedString(0)));
        sb.append(String.format("%sinclude %s\n",
            indent, inclusionSetExpr.toFormattedString(0)));
        sb.append(String.format("%sexclude %s",
            indent, exclusionSetExpr.toFormattedString(0)));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateProhibitionStatement that = (CreateProhibitionStatement) o;
        return isConjunctive == that.isConjunctive && Objects.equals(nameExpr, that.nameExpr)
            && Objects.equals(nodeExpr, that.nodeExpr) && Objects.equals(processExpr, that.processExpr)
            && Objects.equals(accessRightsExpr, that.accessRightsExpr) && Objects.equals(
            inclusionSetExpr, that.inclusionSetExpr) && Objects.equals(exclusionSetExpr, that.exclusionSetExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameExpr, nodeExpr, processExpr, accessRightsExpr, inclusionSetExpr, exclusionSetExpr,
            isConjunctive);
    }

    public static CreateProhibitionStatement fromProhibition(PolicyQuery policyQuery, Prohibition prohibition) throws PMException {
        List<Expression<String>> accessRightsList = new ArrayList<>();
        for (String ar : prohibition.getAccessRightSet()) {
            accessRightsList.add(new StringLiteralExpression(ar));
        }

        ArrayLiteralExpression<String> arList = ArrayLiteralExpression.of(accessRightsList, STRING_TYPE);

        List<Expression<String>> inclusionSet = new ArrayList<>();
        for (long inc : prohibition.getInclusionSet()) {
            String incName = policyQuery.graph().getNodeById(inc).getName();
            StringLiteralExpression s = new StringLiteralExpression(incName);
            inclusionSet.add(s);
        }

        List<Expression<String>> exclusionSet = new ArrayList<>();
        for (long exc : prohibition.getExclusionSet()) {
            String excName = policyQuery.graph().getNodeById(exc).getName();
            StringLiteralExpression s = new StringLiteralExpression(excName);
            exclusionSet.add(s);
        }

        ProhibitionOp op;
        Expression<String> nodeName;
        Expression<String> process = null;
        switch (prohibition) {
            case NodeProhibition nodeProhibition ->{
                op = new CreateNodeProhibitionOp();
                nodeName = new StringLiteralExpression(policyQuery.graph().getNodeById(nodeProhibition.getNodeId()).getName());
            }
            case ProcessProhibition processProhibition -> {
                op = new CreateProcessProhibitionOp();
                nodeName = new StringLiteralExpression(policyQuery.graph().getNodeById(processProhibition.getUserId()).getName());
                process = new StringLiteralExpression(processProhibition.getProcess());
            }
        };

        return new CreateProhibitionStatement(
            op,
            new StringLiteralExpression(prohibition.getName()),
            nodeName,
            process,
            arList,
            ArrayLiteralExpression.of(inclusionSet, STRING_TYPE),
            ArrayLiteralExpression.of(exclusionSet, STRING_TYPE),
            prohibition.isConjunctive()
        );
    }
}
