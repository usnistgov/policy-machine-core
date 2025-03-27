package gov.nist.csd.pm.pdp.event;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.executable.op.arg.ListIdNodeFormalArg;
import gov.nist.csd.pm.pap.executable.op.arg.NodeFormalArg;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class EventContextUtil {

    public static EventContext buildEventContext(PAP pap, UserContext userCtx, String opName, ActualArgs args) throws
                                                                                                                PMException {
        String userName = pap.query().graph().getNodeById(userCtx.getUser()).getName();

        return new EventContext(userName, userCtx.getProcess(), opName, resolveNodeArgNames(pap, args));
    }

    private static Map<String, Object> resolveNodeArgNames(PAP pap, ActualArgs actualArgs) {
        Map<String, Object> args = new HashMap<>();

        actualArgs.foreach(f -> {
            if (!(f instanceof NodeFormalArg)) {
                return;
            }

            // arg is id or list id
            // convert the ids to names
            switch (f) {
                case IdNodeFormalArg idNodeFormalArg ->
                    args.put(idNodeFormalArg.getName(), resolveNodeArgName(pap, actualArgs.get(idNodeFormalArg)));
                case ListIdNodeFormalArg listIdNodeFormalArg -> {
                    LongArrayList ids = actualArgs.get(listIdNodeFormalArg);
                    List<String> names = LongStream.of(ids.toLongArray())
                        .mapToObj(id -> resolveNodeArgName(pap, id))
                        .collect(Collectors.toList());
                    args.put(listIdNodeFormalArg.getName(), names);
                }
                default -> {
                }
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
