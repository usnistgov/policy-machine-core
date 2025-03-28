package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;

public class ContainerConditionsList extends ObjectArrayList<ContainerCondition> {

    public ContainerConditionsList() {
    }

    public ContainerConditionsList(Collection<? extends ContainerCondition> c) {
        super(c);
    }
}
