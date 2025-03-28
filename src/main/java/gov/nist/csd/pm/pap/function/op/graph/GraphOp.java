package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.arg.ListIdNodeFormalArg;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;

import java.util.List;

public abstract class GraphOp<T> extends Operation<T> {

    public static final FormalArg<NodeType> TYPE_ARG = new FormalArg<>("type", NodeType.class);
    public static final FormalArg<Properties> PROPERTIES_ARG = new FormalArg<>("properties", Properties.class);
    public static final FormalArg<AccessRightSet> ARSET_ARG = new FormalArg<>("arset", AccessRightSet.class);

    public static final ListIdNodeFormalArg DESCENDANTS_ARG = new ListIdNodeFormalArg("descendants");

    public static final IdNodeFormalArg ASCENDANT_ARG = new IdNodeFormalArg("ascendant");
    public static final IdNodeFormalArg UA_ARG = new IdNodeFormalArg("ua");
    public static final IdNodeFormalArg TARGET_ARG = new IdNodeFormalArg("target");

    public GraphOp(String name, List<FormalArg<?>> formalArgs) {
        super(name, formalArgs);
    }
}
