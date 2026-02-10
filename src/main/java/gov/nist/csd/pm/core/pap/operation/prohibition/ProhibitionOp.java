package gov.nist.csd.pm.core.pap.operation.prohibition;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import java.util.List;

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
