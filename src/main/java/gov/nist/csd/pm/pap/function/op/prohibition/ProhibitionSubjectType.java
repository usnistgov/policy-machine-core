package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;

class ProhibitionSubjectType extends ArgType<ProhibitionSubject> {

    @Override
    public ProhibitionSubject cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof ProhibitionSubject prohibitionSubject)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to ProhibitionSubject");
        }

        return prohibitionSubject;
    }
}
