package gov.nist.csd.pm.pap.naming;

public class Naming {

    public static String baseObjectAttribute(String policyClass) {
        return policyClass + "_base_OA";
    }

    public static String baseUserAttribute(String policyClass) {
        return policyClass + "_base_UA";
    }

    public static String pcRepObjectAttribute(String policyClass) {
        return policyClass + "_pc_rep";
    }

}
