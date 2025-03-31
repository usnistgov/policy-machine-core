package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;

import java.io.Serializable;
import java.util.*;

public class EventPattern implements Serializable {

    protected SubjectPattern subjectPattern;
    protected OperationPattern operationPattern;
    protected Map<String, List<ArgPatternExpression>> argPatterns;

    public EventPattern(SubjectPattern subjectPattern,
                        OperationPattern operationPattern,
                        Map<String, List<ArgPatternExpression>> argPatterns) {
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
        this.argPatterns = argPatterns;
    }

    public EventPattern(SubjectPattern subjectPattern, OperationPattern operationPattern) {
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
        this.argPatterns = new HashMap<>();
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

    public Map<String, List<ArgPatternExpression>> getArgPatterns() {
        return argPatterns;
    }

    public void setArgPatterns(Map<String, List<ArgPatternExpression>> argPatterns) {
        this.argPatterns = argPatterns;
    }

    public boolean matches(EventContext eventCtx, PAP pap) throws PMException {
        boolean userMatches = userMatches(eventCtx.getUser(), pap) || processMatches(eventCtx.getProcess(), pap);
        boolean opMatches = operationMatches(eventCtx.getOpName(), pap);
        if (operationPattern.isAny()) {
            return userMatches;
        }

        boolean argsMatch = argsMatch(eventCtx.getArgs(), pap);

        return userMatches && opMatches && argsMatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventPattern that)) return false;
        return Objects.equals(subjectPattern, that.subjectPattern) && Objects.equals(operationPattern, that.operationPattern) && Objects.equals(
            argPatterns, that.argPatterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectPattern, operationPattern, argPatterns);
    }

    @Override
    public String toString() {
        return "EventPattern[" +
                "subjectPattern=" + subjectPattern + ", " +
                "operationPattern=" + operationPattern + ", " +
                "argPatterns=" + argPatterns + ']';
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

    private boolean argsMatch(Map<String, Object> args, PAP pap) throws PMException {
        if (!args.keySet().containsAll(argPatterns.keySet())) {
            return false;
        }

        for (Map.Entry<String, Object> arg : args.entrySet()) {
            if (!argPatterns.containsKey(arg.getKey())) {
                continue;
            }

            Object argValue = arg.getValue();
            List<ArgPatternExpression> expressions = argPatterns.get(arg.getKey());

            // needs to match each expression in pattern list
            for (ArgPatternExpression argPatternExpression : expressions) {
                switch (argValue) {
                    case null -> {}
                    case String argValueStr -> {
                        if (!argPatternExpression.matches(argValueStr, pap)) {
                            return false;
                        }
                    }
                    case Collection<?> argValueCollection -> {
                        if (!argPatternExpression.matches((Collection<String>) argValueCollection, pap)) {
                            return false;
                        }
                    }
                    default -> throw new UnexpectedArgTypeException(argValue.getClass());
                }
            }
        }

        return true;
    }
}
