package gov.nist.csd.pm.pap.function.arg.type;

import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;

public final class ProhibitionSubjectType extends ArgType<ProhibitionSubject> {

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

    @Override
    public Class<ProhibitionSubject> getExpectedClass() {
        return ProhibitionSubject.class;
    }
}
