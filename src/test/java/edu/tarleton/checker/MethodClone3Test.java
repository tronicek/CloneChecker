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
public class MethodClone3Test {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private Clone3Checker test(String inputFile, String rename) throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", rename);
        return test(inputFile, conf);
    }

    private Clone3Checker test(String inputFile, Properties conf) throws Exception {
        conf.setProperty("type", "3");
        conf.setProperty("inputFile", "src/test/method3/" + inputFile);
        conf.setProperty("level", "method");
        Clone3Checker checker = Clone3Checker.instance(conf);
        checker.process();
        return checker;
    }

    @Test
    public void test1() throws Exception {
        Clone3Checker check = test("test1.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(2, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test2a() throws Exception {
        Clone3Checker check = test("test2a.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test2b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("addBlocks", "true");
        Clone3Checker check = test("test2b.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
        assertEquals(true, check.getComments().containsValue("normalization"));
    }

    @Test
    public void test3a() throws Exception {
        Clone3Checker check = test("test3.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test3b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreUnaryAtLiterals", "true");
        Clone3Checker check = test("test3.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test4() throws Exception {
        Clone3Checker check = test("test4.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test5a() throws Exception {
        Clone3Checker check = test("test5.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test5b() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreParentheses", "true");
        Clone3Checker check = test("test5.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test6() throws Exception {
        Clone3Checker check = test("test6.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(2, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test7() throws Exception {
        Clone3Checker check = test("test7.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(3, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test8() throws Exception {
        Clone3Checker check = test("test8.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test9() throws Exception {
        Clone3Checker check = test("test9.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test10() throws Exception {
        Clone3Checker check = test("test10.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test11() throws Exception {
        Clone3Checker check = test("test11.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test12() throws Exception {
        Clone3Checker check = test("test12.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test12a() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        Clone3Checker check = test("test13.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test13() throws Exception {
        Clone3Checker check = test("test13.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test13a() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        Clone3Checker check = test("test13.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test14() throws Exception {
        Clone3Checker check = test("test14.xml", "blind");
        assertEquals(0, check.getType1().size());
        assertEquals(0, check.getType2().size());
        assertEquals(1, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }

    @Test
    public void test14a() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("ignoreAnnotations", "true");
        Clone3Checker check = test("test14.xml", conf);
        assertEquals(0, check.getType1().size());
        assertEquals(1, check.getType2().size());
        assertEquals(0, check.getType3().size());
        assertEquals(0, check.getNotClones().size());
    }
}
