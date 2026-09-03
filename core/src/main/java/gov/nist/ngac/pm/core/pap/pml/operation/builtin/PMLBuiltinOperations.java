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

package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import gov.nist.ngac.pm.core.pap.operation.Operation;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of the PML built-in operations, keyed by their PML operation name.
 */
public class PMLBuiltinOperations {

    // util operations
    private static final Contains contains = new Contains();
    private static final ContainsKey containsKey = new ContainsKey();
    private static final Env env = new Env();

    // policy operations
    private static final GetAssociationsWithSource getAssociationsWithSource = new GetAssociationsWithSource();
    private static final GetAssociationsWithTarget getAssociationsWithTarget = new GetAssociationsWithTarget();
    private static final GetAdjacentAscendants getAdjacentAscendants = new GetAdjacentAscendants();
    private static final GetAdjacentDescendants getAdjacentDescendants = new GetAdjacentDescendants();
    private static final GetNodeProperties getNodeProperties = new GetNodeProperties();
    private static final GetNodeType getNodeType = new GetNodeType();
    private static final HasPropertyKey hasPropertyKey = new HasPropertyKey();
    private static final HasPropertyValue hasPropertyValue = new HasPropertyValue();
    private static final NodeExists nodeExists = new NodeExists();
    private static final GetNode getNode = new GetNode();
    private static final Search search = new Search();
    private static final Append append = new Append();
    private static final AppendAll appendAll = new AppendAll();
    private static final Name name = new Name();
    private static final Id id = new Id();
    private static final IsNull isNull = new IsNull();
    private static final GetPolicyClassIds getPolicyClassIds = new GetPolicyClassIds();
    private static final SelfAccessComputePrivileges selfAccessComputePrivileges = new SelfAccessComputePrivileges();
    private static final SelfAccessComputeAdjacentAscendantPrivileges selfAccessComputeAdjacentAscendantPrivileges =
        new SelfAccessComputeAdjacentAscendantPrivileges();
    private static final SelfAccessComputePrivilegesBatch selfAccessComputePrivilegesBatch = new SelfAccessComputePrivilegesBatch();
    private static final SelfAccessComputeDeniedPrivileges selfAccessComputeDeniedPrivileges = new SelfAccessComputeDeniedPrivileges();
    private static final SelfAccessComputeSubgraphPrivileges selfAccessComputeSubgraphPrivileges = new SelfAccessComputeSubgraphPrivileges();
    private static final SelfAccessComputeAdjacentDescendantPrivileges selfAccessComputeAdjacentDescendantPrivileges =
        new SelfAccessComputeAdjacentDescendantPrivileges();
    private static final SelfAccessComputePersonalObjectSystem selfAccessComputePersonalObjectSystem = new SelfAccessComputePersonalObjectSystem();


    private static final Map<String, Operation<?>> BUILTIN_OPERATIONS = new HashMap<>();

    static {
        BUILTIN_OPERATIONS.put(contains.getName(), contains);
        BUILTIN_OPERATIONS.put(containsKey.getName(), containsKey);
        BUILTIN_OPERATIONS.put(appendAll.getName(), appendAll);
        BUILTIN_OPERATIONS.put(append.getName(), append);
        BUILTIN_OPERATIONS.put(env.getName(), env);

        BUILTIN_OPERATIONS.put(getAssociationsWithSource.getName(), getAssociationsWithSource);
        BUILTIN_OPERATIONS.put(getAssociationsWithTarget.getName(), getAssociationsWithTarget);
        BUILTIN_OPERATIONS.put(getAdjacentAscendants.getName(), getAdjacentAscendants);
        BUILTIN_OPERATIONS.put(getAdjacentDescendants.getName(), getAdjacentDescendants);
        BUILTIN_OPERATIONS.put(getNodeProperties.getName(), getNodeProperties);
        BUILTIN_OPERATIONS.put(getNodeType.getName(), getNodeType);
        BUILTIN_OPERATIONS.put(hasPropertyKey.getName(), hasPropertyKey);
        BUILTIN_OPERATIONS.put(hasPropertyValue.getName(), hasPropertyValue);
        BUILTIN_OPERATIONS.put(nodeExists.getName(), nodeExists);
        BUILTIN_OPERATIONS.put(getNode.getName(), getNode);
        BUILTIN_OPERATIONS.put(search.getName(), search);
        BUILTIN_OPERATIONS.put(name.getName(), name);
        BUILTIN_OPERATIONS.put(id.getName(), id);
        BUILTIN_OPERATIONS.put(getPolicyClassIds.getName(), getPolicyClassIds);
        BUILTIN_OPERATIONS.put(selfAccessComputePrivileges.getName(), selfAccessComputePrivileges);
        BUILTIN_OPERATIONS.put(selfAccessComputeAdjacentAscendantPrivileges.getName(),
            selfAccessComputeAdjacentAscendantPrivileges);
        BUILTIN_OPERATIONS.put(selfAccessComputePrivilegesBatch.getName(), selfAccessComputePrivilegesBatch);
        BUILTIN_OPERATIONS.put(selfAccessComputeDeniedPrivileges.getName(), selfAccessComputeDeniedPrivileges);
        BUILTIN_OPERATIONS.put(selfAccessComputeSubgraphPrivileges.getName(), selfAccessComputeSubgraphPrivileges);
        BUILTIN_OPERATIONS.put(selfAccessComputeAdjacentDescendantPrivileges.getName(),
            selfAccessComputeAdjacentDescendantPrivileges);
        BUILTIN_OPERATIONS.put(selfAccessComputePersonalObjectSystem.getName(), selfAccessComputePersonalObjectSystem);
        BUILTIN_OPERATIONS.put(isNull.getName(), isNull);
    }

    /**
     * Returns a copy of the built-in operations, keyed by their PML operation name.
     *
     * @return the built-in operations
     */
    public static Map<String, Operation<?>> builtinOperations() {
        return new HashMap<>(BUILTIN_OPERATIONS);
    }

    public static boolean isBuiltinOperation(String operationName) {
        return BUILTIN_OPERATIONS.containsKey(operationName);
    }

    private PMLBuiltinOperations() {}
}
