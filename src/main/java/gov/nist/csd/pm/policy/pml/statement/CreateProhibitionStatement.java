package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.expression.*;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.pml.type.Type;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.ComplementedValue;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

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
    private final Expression containers;

    public CreateProhibitionStatement(Expression name, Expression subject, ProhibitionSubject.Type subjectType, Expression accessRights,
                                      boolean isIntersection, Expression containers) {
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

    public Expression getContainers() {
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
        for (Value container : containers.execute(ctx, policy).getArrayValue()) {
            boolean isComplement = container instanceof ComplementedValue;
            String containerName = container.getStringValue();

            containerConditions.add(new ContainerCondition(containerName, isComplement));
        }


        policy.prohibitions().create(
                idValue.getStringValue(),
                new ProhibitionSubject(subjectValue.getStringValue(), subjectType),
                ops,
                isIntersection,
                containerConditions.toArray(new ContainerCondition[]{})
        );

        return new VoidValue();
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
                indent, name, indent, subjectStr, subject, indent, accessRights, indent, (isIntersection ? "intersection" : "union"), containers
        );
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

    private String getSubjectStr() {
        String subjectStr = "";
        switch (subjectType) {
            case USER_ATTRIBUTE -> subjectStr = "UA";
            case USER -> subjectStr = "U";
            case PROCESS -> subjectStr = "process";
        }

        return subjectStr;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Container container = (Container) o;
            return isComplement == container.isComplement && Objects.equals(name, container.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isComplement, name);
        }
    }

    public static CreateProhibitionStatement fromProhibition(Prohibition prohibition) {
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String ar : prohibition.getAccessRightSet()) {
            if (isAdminAccessRight(ar)) {
                arrayLiteral.add(new ReferenceByID(ar));
            } else {
                arrayLiteral.add(new StringLiteral(ar));
            }
        }

        ArrayLiteral containers = new ArrayLiteral(Type.string());
        for (ContainerCondition cc : prohibition.getContainers()) {
            StringLiteral s = new StringLiteral(cc.getName());
            if (cc.isComplement()) {
                containers.add(new NegatedExpression(s));
            } else {
                containers.add(s);
            }
        }

        return new CreateProhibitionStatement(
                new StringLiteral(prohibition.getName()),
                new StringLiteral(prohibition.getSubject().getName()),
                prohibition.getSubject().getType(),
                arrayLiteral,
                prohibition.isIntersection(),
                containers
        );
    }
}
