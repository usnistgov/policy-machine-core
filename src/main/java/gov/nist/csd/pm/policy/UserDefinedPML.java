package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Map;

public interface UserDefinedPML {

    /**
     * Add a new user defined PML function. The function will be available to any subsequent PML statements.
     * @param functionDefinitionStatement The function definition to add.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException;

    /**
     * Remove a user defined PML function.
     * @param functionName The name of the function to be removed.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void deleteFunction(String functionName) throws PMException;

    /**
     * Get all user defined PML functions.
     * @return A map of function names to function definitions for all functions.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    Map<String, FunctionDefinitionStatement> getFunctions() throws PMException;

    /**
     * Get the function definition with the given name.
     * @param name The name of the function to get.
     * @return The function definition of the function with the given name.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    FunctionDefinitionStatement getFunction(String name) throws PMException;

    /**
     * Add a new user defined PML constant. The constant will be available to any subsequent PML statements.
     * @param constantName The name of the constant.
     * @param constantValue The value of the constant.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void createConstant(String constantName, Value constantValue) throws PMException;

    /**
     * Remove a PML constant.
     * @param constName The name of the constant to remove.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void deleteConstant(String constName) throws PMException;

    /**
     * Get all user defined constants.
     * @return A map of constant names to constant values for all constants.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    Map<String, Value> getConstants() throws PMException;

    /**
     * Get the constant value with the given name.
     * @param name The name of the constant to get.
     * @return The value of the constant with the given name.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    Value getConstant(String name) throws PMException;
}
