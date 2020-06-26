package gov.nist.csd.pm.pip.graph.model.nodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Common properties used for nodes
 */
public class Properties {
    public static final int HASH_LENGTH = 163;
    public static final String SUPER_KEYWORD = "super";
    public static final String WILDCARD = "*";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String DESCRIPTION_PROPERTY = "description";
    public static final String NAMESPACE_PROPERTY = "namespace";
    public static final String DEFAULT_NAMESPACE = "default";
    public static final String SOURCE_PROPERTY = "source";
    public static final String STORAGE_PROPERTY = "storage";
    public static final String GCS_STORAGE = "google";
    public static final String AWS_STORAGE = "amazon";
    public static final String LOCAL_STORAGE = "local";
    public static final String CONTENT_TYPE_PROPERTY = "content_type";
    public static final String SIZE_PROPERTY = "size";
    public static final String PATH_PROPERTY = "path";
    public static final String BUCKET_PROPERTY = "bucket";
    public static final String COLUMN_INDEX_PROPERTY = "column_index";
    public static final String ORDER_BY_PROPERTY = "order_by";
    public static final String ROW_INDEX_PROPERTY = "row_index";
    public static final String SESSION_USER_ID_PROPERTY = "user_id";
    public static final String SCHEMA_COMP_PROPERTY = "schema_comp";
    public static final String SCHEMA_COMP_SCHEMA_PROPERTY = "schema";
    public static final String SCHEMA_COMP_TABLE_PROPERTY = "table";
    public static final String SCHEMA_COMP_ROW_PROPERTY = "row";
    public static final String SCHEMA_COMP_COLUMN_PROPERTY = "col";
    public static final String SCHEMA_COMP_CELL_PROPERTY = "cell";
    public static final String SCHEMA_NAME_PROPERTY = "schema";
    public static final String COLUMN_CONTAINER_NAME = "Columns";
    public static final String ROW_CONTAINER_NAME = "Rows";
    public static final String COLUMN_PROPERTY = "column";
    public static final String REP_PROPERTY = "rep";

    public static Map<String, String> none() {
        return new HashMap<>();
    }
}
