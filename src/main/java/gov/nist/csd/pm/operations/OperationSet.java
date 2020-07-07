package gov.nist.csd.pm.operations;

import com.google.gson.Gson;

import java.util.*;

import static gov.nist.csd.pm.operations.Operations.*;
import static gov.nist.csd.pm.operations.Operations.ALL_RESOURCE_OPS;

public class OperationSet extends HashSet<String> {

    public OperationSet(String ... ops) {
        this.addAll(Arrays.asList(ops));
    }

    public OperationSet(Collection<String> ops) {
        this.addAll(ops);
    }
}
