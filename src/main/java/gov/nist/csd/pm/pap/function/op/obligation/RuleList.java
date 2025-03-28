package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.obligation.Rule;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collection;

public class RuleList extends ObjectArrayList<Rule> {

	public RuleList() {
	}

	public RuleList(Collection<? extends Rule> c) {
		super(c);
	}
}
