package gov.nist.csd.pm.policy.exceptions;

public class TransactionNotStartedException extends PMException{
    public TransactionNotStartedException() {
        super("a transaction has not been started using beginTx()");
    }
}
