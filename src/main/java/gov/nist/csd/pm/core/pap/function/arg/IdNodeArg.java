package gov.nist.csd.pm.core.pap.function.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

public final class IdNodeArg extends NodeArg<Long> {

    public IdNodeArg(Long value) {
        super(value);
    }

    @Override
    public long getId(PAP pap) throws PMException {
        return getValue();
    }

    @Override
    public String getName(PAP pap) throws PMException {
        return pap.query().graph().getNodeById(getValue()).getName();
    }
}
