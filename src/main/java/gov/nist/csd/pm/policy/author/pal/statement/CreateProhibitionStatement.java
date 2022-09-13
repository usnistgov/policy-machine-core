package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateProhibitionStatement extends PALStatement {

    private final Expression label;
    private final Expression subject;
    private final ProhibitionSubject.Type subjectType;
    private final Expression accessRights;
    private final boolean isIntersection;
    private final List<Container> containers;

    public CreateProhibitionStatement(Expression label, Expression subject, ProhibitionSubject.Type subjectType, Expression accessRights,
                                      boolean isIntersection, List<Container> containers) {
        this.label = label;
        this.subject = subject;
        this.subjectType = subjectType;
        this.accessRights = accessRights;
        this.isIntersection = isIntersection;
        this.containers = containers;
    }

    public Expression getLabel() {
        return label;
    }

    public Expression getSubject() {
        return subject;
    }

    public ProhibitionSubject.Type getSubjectType() {
        return subjectType;
    }

    public Expression getAccessRights() {
        return accessRights;
    }

    public boolean isIntersection() {
        return isIntersection;
    }

    public List<Container> getContainers() {
        return containers;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value labelValue = this.label .execute(ctx, policyAuthor);
        Value subjectValue = this.subject.execute(ctx, policyAuthor);
        Value permissionsValue = this.accessRights.execute(ctx, policyAuthor);

        Value[] arrayValue = permissionsValue.getArrayValue();
        AccessRightSet ops = new AccessRightSet();
        for (Value v : arrayValue) {
            ops.add(v.getStringValue());
        }

        List<ContainerCondition> containerConditions = new ArrayList<>();
        for (Container container : containers) {
            boolean isComplement = container.isComplement;
            Value containerValue = container.expression.execute(ctx, policyAuthor);
            containerConditions.add(new ContainerCondition(containerValue.getStringValue(), isComplement));
        }


        policyAuthor.prohibitions().create(
                labelValue.getStringValue(),
                new ProhibitionSubject(subjectValue.getStringValue(), subjectType),
                ops,
                isIntersection,
                containerConditions.toArray(new ContainerCondition[]{})
        );

        return new Value();
    }

    @Override
    public String toString() {
        String subjectStr = "";
        switch (subjectType) {
            case USER_ATTRIBUTE -> subjectStr = "user attribute";
            case USER -> subjectStr = "user";
            case PROCESS -> subjectStr = "process";
        }

        StringBuilder containerStr = new StringBuilder((isIntersection ? "intersection" : "union") + " of ");
        int length = containerStr.length();
        for (Container c : containers) {
            if (containerStr.length() != length) {
                containerStr.append(", ");
            }
            containerStr.append(c);
        }

        return String.format("create prohibition %s deny %s %s access rights %s on %s;", label, subjectStr, subject, accessRights, containerStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateProhibitionStatement that = (CreateProhibitionStatement) o;
        return isIntersection == that.isIntersection && Objects.equals(label, that.label) && Objects.equals(subject, that.subject) && Objects.equals(accessRights, that.accessRights) && Objects.equals(containers, that.containers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, subject, accessRights, isIntersection, containers);
    }

    public static class Container {
        private final boolean isComplement;
        private final Expression expression;

        public Container(boolean isComplement, Expression expression) {
            this.isComplement = isComplement;
            this.expression = expression;
        }

        @Override
        public String toString() {
            return (isComplement ? "!" : "") + expression;
        }
    }

    public static CreateProhibitionStatement fromProhibition(Prohibition prohibition) {
        List<Expression> exprs = new ArrayList<>();
        for (String ar : prohibition.getAccessRightSet()) {
            exprs.add(new Expression(new VariableReference(ar, Type.string())));
        }

        List<Container> containers = new ArrayList<>();
        for (ContainerCondition cc : prohibition.getContainers()) {
            containers.add(new Container(cc.complement(), new Expression(new Literal(cc.name()))));
        }

        return new CreateProhibitionStatement(
                new Expression(new Literal(prohibition.getLabel())),
                new Expression(new Literal(prohibition.getSubject().name())),
                prohibition.getSubject().type(),
                new Expression(new Literal(new ArrayLiteral(exprs.toArray(Expression[]::new), Type.string()))),
                prohibition.isIntersection(),
                containers
        );
    }
}
