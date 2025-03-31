package gov.nist.csd.pm.pap.function.op.graph;

import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.mapType;
import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.stringType;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.arg.type.AccessRightSetType;
import gov.nist.csd.pm.pap.function.op.arg.ListIdNodeFormalArg;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;

import java.util.List;
import java.util.Map;

public abstract class GraphOp<T> extends Operation<T> {

    public static final FormalArg<NodeType> TYPE_ARG = new FormalArg<>("type", new NodeTypeType());
    public static final FormalArg<Map<String, String>> PROPERTIES_ARG = new FormalArg<>("properties", mapType(stringType(), stringType()));
    public static final FormalArg<AccessRightSet> ARSET_ARG = new FormalArg<>("arset", new AccessRightSetType());

    public static final ListIdNodeFormalArg DESCENDANTS_ARG = new ListIdNodeFormalArg("descendants");

    public static final IdNodeFormalArg ASCENDANT_ARG = new IdNodeFormalArg("ascendant");
    public static final IdNodeFormalArg UA_ARG = new IdNodeFormalArg("ua");
    public static final IdNodeFormalArg TARGET_ARG = new IdNodeFormalArg("target");

    public GraphOp(String name, List<FormalArg<?>> formalArgs) {
        super(name, formalArgs);
    }
}
