package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class Neo4jUtilTest {

    @TempDir
    Path tmp;

    @Test
    void testDeserializeWithDifferentClassLoaders() throws Exception {
        // create test serializable class
        String src = """
            package example;
            import java.io.Serializable;
            public class Foo implements Serializable {
              private static final long serialVersionUID = 1L;
              private final String name;
              public Foo(String name) { this.name = name; }
              public String getName() { return name; }
              @Override public String toString(){ return "Foo{" + name + "}"; }
            }
            """;

        Path srcDir = tmp.resolve("src");
        Path classesDir = tmp.resolve("classes");
        Path jarPath = tmp.resolve("foo.jar");
        Files.createDirectories(srcDir);
        Files.createDirectories(classesDir);
        Path pkgDir = srcDir.resolve("example");
        Files.createDirectories(pkgDir);
        Path javaFile = pkgDir.resolve("Foo.java");
        Files.writeString(javaFile, src);

        // compile the test class
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "No system Java compiler. Use a JDK to run tests.");
        int rc = compiler.run(null, null, null,
            "-d", classesDir.toString(),
            javaFile.toString());
        assertEquals(0, rc, "Compilation failed");

        // put the test class in a jar file
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jarPath))) {
            Path classFile = classesDir.resolve("example").resolve("Foo.class");
            jos.putNextEntry(new JarEntry("example/Foo.class"));
            Files.copy(classFile, jos);
            jos.closeEntry();
        }

        // load with URLClassLoader
        try (URLClassLoader fooLoader = new URLClassLoader(new URL[]{jarPath.toUri().toURL()}, null)) {
            Class<?> fooClass = Class.forName("example.Foo", true, fooLoader);
            Constructor<?> ctor = fooClass.getConstructor(String.class);
            Object fooInstance = ctor.newInstance("test");

            String hex = Neo4jUtil.serialize(fooInstance);

            // deserialize with default class loader
            PMException ex = assertThrows(
                PMException.class,
                () -> Neo4jUtil.deserialize(hex, getClass().getClassLoader())
            );
            assertTrue(ex.getCause() instanceof ClassNotFoundException);

            // deserialize with the url class loader
            Object obj = Neo4jUtil.deserialize(hex, fooLoader);

            assertNotNull(obj);
            assertEquals("example.Foo", obj.getClass().getName());
            assertSame(fooLoader, obj.getClass().getClassLoader());

            // Check the field value via getter
            Method getName = obj.getClass().getMethod("getName");
            assertEquals("test", getName.invoke(obj));
        }
    }
}