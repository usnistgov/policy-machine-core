package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;

public final class ProhibitionSubjectArgType extends Type<ProhibitionSubject> {

    @Override
    public ProhibitionSubject cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof ProhibitionSubject p)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to ProhibitionSubject");
        }

        return p;
    }
}
