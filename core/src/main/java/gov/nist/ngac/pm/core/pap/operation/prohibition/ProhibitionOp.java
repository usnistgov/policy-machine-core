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

package gov.nist.ngac.pm.core.pap.operation.prohibition;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import java.util.List;

/**
 * Base class for the admin operations that create and delete prohibitions.
 */
public abstract class ProhibitionOp extends AdminOperation<Void> {

    public static NodeIdFormalParameter NODE_ID_PARAM = new NodeIdFormalParameter("node_id");
    public static final NodeIdListFormalParameter INCLUSION_SET_PARAM = new NodeIdListFormalParameter("inclusion_set");
    public static final NodeIdListFormalParameter EXCLUSION_SET_PARAM = new NodeIdListFormalParameter("exclusion_set");
    public static final FormalParameter<Boolean> IS_CONJUNCTIVE_PARAM = new FormalParameter<>("is_conjunctive", BOOLEAN_TYPE);

    public ProhibitionOp(String name,
                         List<FormalParameter<?>> parameters,
                         RequiredCapability... requiredCapabilities) {
        super(name, VOID_TYPE, parameters, List.of(requiredCapabilities));
    }
}
