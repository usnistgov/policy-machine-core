package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.PAP;

public interface PolicyStore extends Transactional {

    GraphStore graph();
    ProhibitionsStore prohibitions();
    ObligationsStore obligations();
    OperationsStore operations();

    void reset() throws PMException;

    /**
     * Give the store a reference to the owning {@link PAP}, called once at construction. Only stores that
     * need to recompile persisted PML text on read (e.g. the Neo4j-backed store, to resolve lazy
     * cross-references via {@code StatementVisitor.fromString}) need to keep it; the default is a no-op.
     * @param pap The PAP this store backs.
     */
    default void setPap(PAP pap) {
    }

}
