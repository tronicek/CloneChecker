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
public class MethodCloneTest {

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
        conf.setProperty("inputFile", "src/test/method/" + inputFile);
        conf.setProperty("level", "method");
        CloneChecker checker = CloneChecker.instance(conf);
        checker.process();
        return checker;
    }

    @Test
    public void test1() throws Exception {
        CloneChecker check = test("test1.xml", "blind");
        assertEquals(2, check.getType1().size());
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
    public void test3a() throws Exception {
        CloneChecker check = test("test3.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test3b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreUnaryAtLiterals", "true");
        CloneChecker check = test("test3.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test3c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreUnaryAtLiterals", "true");
        CloneChecker check = test("test3.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test4a() throws Exception {
        CloneChecker check = test("test4.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test4b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test4.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test4c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test4.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test5a() throws Exception {
        CloneChecker check = test("test5.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test5b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreParentheses", "true");
        CloneChecker check = test("test5.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test5c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreParentheses", "true");
        CloneChecker check = test("test5.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test6b() throws Exception {
        CloneChecker check = test("test6.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(2, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test6c() throws Exception {
        CloneChecker check = test("test6.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(2, check.getNotClones().size());
    }

    @Test
    public void test7b() throws Exception {
        CloneChecker check = test("test7.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(3, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
        assertEquals(true, check.getComments().containsValue("literals"));
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test7c() throws Exception {
        CloneChecker check = test("test7.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(3, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
        assertEquals(true, check.getComments().containsValue("literals"));
        assertEquals(true, check.getComments().containsValue("identifiers, literals"));
    }

    @Test
    public void test8b() throws Exception {
        CloneChecker check = test("test8.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test8c() throws Exception {
        CloneChecker check = test("test8.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test9b() throws Exception {
        CloneChecker check = test("test9.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
    }

    @Test
    public void test9c() throws Exception {
        CloneChecker check = test("test9.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("identifiers"));
    }

    @Test
    public void test10a() throws Exception {
        CloneChecker check = test("test10.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test10b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatNullAsLiteral", "true");
        CloneChecker check = test("test10.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test10c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatNullAsLiteral", "true");
        CloneChecker check = test("test10.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test11a() throws Exception {
        CloneChecker check = test("test11.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test11b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test11.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test11c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test11.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test12a() throws Exception {
        CloneChecker check = test("test12.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test12b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test12.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test12c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("ignoreAnnotations", "true");
        CloneChecker check = test("test12.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test13a() throws Exception {
        CloneChecker check = test("test13.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test13b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test13.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test13c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test13.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test14a() throws Exception {
        CloneChecker check = test("test14.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test14b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test14.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test14c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test14.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test15a() throws Exception {
        CloneChecker check = test("test15.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test15b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test15.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test15c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test15.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test16a() throws Exception {
        CloneChecker check = test("test16.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test16b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test16.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test16c() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test16.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getNotClones().size());
    }

    @Test
    public void test17a() throws Exception {
        CloneChecker check = test("test17.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test17b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test17.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test17c() throws Exception {
        CloneChecker check = test("test17.xml", "consistent");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test17d() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        CloneChecker check = test("test17.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getNotClones().size());
    }
}
