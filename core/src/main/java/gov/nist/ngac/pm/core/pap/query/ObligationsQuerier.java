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

/**
 * An {@link ObligationsQuery} implementation backed by the policy store's obligations store.
 */
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
