package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.AdminOperationDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.AdminOperations;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;

import java.io.Serializable;
import java.util.*;

public class EventPattern implements Serializable {

    protected SubjectPattern subjectPattern;
    protected OperationPattern operationPattern;
    protected Map<String, List<OperandPatternExpression>> operandPatterns;

    public EventPattern(SubjectPattern subjectPattern,
                        OperationPattern operationPattern,
                        Map<String, List<OperandPatternExpression>> operandPatterns) {
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
        this.operandPatterns = operandPatterns;
    }

    public EventPattern(SubjectPattern subjectPattern, OperationPattern operationPattern) {
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
        this.operandPatterns = new HashMap<>();
    }

    public SubjectPattern getSubjectPattern() {
        return subjectPattern;
    }

    public void setSubjectPattern(SubjectPattern subjectPattern) {
        this.subjectPattern = subjectPattern;
    }

    public OperationPattern getOperationPattern() {
        return operationPattern;
    }

    public void setOperationPattern(OperationPattern operationPattern) {
        this.operationPattern = operationPattern;
    }

    public Map<String, List<OperandPatternExpression>> getOperandPatterns() {
        return operandPatterns;
    }

    public void setOperandPatterns(Map<String, List<OperandPatternExpression>> operandPatterns) {
        this.operandPatterns = operandPatterns;
    }

    public boolean matches(EventContext eventCtx, PAP pap) throws PMException {
        boolean userMatches = userMatches(eventCtx.getUser(), pap) || processMatches(eventCtx.getProcess(), pap);
        boolean opMatches = operationMatches(eventCtx.getOpName(), pap);
        if (operationPattern.isAny()) {
            return userMatches;
        }

        boolean operandsMatch = operandsMatch(eventCtx.getOpName(), eventCtx.getOperands(), pap);

        return userMatches && opMatches && operandsMatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventPattern that)) return false;
        return Objects.equals(subjectPattern, that.subjectPattern) && Objects.equals(operationPattern, that.operationPattern) && Objects.equals(operandPatterns, that.operandPatterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectPattern, operationPattern, operandPatterns);
    }

    @Override
    public String toString() {
        return "EventPattern[" +
                "subjectPattern=" + subjectPattern + ", " +
                "operationPattern=" + operationPattern + ", " +
                "operandPatterns=" + operandPatterns + ']';
    }

    private boolean userMatches(String user, PAP pap) throws PMException {
        return subjectPattern.matches(user, pap);
    }

    private boolean processMatches(String process, PAP pap) throws PMException {
        return subjectPattern.matches(process, pap);
    }

    private boolean operationMatches(String opName, PAP pap) throws PMException {
        return operationPattern.matches(opName, pap);
    }

    private boolean operandsMatch(String opName, Map<String, Object> operands, PAP pap) throws PMException {
        // get the operands of the operation that represent nodes
        List<String> nodeOperands = getOperationNodeOperands(opName, pap);

        // remove the non node operands from the operands map as they are not available in the pattern
        Map<String, Object> nodeOperandValues = new HashMap<>();
        for (String nodeOperand : nodeOperands) {
            nodeOperandValues.put(nodeOperand, operands.get(nodeOperand));
        }

        // if more patterns than operands - false
        // if no patterns - true (match everything)
        if (operandPatterns.size() > nodeOperandValues.size()) {
            return false;
        } else if (operandPatterns.isEmpty()) {
            return true;
        }

        for (String nodeOperand : nodeOperands) {
            if (!operandPatterns.containsKey(nodeOperand)) {
                continue;
            } else if (!nodeOperandValues.containsKey(nodeOperand)) {
                return false;
            }

            Object operandValue = nodeOperandValues.get(nodeOperand);
            List<OperandPatternExpression> expressions = operandPatterns.get(nodeOperand);

            // needs to match each expression in pattern list
            for (OperandPatternExpression operandPatternExpression : expressions) {
                switch (operandValue) {
                    case null -> {}
                    case String operandValueStr -> {
                        if (!operandPatternExpression.matches(operandValueStr, pap)) {
                            return false;
                        }
                    }
                    case Collection<?> operandValueCollection -> {
                        if (!operandPatternExpression.matches((Collection<String>) operandValueCollection, pap)) {
                            return false;
                        }
                    }
                    default -> throw new UnexpectedOperandTypeException(operandValue.getClass());
                }
            }
        }

        return true;
    }

    private List<String> getOperationNodeOperands(String opName, PAP pap) throws PMException {
        if (pap.query().operations().getResourceOperations().contains(opName)) {
            return List.of("target");
        }

        // check if operation is PM admin op or custom admin op
        Operation<?> adminOp;
        try {
            adminOp = AdminOperations.get(opName);
        } catch (AdminOperationDoesNotExistException e) {
            adminOp = pap.query().operations().getAdminOperation(opName);
        }

        return adminOp.getNodeOperandNames();
    }
}
