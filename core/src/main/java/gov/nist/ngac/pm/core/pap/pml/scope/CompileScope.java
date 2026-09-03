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
import gov.nist.ngac.pm.core.pap.operation.AdminOperations;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.builtin.PMLBuiltinOperations;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Scope} used during PML compilation.
 */
public class CompileScope extends Scope<Variable, PMLOperationSignature> {

    public CompileScope(PAP pap) throws PMException {
        super(pap, ScopeUtil.loadConstants(), loadOperations(pap));
    }

    private CompileScope(PAP pap,
                         Map<String, Variable> constants,
                         Map<String, Variable> variables,
                         Map<String, PMLOperationSignature> operations,
                         Scope<Variable, PMLOperationSignature> parentScope) {
        super(pap, constants, variables, operations, parentScope);
    }

    @Override
    public CompileScope copy() {
        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getOperations()),
            this.getParentScope() != null ? this.getParentScope().copy() : null
        );
    }

    @Override
    public CompileScope copyFunctionsOnly() {
        Map<String, PMLOperationSignature> operations = new HashMap<>();
        for (PMLOperationSignature op : getOperations().values()) {
            if (!ScopeUtil.isFunction(op)) {
                continue;
            }

            operations.put(op.getName(), op);
        }

        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            operations,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public CompileScope copyFunctionsAndQueriesOnly() {
        Map<String, PMLOperationSignature> filteredOps = new HashMap<>();
        for (PMLOperationSignature function : getOperations().values()) {
            if (ScopeUtil.isFunctionOrQuery(function)) {
                filteredOps.put(function.getName(), function);
            }
        }

        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredOps,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    private static Map<String, PMLOperationSignature> loadOperations(PAP pap) throws PMException {
        Map<String, PMLOperationSignature> operationSignatures = new HashMap<>();

        // add builtin operations and routines stored in PAP
        Map<String, Operation<?>> builtinFuncs = PMLBuiltinOperations.builtinOperations();
        builtinFuncs.values().forEach(f -> {
            operationSignatures.put(f.getName(), ScopeUtil.createOperationSignature(f));
        });

        Collection<Operation<?>> operations = pap.query().operations().getOperations();
        for (Operation<?> op : operations) {
            operationSignatures.put(op.getName(), ScopeUtil.createOperationSignature(op));
        }

        // add admin ops
        for (Operation<?> adminOperation : AdminOperations.ADMIN_OPERATIONS) {
            operationSignatures.put(adminOperation.getName(), ScopeUtil.createOperationSignature(adminOperation));
        }

        return operationSignatures;
    }
}
