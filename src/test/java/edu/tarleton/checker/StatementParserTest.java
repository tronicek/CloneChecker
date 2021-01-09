package edu.tarleton.checker;

import com.github.javaparser.JavaToken;
import java.util.List;
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
public class StatementParserTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private String parse(String code, String rename) throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", rename);
        StatementParser parser = new StatementParser(conf);
        RenamedTokens tokens = parser.parseRename(code, false);
        return toString(tokens.getTokens());
    }

    private String parseWith(String code, Properties conf) throws Exception {
        StatementParser parser = new StatementParser(conf);
        RenamedTokens tokens = parser.parseRename(code, false);
        return toString(tokens.getTokens());
    }

    private String toString(List<JavaToken> tt) {
        StringBuilder sb = new StringBuilder();
        for (JavaToken t : tt) {
            sb.append(t.asString())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    @Test
    public void test1b() throws Exception {
        String code = parse("super(weight);", "blind");
        assertEquals("super ( id ) ;", code);
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        String code2 = parseWith("super(weight);", conf);
        assertEquals("id ( id ) ;", code2);
    }

    @Test
    public void test1c() throws Exception {
        String code = parse("super(weight);", "consistent");
        assertEquals("super ( id0 ) ;", code);
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        String code2 = parseWith("super(weight);", conf);
        assertEquals("id0 ( id1 ) ;", code2);
    }

    @Test
    public void tes2b() throws Exception {
        String code = parse("this(x, y);", "blind");
        assertEquals("this ( id , id ) ;", code);
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        String code2 = parseWith("this(x, y);", conf);
        assertEquals("id ( id , id ) ;", code2);
    }

    @Test
    public void test2c() throws Exception {
        String code = parse("this(x, y);", "consistent");
        assertEquals("this ( id0 , id1 ) ;", code);
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        String code2 = parseWith("this(x, y);", conf);
        assertEquals("id0 ( id1 , id2 ) ;", code2);
    }

    @Test
    public void test3b() throws Exception {
        String code = parse("super(); this.x = 0; this.y = 0;", "blind");
        assertEquals("super ( ) ; this . id = 0 ; this . id = 0 ;", code);
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        String code2 = parseWith("super(); this.x = 0; this.y = 0;", conf);
        assertEquals("id ( ) ; id . id = 0 ; id . id = 0 ;", code2);
    }

    @Test
    public void test3c() throws Exception {
        String code = parse("super(); this.x = 0; this.y = 0;", "consistent");
        assertEquals("super ( ) ; this . id0 = 0 ; this . id1 = 0 ;", code);
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatSuperThisAsIdentifier", "true");
        String code2 = parseWith("super(); this.x = 0; this.y = 0;", conf);
        assertEquals("id0 ( ) ; id1 . id2 = 0 ; id1 . id3 = 0 ;", code2);
    }

    @Test
    public void test4b() throws Exception {
        String code = parse("for (int i = 0; i < 10; i++) { add(i); }", "blind");
        assertEquals("for ( id id = 0 ; id < 10 ; id ++ ) { id ( id ) ; }", code);
    }

    @Test
    public void test4c() throws Exception {
        String code = parse("for (int i = 0; i < 10; i++) { add(i); }", "consistent");
        assertEquals("for ( id0 id1 = 0 ; id1 < 10 ; id1 ++ ) { id2 ( id1 ) ; }", code);
    }

    @Test
    public void test5b() throws Exception {
        String code = parse("for (String name: names) { display(name); }", "blind");
        assertEquals("for ( id id : id ) { id ( id ) ; }", code);
    }

    @Test
    public void test5c() throws Exception {
        String code = parse("for (String name: names) { display(name); }", "consistent");
        assertEquals("for ( id0 id1 : id2 ) { id3 ( id1 ) ; }", code);
    }

    @Test
    public void test6b() throws Exception {
        String code = parse("try (FileInputStream in = new FileInputStream(file)) { process(in); } catch (Exception e) { e.printStackTrace(); }", "blind");
        assertEquals("try ( id id = new id ( id ) ) { id ( id ) ; } catch ( id id ) { id . id ( ) ; }", code);
    }

    @Test
    public void test6c() throws Exception {
        String code = parse("try (FileInputStream in = new FileInputStream(fin)) { process(in); } catch (Exception e) { e.printStackTrace(); }", "consistent");
        assertEquals("try ( id0 id1 = new id0 ( id2 ) ) { id3 ( id1 ) ; } catch ( id4 id5 ) { id5 . id6 ( ) ; }", code);
    }
}
