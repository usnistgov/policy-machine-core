package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.pml.model.expression.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.isAdminAccessRight;

public class CreateProhibitionStatement extends PMLStatement {

    private final Expression name;
    private final Expression subject;
    private final ProhibitionSubject.Type subjectType;
    private final Expression accessRights;
    private final boolean isIntersection;
    private final List<Container> containers;

    public CreateProhibitionStatement(Expression name, Expression subject, ProhibitionSubject.Type subjectType, Expression accessRights,
                                      boolean isIntersection, List<Container> containers) {
        this.name = name;
        this.subject = subject;
        this.subjectType = subjectType;
        this.accessRights = accessRights;
        this.isIntersection = isIntersection;
        this.containers = containers;
    }

    public Expression getName() {
        return name;
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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value idValue = this.name .execute(ctx, policy);
        Value subjectValue = this.subject.execute(ctx, policy);
        Value permissionsValue = this.accessRights.execute(ctx, policy);

        List<Value> arrayValue = permissionsValue.getArrayValue();
        AccessRightSet ops = new AccessRightSet();
        for (Value v : arrayValue) {
            ops.add(v.getStringValue());
        }

        List<ContainerCondition> containerConditions = new ArrayList<>();
        for (Container container : containers) {
            boolean isComplement = container.isComplement;
            Value containerValue = container.name.execute(ctx, policy);
            containerConditions.add(new ContainerCondition(containerValue.getStringValue(), isComplement));
        }


        policy.prohibitions().create(
                idValue.getStringValue(),
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

        StringBuilder containerStr = new StringBuilder((isIntersection ? "intersection" : "union") + " of [");
        int length = containerStr.length();
        for (Container c : containers) {
            if (containerStr.length() != length) {
                containerStr.append(", ");
            }
            containerStr.append(c);
        }
        containerStr.append("]");

        return String.format("create prohibition %s deny %s %s access rights %s on %s", name, subjectStr, subject, accessRights, containerStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateProhibitionStatement that = (CreateProhibitionStatement) o;
        return isIntersection == that.isIntersection && Objects.equals(name, that.name) && Objects.equals(subject, that.subject) && Objects.equals(accessRights, that.accessRights) && Objects.equals(containers, that.containers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, accessRights, isIntersection, containers);
    }

    public static class Container implements Serializable {
        private final boolean isComplement;
        private final Expression name;

        public Container(boolean isComplement, Expression name) {
            this.isComplement = isComplement;
            this.name = name;
        }

        @Override
        public String toString() {
            return (isComplement ? "!" : "") + name;
        }
    }

    public static CreateProhibitionStatement fromProhibition(Prohibition prohibition) {
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String ar : prohibition.getAccessRightSet()) {
            if (isAdminAccessRight(ar)) {
                arrayLiteral.add(new Expression(new VariableReference(ar, Type.string())));
            } else {
                arrayLiteral.add(new Expression(new Literal(ar)));
            }
        }

        List<Container> containers = new ArrayList<>();
        for (ContainerCondition cc : prohibition.getContainers()) {
            containers.add(new Container(cc.complement(), new Expression(new Literal(cc.name()))));
        }

        return new CreateProhibitionStatement(
                new Expression(new Literal(prohibition.getName())),
                new Expression(new Literal(prohibition.getSubject().getName())),
                prohibition.getSubject().getType(),
                new Expression(new Literal(arrayLiteral)),
                prohibition.isIntersection(),
                containers
        );
    }
}
