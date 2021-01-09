package edu.tarleton.checker;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class StatementCloneTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private CloneChecker test(String inputFile, String rename) throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", rename);
        return test(inputFile, conf);
    }

    private CloneChecker test(String inputFile, Properties conf) throws Exception {
        conf.setProperty("inputFile", "src/test/stmt/" + inputFile);
        conf.setProperty("level", "statements");
        CloneChecker checker = CloneChecker.instance(conf);
        checker.process();
        return checker;
    }

    @Test
    public void test1b() throws Exception {
        CloneChecker check = test("test1.xml", "blind");
        assertEquals(5, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test1c() throws Exception {
        CloneChecker check = test("test1.xml", "consistent");
        assertEquals(5, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test2a() throws Exception {
        CloneChecker check = test("test2.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test2b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("addBlocks", "true");
        CloneChecker check = test("test2.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("normalization"));
    }

    @Test
    public void test2c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("addBlocks", "true");
        CloneChecker check = test("test2.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("normalization"));
    }

    @Test
    public void test3b() throws Exception {
        CloneChecker check = test("test3.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(3, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
        assertEquals(true, check.getComments().containsValue("literals"));
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test3c() throws Exception {
        CloneChecker check = test("test3.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(2, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("literals"));
    }

    @Test
    public void test4b() throws Exception {
        CloneChecker check = test("test4.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(3, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
    }

    @Test
    public void test4c() throws Exception {
        CloneChecker check = test("test4.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(3, check.getNotClones().size());
    }

    @Test
    public void test5b() throws Exception {
        CloneChecker check = test("test5.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(3, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
        assertEquals(true, check.getComments().containsValue("literals"));
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test5c() throws Exception {
        CloneChecker check = test("test5.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(3, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
        assertEquals(true, check.getComments().containsValue("literals"));
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test6a() throws Exception {
        CloneChecker check = test("test6.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test6b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreUnaryAtLiterals", "true");
        CloneChecker check = test("test6.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test6c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreUnaryAtLiterals", "true");
        CloneChecker check = test("test6.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test7a() throws Exception {
        CloneChecker check = test("test7.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test7b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatNullAsLiteral", "true");
        CloneChecker check = test("test7.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test7c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatNullAsLiteral", "true");
        CloneChecker check = test("test7.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test8a() throws Exception {
        CloneChecker check = test("test8.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test8b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreParentheses", "true");
        CloneChecker check = test("test8.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("normalization"));
    }

    @Test
    public void test8c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreParentheses", "true");
        CloneChecker check = test("test8.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("normalization"));
    }

    @Test
    public void test9a() throws Exception {
        CloneChecker check = test("test9.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test9b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test9.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
    }

    @Test
    public void test9c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test9.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
    }
}
