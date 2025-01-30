package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.operand.ListStringOperandValue;
import gov.nist.csd.pm.common.event.operand.OperandValue;
import gov.nist.csd.pm.common.event.operand.StringOperandValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.Operation;
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
        return subjectPattern.matches(new StringOperandValue(user), pap);
    }

    private boolean processMatches(String process, PAP pap) throws PMException {
        return subjectPattern.matches(new StringOperandValue(process), pap);
    }

    private boolean operationMatches(String opName, PAP pap) throws PMException {
        return operationPattern.matches(new StringOperandValue(opName), pap);
    }

    private boolean operandsMatch(String opName, Map<String, OperandValue> operands, PAP pap) throws PMException {
        // get the operands of the operation that represent nodes
        List<String> nodeOperands = getOperationNodeOperands(opName, pap);

        // if more patterns than operands - false
        // if no patterns - true (match everything)
        if (operandPatterns.size() > operands.size()) {
            return false;
        } else if (operandPatterns.isEmpty()) {
            return true;
        }

        for (String nodeOperand : nodeOperands) {
            if (!operandPatterns.containsKey(nodeOperand)) {
                continue;
            } else if (!operands.containsKey(nodeOperand)) {
                return false;
            }

            OperandValue operandValue = operands.get(nodeOperand);
            List<OperandPatternExpression> expressions = operandPatterns.get(nodeOperand);

            // needs to match each expression in pattern list
            for (OperandPatternExpression operandPatternExpression : expressions) {
                switch (operandValue) {
                    case null -> {}
                    case StringOperandValue stringOperandValue -> {
                        if (!operandPatternExpression.matches(stringOperandValue, pap)) {
                            return false;
                        }
                    }
                    case ListStringOperandValue listStringOperandValue -> {
                        if (!operandPatternExpression.matches(listStringOperandValue, pap)) {
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

        Operation<?> adminOperation = pap.query().operations().getAdminOperation(opName);
        return adminOperation.getNodeOperandNames();
    }
}
