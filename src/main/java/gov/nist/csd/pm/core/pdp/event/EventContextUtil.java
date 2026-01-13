package gov.nist.csd.pm.core.pdp.event;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventContextUtil {

    /**
     * Util method to build an EventContext object given information about an operation.
     * @param pap The PAP object.
     * @param userCtx The user that executed the operation.
     * @param opName The operation name.
     * @param args The args passed to the operation.
     * @return The EventContext object.
     * @throws PMException If there is an exception converting the operation to an EventContext.
     */
    public static EventContext buildEventContext(PAP pap, UserContext userCtx, String opName, Args args) throws PMException {
        return new EventContext(EventContextUser.fromUserContext(userCtx, pap), opName, prepareArgs(pap, args));
    }

    /**
     * Takes the passed args in actualArgs and converts any node ids to node names. Any non node
     * args are just added as is.
     */
    private static Map<String, Object> prepareArgs(PAP pap, Args actualArgs) throws PMException {
        Map<String, Object> args = new HashMap<>();

        Map<FormalParameter<?>, Object> map = actualArgs.getMap();
        for (Map.Entry<FormalParameter<?>, Object> entry : map.entrySet()) {
            FormalParameter<?> param = entry.getKey();
            Object value = entry.getValue();

            switch (param) {
                case NodeFormalParameter idNodeFormalArg ->
                    args.put(idNodeFormalArg.getName(), actualArgs.get(idNodeFormalArg).getName(pap));

                case NodeListFormalParameter listIdNodeFormalArg -> {
                    List<String> names = actualArgs.getNameList(listIdNodeFormalArg, pap);
                    args.put(listIdNodeFormalArg.getName(), names);
                }

                // if not a node arg, add as is
                default -> args.put(param.getName(), value);
            }
        }

        return args;
    }
}
