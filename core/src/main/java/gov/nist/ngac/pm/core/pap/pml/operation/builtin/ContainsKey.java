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


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;
import java.util.Map;

/**
 * A PML built-in function that returns whether a map contains a key.
 */
public class ContainsKey extends PMLFunctionOperation<Boolean> {

    public static final FormalParameter<Map<Object, Object>> MAP_PARAM = new FormalParameter<>("map", MapType.of(
        ANY_TYPE, ANY_TYPE));
    public static final FormalParameter<Object> KEY_PARAM = new FormalParameter<>("key", ANY_TYPE);


    public ContainsKey() {
        super(
                "contains_key",
                BOOLEAN_TYPE,
                List.of(MAP_PARAM, KEY_PARAM)
        );
    }

    @Override
    public Boolean execute(Args args) throws PMException {
        Map<Object, Object> valueMap = args.get(MAP_PARAM);
        Object element = args.get(KEY_PARAM);
        return valueMap.containsKey(element);
    }
}
