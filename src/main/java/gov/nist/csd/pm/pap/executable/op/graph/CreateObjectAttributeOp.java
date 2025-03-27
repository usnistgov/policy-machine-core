package gov.nist.csd.pm.pap.executable.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;

public class CreateObjectAttributeOp extends CreateNodeOp{

    public CreateObjectAttributeOp() {
        super("create_object_attribute", CREATE_OBJECT_ATTRIBUTE);
    }

    @Override
    public Long execute(PAP pap, ActualArgs actualArgs) throws PMException {
        return pap.modify().graph().createObjectAttribute(
                actualArgs.get(NAME_ARG),
                actualArgs.get(DESCENDANTS_ARG)
        );
    }
}
