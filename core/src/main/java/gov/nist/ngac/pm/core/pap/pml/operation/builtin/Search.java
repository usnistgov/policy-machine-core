/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A PML built-in query that returns the nodes matching a type and property filter.
 */
public class Search extends PMLQueryOperation<List<Map<String, Object>>> {

    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));


    public Search() {
        super(
                "search",
                ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)),
                List.of(TYPE_PARAM, PROPERTIES_PARAM),
            List.of()
        );
    }

    @Override
    public List<Map<String, Object>> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        NodeType nodeType = NodeType.toNodeType(args.get(TYPE_PARAM));

        Map<String, String> properties = args.get(PROPERTIES_PARAM);
        Collection<Node> search = query.graph().search(nodeType, properties);

        List<Map<String, Object>> ret = new ArrayList<>(search.size());
        for (Node node : search) {
            ret.add(Map.of(
                "name", node.getName(),
                "type", node.getType().toString(),
                "properties", node.getProperties()
            ));
        }

        return ret;
    }
}
