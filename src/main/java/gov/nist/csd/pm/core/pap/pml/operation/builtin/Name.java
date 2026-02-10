package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public class Name extends PMLQueryOperation<String> {

    public static final NodeIdFormalParameter NODE_ID_PARAM =
        new NodeIdFormalParameter("id");

    public Name() {
        super(
            "name",
            STRING_TYPE,
            List.of(NODE_ID_PARAM),
            List.of()
        );
    }

    @Override
    public String execute(PolicyQuery query, Args args) throws PMException {
        long id = args.get(NODE_ID_PARAM);
        return query.graph().getNodeById(id).getName();
    }
}
