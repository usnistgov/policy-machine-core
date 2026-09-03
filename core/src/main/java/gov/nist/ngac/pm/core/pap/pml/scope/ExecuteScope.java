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

package gov.nist.ngac.pm.core.pap.pml.scope;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.Function;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.builtin.PMLBuiltinOperations;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Scope} used during PML execution.
 */
public class ExecuteScope extends Scope<Object, Operation<?>> {

    public ExecuteScope(PAP pap) throws PMException {
        super(pap, loadConstants(), loadOperations(pap));
    }

    private ExecuteScope(PAP pap,
                        Map<String, Object> constants,
                        Map<String, Object> variables,
                        Map<String, Operation<?>> operations,
                        Scope<Object, Operation<?>> parentScope) {
        super(pap, constants, variables, operations, parentScope);
    }

    @Override
    public Scope<Object, Operation<?>> copy() {
        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getOperations()),
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public Scope<Object, Operation<?>> copyFunctionsOnly() {
        Map<String, Operation<?>> filteredOps = new HashMap<>();
        for (Operation<?> operation : getOperations().values()) {
            if (!(operation instanceof Function<?> function)) {
                continue;
            }

            filteredOps.put(function.getName(), function);
        }

        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredOps,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public Scope<Object, Operation<?>> copyFunctionsAndQueriesOnly() {
        Map<String, Operation<?>> filteredOps = new HashMap<>();
        for (Operation<?> operation : getOperations().values()) {
            if (operation instanceof Function<?> || operation instanceof QueryOperation<?>) {
                filteredOps.put(operation.getName(), operation);
            }
        }

        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredOps,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    private static Map<String, Object> loadConstants() {
        Map<String, Object> constants = new HashMap<>();
        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            constants.put(adminPolicyNode.constantName(), adminPolicyNode.nodeName());
        }

        return constants;
    }

    private static Map<String, Operation<?>> loadOperations(PAP pap) throws PMException {
        // add pml operations and routines stored in PAP
        Map<String, Operation<?>> operationsMap = new HashMap<>(PMLBuiltinOperations.builtinOperations());

        Collection<Operation<?>> operations = pap.query().operations().getOperations();
        for (Operation<?> operation : operations) {
            operationsMap.put(operation.getName(), operation);
        }

        return operationsMap;
    }
}
