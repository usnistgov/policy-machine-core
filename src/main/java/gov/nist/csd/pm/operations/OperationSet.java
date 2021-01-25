package gov.nist.csd.pm.operations;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class OperationSet extends HashSet<String> {
    public OperationSet(){
    }

    public OperationSet(String ... ops) {
        this.addAll(Arrays.asList(ops));
    }

    public OperationSet(Collection<String> ops) {
        this.addAll(ops);
    }

}
