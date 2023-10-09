package edu.tarleton.checker;

import com.github.javaparser.JavaParser;
import com.github.javaparser.JavaToken;
import com.github.javaparser.ParseResult;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.body.BodyDeclaration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * The parser that parses methods, constructors and initializers.
 *
 * @author Zdenek Tronicek
 */
public class MethodParser extends Parser {

    public MethodParser(Properties conf) {
        super(conf);
    }

    @Override
    public Tokens parse(String code, boolean normalize) {
        JavaParser parser = new JavaParser(parserConfiguration);
        BodyDeclaration body = parseBody(parser, code);
        List<JavaToken> tokens = tokenize(body);
        boolean wasNormalized = false;
        if (normalize) {
            NormalizingVisitor norm = new NormalizingVisitor(conf, tokens);
            norm.visitBody(body);
            wasNormalized = norm.wasModified();
        }
        return new Tokens(tokens, wasNormalized);
    }

    @Override
    public RenamedTokens parseRename(String code, boolean normalize) {
        JavaParser parser = new JavaParser(parserConfiguration);
        BodyDeclaration body = parseBody(parser, code);
        List<JavaToken> tokens = tokenize(body);
        boolean wasNormalized = false;
        if (normalize) {
            NormalizingVisitor norm = new NormalizingVisitor(conf, tokens);
            norm.visitBody(body);
            wasNormalized = norm.wasModified();
        }
        RenamingVisitor ren = new RenamingVisitor(conf, tokens);
        ren.visitBody(body);
        return new RenamedTokens(tokens, ren.getIdentifiers(), wasNormalized);
    }

    private BodyDeclaration parseBody(JavaParser parser, String code) {
        ParseResult<BodyDeclaration> result = parser.parseBodyDeclaration(code);
        if (!result.isSuccessful()) {
            System.err.println("parser error " + result.getProblems());
            System.err.println(code);
            throw new ParseException();
        }
        return result.getResult().get();
    }

    private List<JavaToken> tokenize(BodyDeclaration<?> body) {
        List<JavaToken> tokens = new ArrayList<>();
        Optional<TokenRange> opt = body.getTokenRange();
        if (opt.isPresent()) {
            TokenRange range = opt.get();
            JavaToken token = range.getBegin();
            JavaToken end = range.getEnd();
            while (token != end) {
                switch (token.getCategory()) {
                    case COMMENT:
                    case EOL:
                    case WHITESPACE_NO_EOL:
                        break;
                    default:
                        tokens.add(token);
                }
                token = token.getNextToken().orElse(null);
            }
            tokens.add(token);
        }
        return tokens;
    }
}
