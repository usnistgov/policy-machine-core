package gov.nist.csd.pm.core.pap.function.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

public final class NameNodeArg extends NodeArg<String> {

    public NameNodeArg(String value) {
        super(value);
    }

    @Override
    public long getId(PAP pap) throws PMException {
        return pap.query().graph().getNodeId(getValue());

    }

    @Override
    public String getName(PAP pap) throws PMException {
        return getValue();
    }
}
