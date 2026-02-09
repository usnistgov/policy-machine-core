package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import java.util.List;

public abstract non-sealed class AdminOperation<R> extends Operation<R> {

    public AdminOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          List<RequiredCapability> requiredCapabilities) {
        super(name, returnType, parameters, requiredCapabilities);
    }

    public AdminOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          RequiredCapability requiredCapability,
                          RequiredCapability... requiredCapabilities) {
        super(name, returnType, parameters, requiredCapability, requiredCapabilities);
    }

    public AdminOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters,
                          AdminPolicyNode target,
                          AdminAccessRight ar) {
        super(name, returnType, parameters, new RequiredCapability(new RequiredPrivilegeOnNode(target.nodeName(), ar)));
    }
}
