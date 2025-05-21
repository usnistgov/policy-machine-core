package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.prohibition.ProhibitionOp.ProhibitionOpArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.CREATE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.CREATE_PROHIBITION;

public class CreateProhibitionOp extends ProhibitionOp<ProhibitionOpArgs> {

    public CreateProhibitionOp() {
        super(
            "create_prohibition",
            List.of(NAME_PARAM, SUBJECT_PARAM, ARSET_PARAM, INTERSECTION_PARAM, CONTAINERS_PARAM),
            CREATE_PROCESS_PROHIBITION,
            CREATE_PROHIBITION
        );
    }

    @Override
    protected ProhibitionOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        ProhibitionSubject subject = (ProhibitionSubject) prepareArg(SUBJECT_PARAM, argsMap);
        AccessRightSet arset = new AccessRightSet(prepareArg(ARSET_PARAM, argsMap));
        Boolean intersection = prepareArg(INTERSECTION_PARAM, argsMap);
        Map<Long, Boolean> containers = prepareArg(CONTAINERS_PARAM, argsMap);
        List<ContainerCondition> containerConditions = new ArrayList<>();
        for (var container : containers.entrySet()) {
            containerConditions.add(new ContainerCondition(container.getKey(), container.getValue()));
        }
        return new ProhibitionOpArgs(name, subject, arset, intersection, containerConditions);
    }

    @Override
    public Void execute(PAP pap, ProhibitionOpArgs args) throws PMException {
        pap.modify().prohibitions().createProhibition(
            args.getName(),
            args.getSubject(),
            args.getArset(),
            args.getIntersection(),
            args.getContainers()
        );
        return null;
    }
}
