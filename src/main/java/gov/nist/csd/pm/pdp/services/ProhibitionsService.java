package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pdp.services.guard.ProhibitionsGuard;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.operations.Operations.*;

public class ProhibitionsService extends Service implements Prohibitions {

    private ProhibitionsGuard guard;

    public ProhibitionsService(PAP pap, EPP epp, OperationSet resourceOps) {
        super(pap, epp, resourceOps);

        this.guard = new ProhibitionsGuard(pap, resourceOps);
    }

    @Override
    public void add(Prohibition prohibition) throws PMException {
        guard.checkAdd(userCtx, prohibition);

        //create prohibition in PAP
        getProhibitionsAdmin().add(prohibition);
    }

    @Override
    public List<Prohibition> getAll() throws PMException {
        List<Prohibition> all = getProhibitionsAdmin().getAll();
        guard.filter(userCtx, all);
        return all;
    }

    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        Prohibition prohibition = getProhibitionsAdmin().get(prohibitionName);
        guard.checkGet(userCtx, prohibition);

        return prohibition;
    }

    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PMException {
        List<Prohibition> prohibitionsFor = getProhibitionsAdmin().getProhibitionsFor(subject);
        guard.filter(userCtx, prohibitionsFor);
        return prohibitionsFor;
    }

    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PMException {
        guard.checkUpdate(userCtx, prohibition);
        getProhibitionsAdmin().update(prohibitionName, prohibition);
    }

    @Override
    public void delete(String prohibitionName) throws PMException {
        guard.checkDelete(userCtx, getProhibitionsAdmin().get(prohibitionName));
        getProhibitionsAdmin().delete(prohibitionName);
    }

    public void reset(UserContext userCtx) throws PMException {
        guard.checkReset(userCtx);

        List<Prohibition> prohibitions = getProhibitionsAdmin().getAll();
        Set<String> names = new HashSet<>();
        for (Prohibition pro : prohibitions) {
            names.add(pro.getName());
        }
        for (String name : names) {
            delete(name);
        }
    }
}