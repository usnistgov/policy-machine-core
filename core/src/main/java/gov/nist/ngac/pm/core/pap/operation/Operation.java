/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A named, callable operation with parameters, required capabilities, and a body to execute. Subclasses
 * differ in how much policy access they're allowed during execution.
 *
 * @param <R> the operation's return type
 */
public abstract sealed class Operation<R> permits AdminOperation, ResourceOperation,
    QueryOperation, Routine, Function {

    public static final FormalParameter<String> NAME_PARAM = new FormalParameter<>("name", STRING_TYPE);
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));

    protected final String name;
    protected final Type<R> returnType;
    protected final List<FormalParameter<?>> parameters;
    protected final List<FormalParameter<?>> eventParameters;
    protected final List<RequiredCapability> requiredCapabilities;

    public Operation(String name, Type<R> returnType, List<FormalParameter<?>> parameters, List<RequiredCapability> requiredCapabilities) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.eventParameters = new ArrayList<>(parameters);
        this.requiredCapabilities = requiredCapabilities;
    }

    public Operation(String name, Type<R> returnType, List<FormalParameter<?>> parameters,
                     RequiredCapability requiredCapability, RequiredCapability... requiredCapabilities) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.eventParameters = new ArrayList<>(parameters);
        this.requiredCapabilities = new ArrayList<>();
        this.requiredCapabilities.add(requiredCapability);
        this.requiredCapabilities.addAll(List.of(requiredCapabilities));
    }

    public Operation(String name,
                     Type<R> returnType,
                     List<FormalParameter<?>> parameters,
                     List<FormalParameter<?>> eventParameters,
                     List<RequiredCapability> requiredCapabilities) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.eventParameters = eventParameters;
        this.requiredCapabilities = requiredCapabilities;
    }

    public Operation(String name,
                     Type<R> returnType,
                     List<FormalParameter<?>> parameters,
                     List<FormalParameter<?>> eventParameters,
                     RequiredCapability requiredCapability,
                     RequiredCapability... requiredCapabilities) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.eventParameters = eventParameters;
        this.requiredCapabilities = new ArrayList<>();
        this.requiredCapabilities.add(requiredCapability);
        this.requiredCapabilities.addAll(List.of(requiredCapabilities));
    }

    /**
     * Executes the operation with the given arguments.
     *
     * @param pap the PAP to execute against
     * @param userCtx the user executing the operation
     * @param args the operation's arguments
     * @return the operation's return value
     * @throws PMException if execution fails
     */
    public abstract R execute(PAP pap, UserContext userCtx, Args args) throws PMException;

    public String getName() {
        return name;
    }

    public Type<R> getReturnType() {
        return returnType;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return parameters;
    }

    public List<FormalParameter<?>> getRequiredFormalParameters() {
        return parameters.stream().filter(FormalParameter::isRequired).toList();
    }

    public List<RequiredCapability> getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public List<FormalParameter<?>> getEventParameters() {
        return eventParameters;
    }

    /**
     * Converts raw argument values to a type-checked {@link Args}.
     *
     * @param rawArgs the raw argument values, keyed by parameter name
     * @return the type-checked args
     * @throws IllegalArgumentException if the args don't match the operation's parameters, or a value has the wrong type
     */
    public Args validateArgs(Map<String, Object> rawArgs) {
        Set<String> rawArgNames = new HashSet<>(rawArgs.keySet());
        Set<String> allParamNames = new HashSet<>(parameters.stream().map(FormalParameter::getName).toList());
        Set<String> requiredParamNames = new HashSet<>(parameters.stream()
            .filter(FormalParameter::isRequired)
            .map(FormalParameter::getName)
            .toList());

        // check for unexpected args
        if (!allParamNames.containsAll(rawArgNames)) {
            throw new IllegalArgumentException("unexpected args " + rawArgNames + ", expected " + allParamNames);
        }

        // check for required args
        if (!rawArgNames.containsAll(requiredParamNames)) {
            throw new IllegalArgumentException("required args " + requiredParamNames + ", received " + rawArgNames);
        }

        Map<String, FormalParameter<?>> paramMap = parameters.stream()
            .collect(Collectors.toMap(FormalParameter::getName, java.util.function.Function.identity()));

        return new Args(buildTypeSafeArgs(rawArgs, paramMap));
    }

    /**
     * Converts raw event context values to a type-checked {@link Args}, allowing a subset of the event
     * parameters to be present.
     *
     * @param rawArgs the raw argument values, keyed by parameter name
     * @return the type-checked args
     * @throws IllegalArgumentException if an argument isn't one of the operation's event parameters
     */
    public Args validateEventContextArgs(Map<String, Object> rawArgs) {
        Set<String> rawArgNames = new HashSet<>(rawArgs.keySet());
        Set<String> eventParamNames = new HashSet<>(eventParameters.stream().map(FormalParameter::getName).toList());

        // error on unexpected args - ok if not all args
        if (!eventParamNames.containsAll(rawArgNames)) {
            throw new IllegalArgumentException("expected subset of event context args " + eventParamNames + ", received " + rawArgNames);
        }

        Map<String, FormalParameter<?>> paramMap = eventParameters.stream()
            .collect(Collectors.toMap(FormalParameter::getName, java.util.function.Function.identity()));

        return new Args(buildTypeSafeArgs(rawArgs, paramMap));
    }

    /**
     * Checks that the given user can perform this operation with the given args.
     *
     * @param pap the PAP to query access against
     * @param userCtx the user trying to execute the operation
     * @param args the operation's arguments
     * @throws UnauthorizedException if the user doesn't satisfy any required capability
     * @throws PMException if checking access fails
     */
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        if (requiredCapabilities.isEmpty()) {
            return;
        }

        for (RequiredCapability reqCap : requiredCapabilities) {
            if (reqCap.isSatisfied(pap, userCtx, args)) {
                return;
            }
        }

        throw UnauthorizedException.of(pap.query().graph(), userCtx, getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation<?> operation = (Operation<?>) o;
        return Objects.equals(name, operation.name) && Objects.equals(returnType, operation.returnType)
            && Objects.equals(parameters, operation.parameters) && Objects.equals(requiredCapabilities,
            operation.requiredCapabilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, parameters, requiredCapabilities);
    }

    private Map<FormalParameter<?>, Object> buildTypeSafeArgs(Map<String, Object> rawArgs,
                                                              Map<String, FormalParameter<?>> paramMap) {
        Map<FormalParameter<?>, Object> argsWithFormalParams = new HashMap<>();
        for (Entry<String, Object> entry : rawArgs.entrySet()) {
            FormalParameter<?> param = paramMap.get(entry.getKey());
            Object value = entry.getValue();
            validateArgType(entry.getKey(), param, value);
            argsWithFormalParams.put(param, value);
        }

        return argsWithFormalParams;
    }

    private void validateArgType(String argName, FormalParameter<?> param, Object value) {
        if (value != null && !param.getType().isCastableTo(Type.resolveTypeOfObject(value))) {
            throw new IllegalArgumentException(
                String.format("Invalid type for argument '%s'. Expected %s but got %s",
                    argName, param.getType().getClass().getSimpleName(),
                    Type.resolveTypeOfObject(value).getClass().getSimpleName()));
        }
    }
}