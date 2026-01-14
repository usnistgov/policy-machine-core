package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        boolean userMatches = subjectPattern.matches(eventCtx.user(), pap);
        boolean opMatches = operationPattern.matches(eventCtx.opName(), pap);

        // if the pattern states any operation, return if the user matched
        if (operationPattern.isAny()) {
            return userMatches;
        }

        boolean argsMatch = argsMatch(eventCtx.args(), pap);

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

    private boolean argsMatch(Map<String, Object> args, PAP pap) throws PMException {
        // if there are patterns defined that are not in args, return false as not all patterns can be satisfied
        if (!args.keySet().containsAll(argPatterns.keySet())) {
            return false;
        }

        // for each arg, if there is a pattern with the same key, check the value against the pattern
        for (Map.Entry<String, Object> arg : args.entrySet()) {
            String argKey = arg.getKey();
            Object argValue = arg.getValue();

            // if the pattern does not include this arg, ignore
            if (!argPatterns.containsKey(argKey)) {
                continue;
            }

            // needs to satisfy each expression in pattern list
            List<ArgPatternExpression> expressions = argPatterns.get(arg.getKey());
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
