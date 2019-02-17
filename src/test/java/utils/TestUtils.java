package utils;

import gov.nist.csd.pm.db.DatabaseContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtils {

    private static DatabaseContext dbCtx;

    public static DatabaseContext getDatabaseContext() throws IOException {
        if(dbCtx == null) {
            InputStream is = TestUtils.class.getClassLoader().getResourceAsStream("test_db.config");
            Properties props = new Properties();
            props.load(is);

            dbCtx = new DatabaseContext(
                    props.getProperty("host"),
                    Integer.valueOf(props.getProperty("port")),
                    props.getProperty("username"),
                    props.getProperty("password"),
                    props.getProperty("schema")
            );
        }

        return dbCtx;
    }
}
