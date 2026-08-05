package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.StatementVisitor;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.store.ObligationsStore.ObligationPml;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObligationsQuerier extends Querier implements ObligationsQuery {

    private final OperationsQuery operationsQuery;

    public ObligationsQuerier(PolicyStore store, OperationsQuery operationsQuery) {
        super(store);
        this.operationsQuery = operationsQuery;
    }

    @Override
    public Obligation getObligation(String name) throws PMException {
        if (!obligationExists(name)) {
            throw new ObligationDoesNotExistException(name);
        }

        return compile(store.obligations().getObligationPml(name));
    }

    @Override
    public Collection<Obligation> getObligations() throws PMException {
        List<Obligation> obligations = new ArrayList<>();
        for (ObligationPml row : store.obligations().getObligationPmls()) {
            obligations.add(compile(row));
        }

        return obligations;
    }

    @Override
    public boolean obligationExists(String name) throws PMException {
        return store.obligations().obligationExists(name);
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(NodeUserContext author) throws PMException {
        Collection<Obligation> obligations = getObligations();
        List<Obligation> withAuthor = new ArrayList<>();
        for (Obligation obligation : obligations) {
            if(obligation.getAuthor().equals(author)) {
                withAuthor.add(obligation);
            }
        }

        return withAuthor;
    }

    private Obligation compile(ObligationPml row) throws PMException {
        if (row == null) {
            return null;
        }

        return StatementVisitor.fromString(operationsQuery, row.pmlText(), CreateObligationStatement.class,
            stmt -> stmt.toObligation(row.author()));
    }
}
