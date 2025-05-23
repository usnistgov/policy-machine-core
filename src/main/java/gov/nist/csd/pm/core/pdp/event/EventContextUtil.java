package gov.nist.csd.pm.core.pdp.event;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.ListIdNodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventContextUtil {

    public static EventContext buildEventContext(PAP pap, UserContext userCtx, String opName, Args args) throws
                                                                                                                PMException {
        String userName = pap.query().graph().getNodeById(userCtx.getUser()).getName();

        return new EventContext(userName, userCtx.getProcess(), opName, resolveNodeArgNames(pap, args));
    }

    private static Map<String, Object> resolveNodeArgNames(PAP pap, Args actualArgs) {
        Map<String, Object> args = new HashMap<>();

        actualArgs.foreach((formalArg, value) -> {
            // if the arg is a node arg than we need to convert the node IDs to names for the EPP
            switch (formalArg) {
                case IdNodeFormalParameter idNodeFormalArg ->
                    args.put(idNodeFormalArg.getName(), resolveNodeArgName(pap, actualArgs.get(idNodeFormalArg)));
                case ListIdNodeFormalParameter listIdNodeFormalArg -> {
                    List<Long> ids = actualArgs.get(listIdNodeFormalArg);
                    List<String> names = ids.stream()
                        .map(id -> resolveNodeArgName(pap, id))
                        .collect(Collectors.toList());
                    args.put(listIdNodeFormalArg.getName(), names);
                }
                default -> args.put(formalArg.getName(), value);
            }
        });

        return args;
    }

    private static String resolveNodeArgName(PAP pap, long id) {
        try {
            return pap.query().graph().getNodeById(id).getName();
        } catch (PMException e) {
            throw new RuntimeException(e);
        }
    }

}
