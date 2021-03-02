package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.services.*;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.MemTx;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.services.*;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.common.tx.TxRunner;

public class PDP {

    /**
     * Factory method to create a new PDP and EPP, while abstracting the initialization process. A PDP needs an EPP, and
     * vice versa. These steps need to occur each time a new PDP/EPP is created. The order of each step matters inorder to
     * avoid any race conditions. The EPP is accessible via the PDP's getEPP method.
     *
     * @return the new PDP instance
     */
    public static PDP newPDP(FunctionalEntity pap, EPPOptions eppOptions, Decider decider, Auditor auditor) throws PMException {
        // create PDP
        PDP pdp = new PDP(pap, decider, auditor);
        // create the EPP
        EPP epp = new EPP(pap, pdp, eppOptions);
        // set the PDPs EPP
        pdp.setEPP(epp);
        // initialize PDP services which need the epp that was just set
        //pdp.initServices();
        return pdp;
    }

    private final FunctionalEntity pap;
    private EPP epp;
    private Decider decider;
    private Auditor auditor;
    private OperationSet resourceOps;


    /**
     * Create a new PDP instance given a Policy Administration Point and an optional set of FunctionExecutors to be
     * used by the EPP.
     * @param pap the Policy Administration Point that the PDP will use to change the graph.
     * @throws PMException if there is an error initializing the EPP.
     */
    private PDP(FunctionalEntity pap, Decider decider, Auditor auditor) throws PMException {
        this.pap = pap;
        this.decider = decider;
        this.auditor = auditor;
    }

    private void setEPP(EPP epp) {
        this.epp = epp;
    }

    public EPP getEPP() {
        return epp;
    }

    public OperationSet getResourceOps() throws PMException{
        return resourceOps;
    }

    public void setResourceOps(OperationSet resourceOps)  throws PMException {
        this.resourceOps = resourceOps;
    }

    public void addResourceOps (String... ops) throws PMException {
        OperationSet resourceOperations = getResourceOps();
        String[] newOps = ops;
        for (String op: newOps) {
            if (!resourceOperations.contains(op)) {
                resourceOperations.add(op);
            }
        }
        setResourceOps(resourceOperations);
    }

    public void deleteResourceOps (String... ops) throws PMException {
        OperationSet resourceOperations = getResourceOps();
        String[] newOps = ops;
        for (String op: newOps) {
            if (resourceOperations.contains(op)) {
                resourceOperations.remove(op);
            }
        }
        setResourceOps(resourceOperations);
    }

    public WithUser withUser(UserContext userCtx) {
        return new WithUser(userCtx, pap, epp, decider, auditor);
    }

    public static class WithUser implements FunctionalEntity {

        private UserContext userCtx;
        private FunctionalEntity pap;
        private EPP epp;
        private Decider decider;
        private Auditor auditor;
        private AnalyticsService    analyticsService;


        public WithUser(UserContext userCtx, FunctionalEntity pap, EPP epp, Decider decider, Auditor auditor) {
            this.userCtx = userCtx;
            this.pap = pap;
            this.epp = epp;
            this.decider = decider;
            this.auditor = auditor;
        }

        public WithUser(UserContext userCtx, FunctionalEntity pap, EPP epp, Decider decider, Auditor auditor, AnalyticsService analyticsService) {
            this.userCtx = userCtx;
            this.pap = pap;
            this.epp = epp;
            this.decider = decider;
            this.auditor = auditor;
            this.analyticsService = analyticsService;
        }

        public AnalyticsService getAnalyticsService(UserContext userCtx) {
            analyticsService.setUserCtx(userCtx);
            return analyticsService;
        }

        @Override
        public Graph getGraph() throws PMException {
            return new GraphService(userCtx, pap, epp, decider, auditor);
        }

        @Override
        public Prohibitions getProhibitions() {
            return new ProhibitionsService(userCtx, pap, epp, decider, auditor);
        }

        @Override
        public Obligations getObligations() {
            return new ObligationsService(userCtx, pap, epp, decider, auditor);
        }

        @Override
        public void runTx(TxRunner txRunner) throws PMException {
            GraphService graphService = new GraphService(userCtx, pap, epp, decider, auditor);
            ProhibitionsService prohibitionsService = new ProhibitionsService(userCtx, pap, epp, decider, auditor);
            ObligationsService obligationsService = new ObligationsService(userCtx, pap, epp, decider, auditor);
            MemTx tx = new MemTx(graphService, prohibitionsService, obligationsService);
            tx.runTx(txRunner);
        }
    }

    /**
     * pdp.TxBuilder.withUser("bob").runTx(() -> {})
     * pap.TxBuilder.runTx(() -> {}, "bob")
     * pdp.tx((g,p,o)->{
     *
     * }).run()
     * pdp.tx((g,p,o)->{
     *
     * })
     * .withUser()
     * .run();
     *
     * pdp.withUser().runTx()
     */

}
