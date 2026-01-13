package gov.nist.csd.pm.core.pap.function.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

public sealed abstract class NodeArg<T> permits NameNodeArg, IdNodeArg{

    private T value;

    public NodeArg(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Get the ID of this node arg given the PAP object. If this is a NameNodeArg it will call
     * pap.query().graph().getNodeId(value). If this is a IdNodeArg it will just return the value field.
     * @param pap The PAP object.
     * @return The ID of the node.
     * @throws PMException If there is an exception accessing the policy via PAP object.
     */
    public abstract long getId(PAP pap) throws PMException;

    /**
     * Get the Name of this node arg given the PAP object. If this is a NameNodeArg it will just return the value field.
     * If this is a IdNodeArg it will call pap.query().graph().getNodeById(value).getName().
     * @param pap The PAP object.
     * @return The name of the node.
     * @throws PMException If there is an exception accessing the policy via PAP object.
     */
    public abstract String getName(PAP pap) throws PMException;
}
