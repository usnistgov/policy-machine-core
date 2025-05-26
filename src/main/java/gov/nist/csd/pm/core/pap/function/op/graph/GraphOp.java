package gov.nist.csd.pm.core.pap.function.op.graph;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.op.Operation;

import gov.nist.csd.pm.core.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.ListIdNodeFormalParameter;
import java.util.List;
import java.util.Map;

public abstract class GraphOp<R, A extends Args> extends Operation<R, A> {

    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));

    public static final ListIdNodeFormalParameter DESCENDANTS_PARAM = new ListIdNodeFormalParameter("descendants");

    public static final IdNodeFormalParameter ASCENDANT_PARAM = new IdNodeFormalParameter("ascendant");
    public static final IdNodeFormalParameter UA_PARAM = new IdNodeFormalParameter("ua");
    public static final IdNodeFormalParameter TARGET_PARAM = new IdNodeFormalParameter("target");

    public GraphOp(String name, List<FormalParameter<?>> formalParameters) {
        super(name, formalParameters);
    }
}
