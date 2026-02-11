package gov.nist.csd.pm.core.pdp;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import java.util.List;
import java.util.Map;

public class OperationDenied extends AdminOperation<Void> {

    public static FormalParameter<String> OP_NAME_PARAM = new FormalParameter<>("op_name", STRING_TYPE);
    public static FormalParameter<String> TIMESTAMP_PARAM = new FormalParameter<>("timestamp", STRING_TYPE);
    public static FormalParameter<Map<String, Object>> ARGS_PARAM = new FormalParameter<>("args", MapType.of(STRING_TYPE, ANY_TYPE));

    public OperationDenied() {
        super(
            "op_denied",
            VOID_TYPE,
            List.of(
                OP_NAME_PARAM,
                TIMESTAMP_PARAM,
                ARGS_PARAM
            ),
            List.of()
        );
    }

    @Override
    public final Void execute(PAP pap, Args args) throws PMException {
        return null;
    }


}
