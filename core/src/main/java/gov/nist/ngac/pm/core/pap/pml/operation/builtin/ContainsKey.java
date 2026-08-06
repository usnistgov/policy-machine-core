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
