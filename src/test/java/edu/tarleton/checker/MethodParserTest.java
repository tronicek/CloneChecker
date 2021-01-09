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
public class MethodParserTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private RenamedTokens parse(String code, String rename) throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", rename);
        MethodParser parser = new MethodParser(conf);
        return parser.parseRename(code, false);
    }

    private RenamedTokens parseWith(String code, Properties conf) throws Exception {
        MethodParser parser = new MethodParser(conf);
        return parser.parseRename(code, false);
    }

    private String toString(List<JavaToken> tt) {
        StringBuilder sb = new StringBuilder();
        for (JavaToken t : tt) {
            sb.append(t.asString())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    private String toStr(List<String> ids) {
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            sb.append(id)
                    .append(" ");
        }
        return sb.toString().trim();
    }

    @Test
    public void test1b() throws Exception {
        RenamedTokens tt = parse("int sum(int x, int y) { return x + y; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id , id id ) { return id + id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int sum int x int y x y", ids);
    }

    @Test
    public void test1c() throws Exception {
        RenamedTokens tt = parse("int sum(int x, int y) { return x + y; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id0 id2 , id0 id3 ) { return id2 + id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int sum int x int y x y", ids);
    }

    @Test
    public void test2b() throws Exception {
        RenamedTokens tt = parse("void print(int x) { System.out.println(x); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("void id ( id id ) { id . id . id ( id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("print int x System out println x", ids);
    }

    @Test
    public void test2bx() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "blind");
        conf.setProperty("treatVoidAsType", "true");
        RenamedTokens tt = parseWith("void print(int x) { System.out.println(x); }", conf);
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id ) { id . id . id ( id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("void print int x System out println x", ids);
    }

    @Test
    public void test2c() throws Exception {
        RenamedTokens tt = parse("void print(int x) { System.out.println(x); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("void id0 ( id1 id2 ) { id3 . id4 . id5 ( id2 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("print int x System out println x", ids);
    }

    @Test
    public void test2cx() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("rename", "consistent");
        conf.setProperty("treatVoidAsType", "true");
        RenamedTokens tt = parseWith("void print(int x) { System.out.println(x); }", conf);
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id2 id3 ) { id4 . id5 . id6 ( id3 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("void print int x System out println x", ids);
    }

    @Test
    public void test3b() throws Exception {
        RenamedTokens tt = parse("Point(int x, int y) { this.x = x; this.y = y; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id ( id id , id id ) { this . id = id ; this . id = id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Point int x int y x x y y", ids);
    }

    @Test
    public void test3c() throws Exception {
        RenamedTokens tt = parse("Point(int x, int y) { this.x = x; this.y = y; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 ( id1 id2 , id1 id3 ) { this . id2 = id2 ; this . id3 = id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Point int x int y x x y y", ids);
    }

    @Test
    public void test4b() throws Exception {
        RenamedTokens tt = parse("void move(int dx, int dy) { x += dx; y += dy; print(x, y); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("void id ( id id , id id ) { id += id ; id += id ; id ( id , id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("move int dx int dy x dx y dy print x y", ids);
    }

    @Test
    public void test4c() throws Exception {
        RenamedTokens tt = parse("void move(int dx, int dy) { x += dx; y += dy; print(x, y); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("void id0 ( id1 id2 , id1 id3 ) { id4 += id2 ; id5 += id3 ; id6 ( id4 , id5 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("move int dx int dy x dx y dy print x y", ids);
    }

    @Test
    public void test5b() throws Exception {
        RenamedTokens tt = parse("Object instance(String make) { return new Car(make); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id ) { return new id ( id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Object instance String make Car make", ids);
    }

    @Test
    public void test5c() throws Exception {
        RenamedTokens tt = parse("Object instance(String make) { return new Car(make); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id2 id3 ) { return new id4 ( id3 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Object instance String make Car make", ids);
    }

    @Test
    public void test6b() throws Exception {
        RenamedTokens tt = parse("Object instance(String make) { return new edu.tarleton.Car(make); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id ) { return new id . id . id ( id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Object instance String make edu tarleton Car make", ids);
    }

    @Test
    public void test6c() throws Exception {
        RenamedTokens tt = parse("Object instance(String make) { return new edu.tarleton.Car(make); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id2 id3 ) { return new id4 . id5 . id6 ( id3 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Object instance String make edu tarleton Car make", ids);
    }

    @Test
    public void test7b() throws Exception {
        RenamedTokens tt = parse("int fib(int n) { if (n == 1 || n == 2) { return 1; } return fib(n-1) + fib(n-2); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id ) { if ( id == 1 || id == 2 ) { return 1 ; } return id ( id - 1 ) + id ( id - 2 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int fib int n n n fib n fib n", ids);
    }

    @Test
    public void test7c() throws Exception {
        RenamedTokens tt = parse("int fib(int n) { if (n == 1 || n == 2) { return 1; } return fib(n-1) + fib(n-2); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id0 id2 ) { if ( id2 == 1 || id2 == 2 ) { return 1 ; } return id1 ( id2 - 1 ) + id1 ( id2 - 2 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int fib int n n n fib n fib n", ids);
    }

    @Test
    public void test8b() throws Exception {
        RenamedTokens tt = parse("@Override String toString() { return null; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("@ id id id ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Override String toString", ids);
    }

    @Test
    public void test8c() throws Exception {
        RenamedTokens tt = parse("@Override String toString() { return null; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("@ id0 id1 id2 ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Override String toString", ids);
    }

    @Test
    public void test9b() throws Exception {
        RenamedTokens tt = parse("@java.lang.Override String toString() { return null; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("@ id . id . id id id ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("java lang Override String toString", ids);
    }

    @Test
    public void test9c() throws Exception {
        RenamedTokens tt = parse("@java.lang.Override String toString() { return null; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("@ id0 . id1 . id2 id3 id4 ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("java lang Override String toString", ids);
    }

    @Test
    public void test10b() throws Exception {
        RenamedTokens tt = parse("@SuppressWarnings(\"unchecked\") String toString() { return null; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("@ id ( \"unchecked\" ) id id ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("SuppressWarnings String toString", ids);
    }

    @Test
    public void test10c() throws Exception {
        RenamedTokens tt = parse("@SuppressWarnings(\"unchecked\") String toString() { return null; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("@ id0 ( \"unchecked\" ) id1 id2 ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("SuppressWarnings String toString", ids);
    }

    @Test
    public void test11b() throws Exception {
        RenamedTokens tt = parse("@java.lang.SuppressWarnings(\"unchecked\") String toString() { return null; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("@ id . id . id ( \"unchecked\" ) id id ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("java lang SuppressWarnings String toString", ids);
    }

    @Test
    public void test11c() throws Exception {
        RenamedTokens tt = parse("@java.lang.SuppressWarnings(\"unchecked\") String toString() { return null; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("@ id0 . id1 . id2 ( \"unchecked\" ) id3 id4 ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("java lang SuppressWarnings String toString", ids);
    }

    @Test
    public void test12b() throws Exception {
        RenamedTokens tt = parse("@Deprecated(since = \"1.2\", note = \"xxx\") String toString() { return null; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("@ id ( id = \"1.2\" , id = \"xxx\" ) id id ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Deprecated since note String toString", ids);
    }

    @Test
    public void test12c() throws Exception {
        RenamedTokens tt = parse("@Deprecated(since = \"1.2\", note = \"xxx\") String toString() { return null; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("@ id0 ( id1 = \"1.2\" , id2 = \"xxx\" ) id3 id4 ( ) { return null ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Deprecated since note String toString", ids);
    }

    @Test
    public void test13b() throws Exception {
        RenamedTokens tt = parse("<T> T ident(T t) { return t; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("< id > id id ( id id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("T T ident T t t", ids);
    }

    @Test
    public void test13c() throws Exception {
        RenamedTokens tt = parse("<T> T ident(T t) { return t; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("< id0 > id0 id1 ( id0 id2 ) { return id2 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("T T ident T t t", ids);
    }

    @Test
    public void test14b() throws Exception {
        RenamedTokens tt = parse("List<String> add(List<String> list, String s) { list.add(s); return list; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < id > id ( id < id > id , id id ) { id . id ( id ) ; return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List String add List String list String s list add s list", ids);
    }

    @Test
    public void test14c() throws Exception {
        RenamedTokens tt = parse("List<String> add(List<String> list, String s) { list.add(s); return list; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < id1 > id2 ( id0 < id1 > id3 , id1 id4 ) { id3 . id2 ( id4 ) ; return id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List String add List String list String s list add s list", ids);
    }

    @Test
    public void test15b() throws Exception {
        RenamedTokens tt = parse("List<java.lang.String> add(List<java.lang.String> list, String s) { list.add(s); return list; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < id . id . id > id ( id < id . id . id > id , id id ) { id . id ( id ) ; return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List java lang String add List java lang String list String s list add s list", ids);
    }

    @Test
    public void test15c() throws Exception {
        RenamedTokens tt = parse("List<java.lang.String> add(List<java.lang.String> list, String s) { list.add(s); return list; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < id1 . id2 . id3 > id4 ( id0 < id1 . id2 . id3 > id5 , id3 id6 ) { id5 . id4 ( id6 ) ; return id5 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List java lang String add List java lang String list String s list add s list", ids);
    }

    @Test
    public void test16b() throws Exception {
        RenamedTokens tt = parse("java.lang.String modify(String s) { return (java.lang.String) s; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id . id . id id ( id id ) { return ( id . id . id ) id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("java lang String modify String s java lang String s", ids);
    }

    @Test
    public void test16c() throws Exception {
        RenamedTokens tt = parse("java.lang.String modify(String s) { return (java.lang.String) s; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 . id1 . id2 id3 ( id2 id4 ) { return ( id0 . id1 . id2 ) id4 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("java lang String modify String s java lang String s", ids);
    }

    @Test
    public void test17b() throws Exception {
        RenamedTokens tt = parse("List<? extends Number> ident(List<? extends Number> list) { return list; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < ? extends id > id ( id < ? extends id > id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List Number ident List Number list list", ids);
    }

    @Test
    public void test17c() throws Exception {
        RenamedTokens tt = parse("List<? extends Number> ident(List<? extends Number> list) { return list; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < ? extends id1 > id2 ( id0 < ? extends id1 > id3 ) { return id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List Number ident List Number list list", ids);
    }

    @Test
    public void test18b() throws Exception {
        RenamedTokens tt = parse("List<? extends java.lang.Number> ident(List<? extends java.lang.Number> list) { return list; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < ? extends id . id . id > id ( id < ? extends id . id . id > id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List java lang Number ident List java lang Number list list", ids);
    }

    @Test
    public void test18c() throws Exception {
        RenamedTokens tt = parse("List<? extends java.lang.Number> ident(List<? extends java.lang.Number> list) { return list; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < ? extends id1 . id2 . id3 > id4 ( id0 < ? extends id1 . id2 . id3 > id5 ) { return id5 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List java lang Number ident List java lang Number list list", ids);
    }

    @Test
    public void test19b() throws Exception {
        RenamedTokens tt = parse("List<? super Number> ident(List<? super Number> list) { return list; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < ? super id > id ( id < ? super id > id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List Number ident List Number list list", ids);
    }

    @Test
    public void test19c() throws Exception {
        RenamedTokens tt = parse("List<? super Number> ident(List<? super Number> list) { return list; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < ? super id1 > id2 ( id0 < ? super id1 > id3 ) { return id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List Number ident List Number list list", ids);
    }

    @Test
    public void test20b() throws Exception {
        RenamedTokens tt = parse("List<? super java.lang.Number> ident(List<? super java.lang.Number> list) { return list; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < ? super id . id . id > id ( id < ? super id . id . id > id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List java lang Number ident List java lang Number list list", ids);
    }

    @Test
    public void test20c() throws Exception {
        RenamedTokens tt = parse("List<? super java.lang.Number> ident(List<? super java.lang.Number> list) { return list; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < ? super id1 . id2 . id3 > id4 ( id0 < ? super id1 . id2 . id3 > id5 ) { return id5 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("List java lang Number ident List java lang Number list list", ids);
    }

    @Test
    public void test21b() throws Exception {
        RenamedTokens tt = parse("<T extends Number> T ident(T t, Number n) { return t; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("< id extends id > id id ( id id , id id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("T Number T ident T t Number n t", ids);
    }

    @Test
    public void test21c() throws Exception {
        RenamedTokens tt = parse("<T extends Number> T ident(T t, Number n) { return t; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("< id0 extends id1 > id0 id2 ( id0 id3 , id1 id4 ) { return id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("T Number T ident T t Number n t", ids);
    }

    @Test
    public void test22b() throws Exception {
        RenamedTokens tt = parse("<T extends Number & Serializable> T ident(T t, Number n) { return t; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("< id extends id & id > id id ( id id , id id ) { return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("T Number Serializable T ident T t Number n t", ids);
    }

    @Test
    public void test22c() throws Exception {
        RenamedTokens tt = parse("<T extends Number & Serializable> T ident(T t, Number n) { return t; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("< id0 extends id1 & id2 > id0 id3 ( id0 id4 , id1 id5 ) { return id4 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("T Number Serializable T ident T t Number n t", ids);
    }

    @Test
    public void test23b() throws Exception {
        RenamedTokens tt = parse("Map<Integer, String> emptyMap() { return new HashMap<>(); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id < id , id > id ( ) { return new id < > ( ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Map Integer String emptyMap HashMap", ids);
    }

    @Test
    public void test23c() throws Exception {
        RenamedTokens tt = parse("Map<Integer, String> emptyMap() { return new HashMap<>(); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 < id1 , id2 > id3 ( ) { return new id4 < > ( ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Map Integer String emptyMap HashMap", ids);
    }

    @Test
    public void test24b() throws Exception {
        RenamedTokens tt = parse("double pi2() { return Math.PI * Math.PI; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( ) { return id . id * id . id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("double pi2 Math PI Math PI", ids);
    }

    @Test
    public void test24c() throws Exception {
        RenamedTokens tt = parse("double pi2() { return Math.PI * Math.PI; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( ) { return id2 . id3 * id2 . id3 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("double pi2 Math PI Math PI", ids);
    }

    @Test
    public void test25b() throws Exception {
        RenamedTokens tt = parse("Object[] contents() { Object[] c = { new Point(1, 2), new Point(3, 4) , }; return c; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id [ ] id ( ) { id [ ] id = { new id ( 1 , 2 ) , new id ( 3 , 4 ) , } ; return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Object contents Object c Point Point c", ids);
    }

    @Test
    public void test25c() throws Exception {
        RenamedTokens tt = parse("Object[] contents() { Object[] c = { new Point(1, 2), new Point(3, 4) , }; return c; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 [ ] id1 ( ) { id0 [ ] id2 = { new id3 ( 1 , 2 ) , new id3 ( 3 , 4 ) , } ; return id2 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("Object contents Object c Point Point c", ids);
    }

    @Test
    public void test26b() throws Exception {
        RenamedTokens tt = parse("String toString(Point p, Rectangle r) { return p.toString() + r.toString(); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id , id id ) { return id . id ( ) + id . id ( ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("String toString Point p Rectangle r p toString r toString", ids);
    }

    @Test
    public void test26c() throws Exception {
        RenamedTokens tt = parse("String toString(Point p, Rectangle r) { return p.toString() + r.toString(); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id2 id3 , id4 id5 ) { return id3 . id1 ( ) + id5 . id1 ( ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("String toString Point p Rectangle r p toString r toString", ids);
    }

    @Test
    public void test27b() throws Exception {
        RenamedTokens tt = parse("boolean empty(List<String> list) { int size = list.size(); if (size > 0) { c++; return false; } else { d--; return true; } }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id < id > id ) { id id = id . id ( ) ; if ( id > 0 ) { id ++ ; return false ; } else { id -- ; return true ; } }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("boolean empty List String list int size list size size c d", ids);
    }

    @Test
    public void test27c() throws Exception {
        RenamedTokens tt = parse("boolean empty(List<String> list) { int size = list.size(); if (size > 0) { c++; return false; } else { d--; return true; } }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id2 < id3 > id4 ) { id5 id6 = id4 . id6 ( ) ; if ( id6 > 0 ) { id7 ++ ; return false ; } else { id8 -- ; return true ; } }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("boolean empty List String list int size list size size c d", ids);
    }

    @Test
    public void test28b() throws Exception {
        RenamedTokens tt = parse("int max(int a, int b, int c) { if (a < b) { int m = b; if (c > m) { m = c; } return m; } else { int m = a; if (b > m) { m = b; } return m; } }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id id ( id id , id id , id id ) { if ( id < id ) { id id = id ; if ( id > id ) { id = id ; } return id ; } else { id id = id ; if ( id > id ) { id = id ; } return id ; } }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int max int a int b int c a b int m b c m m c m int m a b m m b m", ids);
    }

    @Test
    public void test28c() throws Exception {
        RenamedTokens tt = parse("int max(int a, int b, int c) { if (a < b) { int m = b; if (c > m) { m = c; } return m; } else { int m = a; if (b > m) { m = b; } return m; } }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id0 id2 , id0 id3 , id0 id4 ) { if ( id2 < id3 ) { id0 id5 = id3 ; if ( id4 > id5 ) { id5 = id4 ; } return id5 ; } else { id0 id6 = id2 ; if ( id3 > id6 ) { id6 = id3 ; } return id6 ; } }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int max int a int b int c a b int m b c m m c m int m a b m m b m", ids);
    }

    @Test
    public void test29b() throws Exception {
        RenamedTokens tt = parse("void print() { int i = 0, j = 1; sum(i, j); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("void id ( ) { id id = 0 , id = 1 ; id ( id , id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("print int i j sum i j", ids);
    }

    @Test
    public void test29c() throws Exception {
        RenamedTokens tt = parse("void print() { int i = 0, j = 1; sum(i, j); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("void id0 ( ) { id1 id2 = 0 , id3 = 1 ; id4 ( id2 , id3 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("print int i j sum i j", ids);
    }

    @Test
    public void test30b() throws Exception {
        RenamedTokens tt = parse("static { build(types); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("static { id ( id ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("build types", ids);
    }

    @Test
    public void test30c() throws Exception {
        RenamedTokens tt = parse("static { build(types); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("static { id0 ( id1 ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("build types", ids);
    }

    @Test
    public void test31b() throws Exception {
        RenamedTokens tt = parse("void m() { print(A.this); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("void id ( ) { id ( id . this ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("m print A", ids);
    }

    @Test
    public void test31c() throws Exception {
        RenamedTokens tt = parse("void m() { print(A.this); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("void id0 ( ) { id1 ( id2 . this ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("m print A", ids);
    }

    @Test
    public void test32b() throws Exception {
        RenamedTokens tt = parse("int[] create() { int[] p = { 1, 2, 3 }; return p; }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("id [ ] id ( ) { id [ ] id = { 1 , 2 , 3 } ; return id ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int create int p p", ids);
    }

    @Test
    public void test32c() throws Exception {
        RenamedTokens tt = parse("int[] create() { int[] p = { 1, 2, 3 }; return p; }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 [ ] id1 ( ) { id0 [ ] id2 = { 1 , 2 , 3 } ; return id2 ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("int create int p p", ids);
    }

    @Test
    public void test33b() throws Exception {
        RenamedTokens tt = parse("void toString() { return p.toString() + \" \" + p.toString(); }", "blind");
        String code = toString(tt.getTokens());
        assertEquals("void id ( ) { return id . id ( ) + \" \" + id . id ( ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("toString p toString p toString", ids);
    }

    @Test
    public void test33c() throws Exception {
        RenamedTokens tt = parse("String toString(Point p) { return p.toString() + \" \" + p.toString(); }", "consistent");
        String code = toString(tt.getTokens());
        assertEquals("id0 id1 ( id2 id3 ) { return id3 . id1 ( ) + \" \" + id3 . id1 ( ) ; }", code);
        String ids = toStr(tt.getIdentifiers());
        assertEquals("String toString Point p p toString p toString", ids);
    }
}
