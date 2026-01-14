package gov.nist.csd.pm.core.pdp.event;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public class EventContextUtil {

    /**
     * Util method to build an EventContext object given information about an operation. The provided userCtx will either
     * have a user node ID or a list of attribute IDs. These IDs will be converted into their corresponding names.
     * @param pap The PAP object.
     * @param userCtx The user that executed the operation.
     * @param opName The operation name.
     * @param args The args passed to the operation.
     * @return The EventContext object.
     * @throws PMException If there is an exception converting the operation to an EventContext.
     */
    public static EventContext buildEventContext(PAP pap, UserContext userCtx, String opName, Args args) throws PMException {
        return new EventContext(EventContextUser.fromUserContext(userCtx, pap), opName, args.toMap());
    }
}
