package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * A PML built-in query that returns a node's name given its id.
 */
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
    public String execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        long id = args.get(NODE_ID_PARAM);
        return query.graph().getNodeById(id).getName();
    }
}
