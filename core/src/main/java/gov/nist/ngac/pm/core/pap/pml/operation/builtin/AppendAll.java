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
import static gov.nist.ngac.pm.core.pap.pml.operation.builtin.Append.DST_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

/**
 * PML built-in function append_all(dst, src). Appends every element of the src list to dst in place and
 * returns dst.
 */
public class AppendAll extends PMLFunctionOperation<List<Object>> {

    public static final FormalParameter<List<Object>> SRC_LIST_PARAM = new FormalParameter<>("src", ListType.of(ANY_TYPE));

    public AppendAll() {
        super(
                "append_all",
                ListType.of(ANY_TYPE),
                List.of(DST_PARAM, SRC_LIST_PARAM)
        );
    }

    @Override
    public List<Object> execute(Args args) throws PMException {
        List<Object> valueArr = args.get(DST_PARAM);
        List<Object> srcValue = args.get(SRC_LIST_PARAM);

        valueArr.addAll(srcValue);

        return valueArr;
    }
}
