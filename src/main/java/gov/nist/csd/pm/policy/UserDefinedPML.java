package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Map;

public interface UserDefinedPML {

    /**
     * Add a new user defined PML function. The function will be available to any subsequent PML statements.
     * @param functionDefinitionStatement The function definition to add.
     * @throws PMException
     */
    void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException;

    /**
     * Remove a user defined PML function.
     * @param functionName The name of the function to be removed.
     * @throws PMException
     */
    void deleteFunction(String functionName) throws PMException;

    /**
     * Get all user defined PML functions.
     * @return A map of function names to function definitions for all functions.
     * @throws PMException
     */
    Map<String, FunctionDefinitionStatement> getFunctions() throws PMException;

    /**
     * Get the function definition with the given name.
     * @param name The name of the function to get.
     * @return The function definition of the function with the given name.
     * @throws PMException
     */
    FunctionDefinitionStatement getFunction(String name) throws PMException;

    /**
     * Add a new user defined PML constant. The constant will be available to any subsequent PML statements.
     * @param constantName The name of the constant.
     * @param constantValue The value of the constant.
     * @throws PMException
     */
    void createConstant(String constantName, Value constantValue) throws PMException;

    /**
     * Remove a PML constant.
     * @param constName The name of the constant to remove.
     * @throws PMException
     */
    void deleteConstant(String constName) throws PMException;

    /**
     * Get all user defined contants.
     * @return A map of constant names to constant values for all constants.
     * @throws PMException
     */
    Map<String, Value> getConstants() throws PMException;

    /**
     * Get the constant value with the given name.
     * @param name The name of the constant to get.
     * @return The value of the constant with the given name.
     * @throws PMException
     */
    Value getConstant(String name) throws PMException;
}
