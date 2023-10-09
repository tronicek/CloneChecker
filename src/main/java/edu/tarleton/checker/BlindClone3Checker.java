package edu.tarleton.checker;

import static com.github.javaparser.JavaToken.Category.IDENTIFIER;
import static com.github.javaparser.JavaToken.Category.KEYWORD;
import static com.github.javaparser.JavaToken.Category.LITERAL;
import com.github.javaparser.JavaToken;
import edu.tarleton.checker.Result.Diff;
import edu.tarleton.drdup2.nicad.NiCadClone;
import edu.tarleton.drdup2.nicad.NiCadClones;
import edu.tarleton.drdup2.nicad.NiCadSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * The checker for Type-3 clones and rename=blind.
 *
 * @author Zdenek Tronicek
 */
public class BlindClone3Checker extends Clone3Checker {

    public BlindClone3Checker(Properties conf) {
        super(conf);
    }

    @Override
    public void process() throws Exception {
        Parser parser = Parser.instantiate(conf);
        String input = conf.getProperty("inputFile");
        NiCadClones cls = readFile(input);
        for (NiCadClone clone : cls.getClones()) {
            processClone(parser, clone);
        }
        String outputDir = conf.getProperty("outputDir");
        if (outputDir == null) {
            System.out.printf("type 1: %d%n", type1.size());
            System.out.printf("type 2: %d%n", type2.size());
            System.out.printf("type 3: %d%n", type3.size());
            System.out.printf("not a clone: %d%n", notClones.size());
            System.out.printf("invalid: %d%n", invalid.size());
            return;
        }
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!outputDir.endsWith("/")) {
            outputDir += "/";
        }
        NiCadClones clonesType1 = new NiCadClones(type1);
        writeTextFile(clonesType1, comments, outputDir + "clones-type1.xml");
        NiCadClones clonesType2 = new NiCadClones(type2);
        writeTextFile(clonesType2, comments, outputDir + "clones-type2.xml");
        NiCadClones clonesType3 = new NiCadClones(type3);
        writeTextFile(clonesType3, comments, outputDir + "clones-type3.xml");
        NiCadClones notClones2 = new NiCadClones(notClones);
        writeTextFile(notClones2, comments, outputDir + "not-clones.xml");
        NiCadClones invalid2 = new NiCadClones(invalid);
        writeTextFile(invalid2, comments, outputDir + "invalid.xml");
        writeTextFile(cls, comments, outputDir + "all.xml");
    }

    private NiCadClones readFile(String fileName) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(NiCadClones.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        return (NiCadClones) unmarshaller.unmarshal(new File(fileName));
    }

    private void processClone(Parser parser, NiCadClone clone) {
        try {
            List<Tokens> tokens = new ArrayList<>();
            for (NiCadSource src : clone.getSources()) {
                Tokens tt = parser.parse(src.getSourceCode(), false);
                tokens.add(tt);
            }
            Result res = checkType1(tokens);
            if (res.isClone()) {
                type1.add(clone);
                String s = res.getDiffAsString();
                if (s.isEmpty()) {
                    s = "type 1";
                }
                comments.put(clone, s);
                return;
            }
            List<Tokens> tokens2 = new ArrayList<>();
            for (NiCadSource src : clone.getSources()) {
                Tokens tt = parser.parse(src.getSourceCode(), true);
                tokens2.add(tt);
            }
            Result res2 = checkType2(tokens2);
            if (res2.isClone()) {
                type2.add(clone);
                comments.put(clone, res2.getDiffAsString());
                return;
            }
            Result res3 = checkType3(clone.getDistance(), tokens2);
            if (res3.isClone()) {
                type3.add(clone);
                if (res3.hasDiff()) {
                    comments.put(clone, res3.getDiffAsString());
                }
                return;
            }
            comments.put(clone, "distance=\"" + res3.getDistance() + "\"");
            notClones.add(clone);
        } catch (ParseException e) {
            invalid.add(clone);
            comments.put(clone, "invalid");
        }
    }

    private Result checkType1(List<Tokens> tokens) {
        Set<Diff> diff = new TreeSet<>();
        for (int i = 0; i < tokens.size() - 1; i++) {
            Tokens tt1 = tokens.get(i);
            Tokens tt2 = tokens.get(i + 1);
            Result res = isType1(tt1, tt2);
            if (!res.isClone()) {
                return res;
            }
            diff.addAll(res.getDiff());
        }
        return new Result(true, diff);
    }

    private Result isType1(Tokens tt1, Tokens tt2) {
        if (tt1.size() != tt2.size()) {
            return new Result(false, null);
        }
        for (int i = 0; i < tt1.size(); i++) {
            JavaToken tok1 = tt1.getToken(i);
            JavaToken tok2 = tt2.getToken(i);
            if (!type1Equal(tok1, tok2)) {
                return new Result(false, null);
            }
        }
        Set<Result.Diff> diff = new TreeSet<>();
        if (tt1.wasNormalized() || tt2.wasNormalized()) {
            diff.add(Result.Diff.NORMALIZATION);
        }
        return new Result(true, diff);
    }

    private boolean type1Equal(JavaToken tok1, JavaToken tok2) {
        String s1 = tok1.getText();
        String s2 = tok2.getText();
        return tok1.getKind() == tok2.getKind() && s1.equals(s2);
    }

    public Result checkType2(List<Tokens> tokens) {
        Set<Diff> diff = new TreeSet<>();
        for (int i = 0; i < tokens.size() - 1; i++) {
            Tokens tt1 = tokens.get(i);
            Tokens tt2 = tokens.get(i + 1);
            Result res = isType2(tt1, tt2);
            if (!res.isClone()) {
                return res;
            }
            diff.addAll(res.getDiff());
        }
        return new Result(true, diff);
    }

    private Result isType2(Tokens tt1, Tokens tt2) {
        if (tt1.size() != tt2.size()) {
            return new Result(false, null);
        }
        Set<Diff> diff = new TreeSet<>();
        for (int i = 0; i < tt1.size(); i++) {
            JavaToken tok1 = tt1.getToken(i);
            JavaToken tok2 = tt2.getToken(i);
            Result res = equal(tok1, tok2);
            if (!res.isClone()) {
                return new Result(false, null);
            }
            diff.addAll(res.getDiff());
        }
        if (tt1.wasNormalized() || tt2.wasNormalized()) {
            diff.add(Result.Diff.NORMALIZATION);
        }
        return new Result(true, diff);
    }

    public Result checkType3(int distance, List<Tokens> tokens) {
        Set<Diff> diff = new TreeSet<>();
        for (int i = 0; i < tokens.size() - 1; i++) {
            Tokens tt1 = tokens.get(i);
            Tokens tt2 = tokens.get(i + 1);
            Result res = isType3(distance, tt1, tt2);
            if (!res.isClone()) {
                return res;
            }
            diff.addAll(res.getDiff());
        }
        return new Result(true, diff);
    }

    private Result isType3(int distance, Tokens tt1, Tokens tt2) {
        Set<Diff> diff = new TreeSet<>();
        if (tt1.wasNormalized() || tt2.wasNormalized()) {
            diff.add(Result.Diff.NORMALIZATION);
        }
        int d = levenshteinDistance(tt1.getTokens(), tt2.getTokens());
        if (d != distance) {
            System.out.printf("Distance: %d, expected distance: %d%n", d, distance);
            return new Result(false, diff, d);
        }
        return new Result(true, diff);
    }

    private int levenshteinDistance(List<JavaToken> tt1, List<JavaToken> tt2) {
        int m = tt1.size();
        int[] d = new int[m + 1];
        for (int i = 0; i < d.length; i++) {
            d[i] = i;
        }
        int[] nd = new int[d.length];
        for (int i = 0; i < tt2.size(); i++) {
            nd[0] = i + 1;
            for (int j = 0; j < m; j++) {
                JavaToken t1 = tt1.get(j);
                JavaToken t2 = tt2.get(i);
                Result res = equal(t1, t2);
                if (res.isClone()) {
                    nd[j + 1] = d[j];
                } else {
                    nd[j + 1] = 1 + min(d[j + 1], nd[j], d[j]);
                }
            }
            int[] p = d;
            d = nd;
            nd = p;
        }
        return d[m];
    }

    private Result equal(JavaToken tok1, JavaToken tok2) {
        String s1 = tok1.getText();
        String s2 = tok2.getText();
        if (tok1.getKind() == tok2.getKind() && s1.equals(s2)) {
            return new Result(true, Collections.emptySet());
        }
        if (isIdentifier(tok1) && isIdentifier(tok2)) {
            return new Result(true, Collections.singleton(Result.Diff.IDENTIFIERS));
        }
        if (isLiteral(tok1) && isLiteral(tok2)) {
            return new Result(true, Collections.singleton(Result.Diff.LITERALS));
        }
        return new Result(false, null);
    }

    private boolean isIdentifier(JavaToken token) {
        switch (token.getCategory()) {
            case IDENTIFIER:
                return true;
            case KEYWORD:
                String s = token.getText();
                if (treatSuperThisAsIdentifier && isSuperThis(s)) {
                    return true;
                }
                return isPrimitiveType(s);
            default:
                return false;
        }
    }

    private boolean isSuperThis(String token) {
        return token.equals("super") || token.equals("this");
    }

    private boolean isPrimitiveType(String token) {
        switch (token) {
            case "boolean":
            case "byte":
            case "char":
            case "double":
            case "float":
            case "int":
            case "long":
            case "short":
                return true;
            default:
                return false;
        }
    }

    private boolean isLiteral(JavaToken token) {
        switch (token.getCategory()) {
            case LITERAL:
                return true;
            case KEYWORD:
                String s = token.getText();
                if (s.equals("null") && treatNullAsLiteral) {
                    return true;
                }
                return s.equals("false") || s.equals("true");
            default:
                return false;
        }
    }

    private int min(int a, int b, int c) {
        int m = a < b ? a : b;
        if (c < m) {
            m = c;
        }
        return m;
    }

    private void writeTextFile(NiCadClones clones, Map<NiCadClone, String> comments, String fileName) throws Exception {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            out.println("<clones>");
            for (NiCadClone clone : clones.getClones()) {
                out.printf("    <clone nlines=\"%d\" distance=\"%d\">%n", clone.getNlines(), clone.getDistance());
                String comment = comments.get(clone);
                if (comment != null) {
                    out.printf("        <!-- %s -->%n", comment);
                }
                for (NiCadSource src : clone.getSources()) {
                    out.printf("        <source file=\"%s\" startline=\"%d\" endline=\"%d\">", src.getFile(), src.getStartline(), src.getEndline());
                    String code = src.getSourceCode()
                            .replace("&", "&amp;")
                            .replace("<", "&lt;")
                            .replace(">", "&gt;");
                    out.print(code);
                    out.println("</source>");
                }
                out.println("    </clone>");
            }
            out.println("</clones>");
        }
    }
}
