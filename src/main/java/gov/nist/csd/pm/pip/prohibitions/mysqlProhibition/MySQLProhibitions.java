package gov.nist.csd.pm.pip.prohibitions.mysqlProhibition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;
import gov.nist.csd.pm.pip.graph.mysql.MySQLHelper;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MySQLProhibitions implements Prohibitions {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader reader2 = new ObjectMapper().readerFor(OperationSet.class);
    private final MySQLConnection conn;

    public MySQLProhibitions(MySQLConnection connection) {
        this.conn = connection;
    }

    public static String hashSetToJSON(Set<String> set) throws JsonProcessingException {
        return objectMapper.writeValueAsString(set);
    }

    /**
     * Create a new prohibition.
     *
     * @param prohibition The prohibition to be created.
     * @throws PIPException if there is an error creating a prohibition.
     */
    @Override
    public void add(Prohibition prohibition) throws PIPException {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was received when creating a prohibition");
        }
        else if (prohibition.getName() == null || prohibition.getName().isEmpty()) {
            throw new IllegalArgumentException("a null or empty name was provided when creating a prohibition");
        }
        else if (prohibition.getSubject() == null) {
            throw new IllegalArgumentException("a null subject was provided when creating a prohibition");
        }
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection con = this.conn.getConnection();

            //====================  INSERT NEW PROHIBITION ====================
            ps = con.prepareStatement(MySQLHelper.INSERT_PROHIBITION, Statement.RETURN_GENERATED_KEYS);
            pstmt = con.prepareStatement(MySQLHelper.INSERT_CONTAINERS);

            ps.setString(1, prohibition.getName()); //name
            //no type id yet
            ps.setString(2, null); //type_id

            //as we do not know the type yet we store the subject in both field
            ps.setString(3, prohibition.getSubject()); //user_attribute_id
            ps.setString(4, prohibition.getSubject()); //process_id

            ps.setInt(5, prohibition.isIntersection() ? 1 : 0 ); //is_intersection
            //deny_operations
            if (prohibition.getOperations() == null) {
                ps.setString(6, null);
            } else {
                try {
                ps.setString(6, hashSetToJSON(prohibition.getOperations()));
                } catch (JsonProcessingException j) {
                    j.printStackTrace();
                }
            }

            ps.executeUpdate();
            ResultSet rs_id = ps.getGeneratedKeys();
            int deny_id = 0;
            if(rs_id.next()) {
                deny_id = rs_id.getInt(1);
            }
            //====================  INSERT NEW CONTAINERS ====================
            for (Map.Entry<String,Boolean> container : prohibition.getContainers().entrySet()) {
                pstmt.setInt(1, deny_id);
                pstmt.setInt(2, Integer.parseInt(container.getKey()));
                pstmt.setInt(3, container.getValue() ? 1 : 0);
                pstmt.executeUpdate();
            }
            con.close();
        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            try {
                if(rs != null) {rs.close();}
                if(ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get a list of all prohibitions
     *
     * @return a list of all prohibitions
     * @throws PMException if there is an error getting the prohibitions.
     */
    @Override
    public List<Prohibition> getAll() throws PIPException {
        return null;
    }

    /**
     * Retrieve a Prohibition and return the Object representing it.
     *
     * @param prohibitionName The name of the Prohibition to retrieve.
     * @return the Prohibition with the given name.
     * @throws PIPException if there is an error getting the prohibition with the given name.
     */
    @Override
    public Prohibition get(String prohibitionName) throws PIPException {
        List<Prohibition> ps = getAll();
            for(Prohibition p : ps) {
                if(p.getName().equalsIgnoreCase(prohibitionName)) {
                    return p;
                }
            }
        throw new PIPException(String.format("a prohibition does not exist with the name %s", prohibitionName));
    }

    /**
     * Get all of the prohibitions a given entity is the direct subject of.  The subject can be a user, user attribute,
     * or process.
     *
     * @param subject the name of the subject to get the prohibitions for.
     * @return The list of prohibitions the given entity is the subject of.
     */
    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PIPException {
        return null;
    }

    /**
     * Update the prohibition with the given name. Prohibition names cannot be updated.
     *
     * @param prohibitionName the name of the prohibition to update.
     * @param prohibition     The prohibition to update.
     * @throws PIPException if there is an error updating the prohibition.
     */
    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PIPException {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was provided when updating a prohibition");
        } else if (prohibitionName == null) {
            throw new IllegalArgumentException("cannot update a prohibition with a null name");
        }
    }

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param prohibitionName The name of the prohibition to delete.
     * @throws PIPException if there is an error deleting the prohibition.
     */
    @Override
    public void delete(String prohibitionName) throws PIPException {
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_PROHIBITION);
                PreparedStatement ps = con.prepareStatement(MySQLHelper.DELETE_PROHIBITION_CONTAINER)
        ){
            //delete prohibition
            pstmt.setString(1, prohibitionName);
            pstmt.executeUpdate();

            Prohibition p = get(prohibitionName);
            //delete containers linked to the prohibition


        } catch (SQLException e) {
            throw new PIPException(e.getMessage());
        }
    }
}
