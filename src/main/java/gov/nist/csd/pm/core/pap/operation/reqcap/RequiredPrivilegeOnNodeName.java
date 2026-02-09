package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public class RequiredPrivilegeOnNodeName implements RequiredPrivilege{

    private final String name;
    private final AccessRightSet required;

    public RequiredPrivilegeOnNodeName(String name, AccessRightSet required) {
        this.name = name;
        this.required = required;
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        return pap.query()
            .access()
            .computePrivileges(userCtx, new TargetContext(pap.query().graph().getNodeId(name)))
            .containsAll(required);
    }

    public String getName() {
        return name;
    }

    public AccessRightSet getRequired() {
        return required;
    }
}
