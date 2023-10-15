package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Map;

/**
 * UserDefinedPMLStore extends the {@link UserDefinedPML} interface and outlines how a concrete implementation of the
 * interface at the Policy Administration Point (PAP) level of the Policy Machine should behave including input
 * validation and expected exceptions.
 */
public interface UserDefinedPMLStore extends UserDefinedPML {

    /**
     * See {@link UserDefinedPML#createFunction(FunctionDefinitionStatement)} <p>
     *
     * @throws PMLFunctionAlreadyDefinedException If a function with the given name is already defined.
     * @throws PMBackendException                 If there is an error executing the command in the PIP.
     */
    @Override
    void createFunction(FunctionDefinitionStatement functionDefinitionStatement)
    throws PMLFunctionAlreadyDefinedException, PMBackendException;

    /**
     * See {@link UserDefinedPML#deleteFunction(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void deleteFunction(String functionName)
    throws PMBackendException;

    /**
     * See {@link UserDefinedPML#getFunctions()} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    Map<String, FunctionDefinitionStatement> getFunctions()
    throws PMBackendException;

    /**
     * See {@link UserDefinedPML#getFunction(String)} <p>
     *
     * @throws PMLFunctionNotDefinedException If a function with the given name does not exist.
     * @throws PMBackendException             If there is an error executing the command in the PIP.
     */
    @Override
    FunctionDefinitionStatement getFunction(String name)
    throws PMLFunctionNotDefinedException, PMBackendException;

    /**
     * See {@link UserDefinedPML#createConstant(String, Value)} <p>
     *
     * @throws PMLConstantAlreadyDefinedException If a constant with the given name is already defined.
     * @throws PMBackendException                 If there is an error executing the command in the PIP.
     */
    @Override
    void createConstant(String constantName, Value constantValue)
    throws PMLConstantAlreadyDefinedException, PMBackendException;

    /**
     * See {@link UserDefinedPML#deleteConstant(String)} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void deleteConstant(String constName)
    throws PMBackendException;

    /**
     * See {@link UserDefinedPML#getConstants()} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    Map<String, Value> getConstants()
    throws PMBackendException;

    /**
     * See {@link UserDefinedPML#getConstant(String)} <p>
     *
     * @throws PMLConstantNotDefinedException If a constant with the given name does not exist.
     * @throws PMBackendException             If there is an error executing the command in the PIP.
     */
    @Override
    Value getConstant(String name)
    throws PMLConstantNotDefinedException, PMBackendException;

    /**
     * Check the function being created.
     *
     * @param name The name of the new function.
     * @throws PMLFunctionAlreadyDefinedException If a function already exists with the given name.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    void checkCreateFunctionInput(String name) throws PMLFunctionAlreadyDefinedException, PMBackendException;

    /**
     * Check the function being deleted. If the function does not exist return false to indicate to the caller that
     * execution should not proceed.
     *
     * @param name The name of the function being deleted.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    default boolean checkDeleteFunctionInput(String name) throws PMBackendException {
        // if function does not exist the check returns false
        try {
            getFunction(name);
            return true;
        } catch (PMLFunctionNotDefinedException e) {
            return false;
        }
    }

    /**
     * Check the function being retrieved.
     *
     * @param name The name of the function to get.
     * @throws PMLFunctionNotDefinedException If a function with the given name is not defined.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    void checkGetFunctionInput(String name) throws PMLFunctionNotDefinedException, PMBackendException;

    /**
     * Check the constant being created.
     *
     * @param name The name of the new constant.
     * @throws PMLConstantAlreadyDefinedException If a constant already has the given name.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    void checkCreateConstantInput(String name) throws PMLConstantAlreadyDefinedException, PMBackendException;

    /**
     * Check the constant being deleted. If the constant does not exist return false to indicate to the caller that
     * execution should not proceed.
     *
     * @param name The name of the constant being deleted.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    default boolean checkDeleteConstantInput(String name) throws PMBackendException {
        // if function does not exist the check returns false
        try {
            getConstant(name);
            return true;
        } catch (PMLConstantNotDefinedException e) {
            return false;
        }
    }

    /**
     * Check the constant being retrieved.
     *
     * @param name The name of the constant.
     * @throws PMLConstantNotDefinedException If the given constant is not defined.
     * @throws PMBackendException If there is an error in the backend implementation.
     */
    void checkGetConstantInput(String name) throws PMLConstantNotDefinedException, PMBackendException;

}
