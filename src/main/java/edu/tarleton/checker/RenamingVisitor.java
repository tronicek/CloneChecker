package edu.tarleton.checker;

import com.github.javaparser.JavaToken;
import com.github.javaparser.JavaToken.Category;
import com.github.javaparser.JavaToken.Kind;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.tarleton.checker.rename.RenameStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The visitor that renames identifiers.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class RenamingVisitor extends VoidVisitorAdapter<Void> {

    private final String rename;
    private final boolean ignoreAnnotations;
    private final boolean treatSuperThisAsIdentifier;
    private final boolean treatVoidAsType;
    private final RenameStrategy rs;
    private final List<JavaToken> tokens;
    private final List<String> identifiers = new ArrayList<>();

    public RenamingVisitor(Properties conf, List<JavaToken> tokens) {
        ignoreAnnotations = Boolean.parseBoolean(conf.getProperty("ignoreAnnotations", "false"));
        treatVoidAsType = Boolean.parseBoolean(conf.getProperty("treatVoidAsType", "false"));
        treatSuperThisAsIdentifier = Boolean.parseBoolean(conf.getProperty("treatSuperThisAsIdentifier", "false"));
        rename = conf.getProperty("rename", "blind");
        rs = RenameStrategy.instance(rename);
        this.tokens = tokens;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void visitBody(BodyDeclaration body) {
        if (body.isConstructorDeclaration()) {
            visit(body.asConstructorDeclaration(), null);
            return;
        }
        if (body.isMethodDeclaration()) {
            visit(body.asMethodDeclaration(), null);
        }
        if (body.isInitializerDeclaration()) {
            visit(body.asInitializerDeclaration(), null);
        }
    }

    public void visitStmt(Statement stmt) {
        if (stmt.isBlockStmt()) {
            visit(stmt.asBlockStmt(), null);
            return;
        }
        if (stmt.isExplicitConstructorInvocationStmt()) {
            visit(stmt.asExplicitConstructorInvocationStmt(), null);
            return;
        }
        throw new AssertionError("invalid statement: " + stmt);
    }

    private void declareVar(String name) {
        rs.declare(name);
    }

    private String rename(String name) {
        identifiers.add(name);
        String ren = rs.rename(name);
        if (ren == null) {
            ren = rs.declareGlobal(name);
        }
        //System.out.printf("renaming %s -> %s%n", name, ren);
        return ren;
    }

    private void replaceIdent(Node n, String ident) {
        TokenRange range = n.getTokenRange().get();
        String ren = rename(ident);
        JavaToken tok = range.getBegin();
        JavaToken end = range.getEnd();
        while (tok != end) {
            if (tok.getCategory() == Category.IDENTIFIER
                    && ident.equals(tok.getText())) {
                tok.setText(ren);
            }
            tok = tok.getNextToken().orElse(null);
        }
        if (tok.getCategory() == Category.IDENTIFIER
                && ident.equals(tok.getText())) {
            tok.setText(ren);
        }
    }

    private void replaceToken(JavaToken original, JavaToken repl) {
        for (int i = 0; i < tokens.size(); i++) {
            JavaToken tok = tokens.get(i);
            if (tok == original) {
                tokens.set(i, repl);
                break;
            }
        }
    }

    @Override
    public void visit(ArrayCreationExpr n, Void arg) {
        n.getElementType().accept(this, arg);
        n.getLevels().forEach(p -> p.accept(this, arg));
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ArrayType n, Void arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComponentType().accept(this, arg);
    }

    @Override
    public void visit(AssertStmt n, Void arg) {
        n.getCheck().accept(this, arg);
        n.getMessage().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
    }

    @Override
    public void visit(BlockStmt n, Void arg) {
        rs.enterBlock();
        n.getStatements().accept(this, arg);
        rs.exitBlock();
    }

    @Override
    public void visit(CastExpr n, Void arg) {
        n.getType().accept(this, arg);
        n.getExpression().accept(this, arg);
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        rs.enterBlock();
        n.getParameter().accept(this, arg);
        n.getBody().accept(this, arg);
        rs.exitBlock();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getTypeArguments().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void arg) {
        n.getTypeArguments().ifPresent(p -> p.accept(this, arg));
        if (treatSuperThisAsIdentifier) {
            TokenRange range = n.getTokenRange().get();
            if (n.isThis()) {
                replaceThis(range);
            } else {
                replaceSuper(range);
            }
        }
        n.getExpression().ifPresent(p -> p.accept(this, arg));
        n.getArguments().forEach(p -> p.accept(this, arg));
    }

    private void replaceThis(TokenRange range) {
        String ren = rename("this");
        JavaToken newTok = new JavaToken(Kind.IDENTIFIER.getKind(), ren);
        JavaToken tok = findToken(range, Kind.THIS.getKind());
        newTok.setRange(tok.getRange().get());
        replaceToken(tok, newTok);
    }

    private void replaceSuper(TokenRange range) {
        String ren = rename("super");
        JavaToken newTok = new JavaToken(Kind.IDENTIFIER.getKind(), ren);
        JavaToken tok = findToken(range, Kind.SUPER.getKind());
        newTok.setRange(tok.getRange().get());
        replaceToken(tok, newTok);
    }

    private JavaToken findToken(TokenRange range, int kind) {
        JavaToken tok = range.getBegin();
        JavaToken end = range.getEnd();
        while (tok != end) {
            if (tok.getKind() == kind) {
                return tok;
            }
            tok = tok.getNextToken().orElse(null);
        }
        if (tok.getKind() == kind) {
            return tok;
        }
        return null;
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        n.getScope().accept(this, arg);
        n.getName().accept(this, arg);
        n.getTypeArguments().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(FieldDeclaration n, Void arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        boolean first = true;
        for (VariableDeclarator var : n.getVariables()) {
            visitVar(var, arg, first);
            first = false;
        }
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        rs.enterBlock();
        n.getInitialization().accept(this, arg);
        n.getCompare().ifPresent(p -> p.accept(this, arg));
        n.getUpdate().accept(this, arg);
        n.getBody().accept(this, arg);
        rs.exitBlock();
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        rs.enterBlock();
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        rs.exitBlock();
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        n.getElseStmt().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        n.getExpression().accept(this, arg);
        n.getType().accept(this, arg);
    }

    @Override
    public void visit(LambdaExpr n, Void arg) {
        rs.enterBlock();
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
        rs.exitBlock();
    }

    @Override
    public void visit(MarkerAnnotationExpr n, Void arg) {
        if (ignoreAnnotations) {
            return;
        }
        n.getName().accept(this, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getTypeArguments().ifPresent(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getArguments().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        rs.enterBlock();
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getBody().ifPresent(p -> p.accept(this, arg));
        rs.exitBlock();
    }

    @Override
    public void visit(MethodReferenceExpr n, Void arg) {
        n.getScope().accept(this, arg);
        replaceIdent(n, n.getIdentifier());
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
    }

    @Override
    public void visit(Name n, Void arg) {
        n.getQualifier().ifPresent(p -> p.accept(this, arg));
        replaceIdent(n, n.getIdentifier());
    }

    @Override
    public void visit(NormalAnnotationExpr n, Void arg) {
        if (ignoreAnnotations) {
            return;
        }
        n.getName().accept(this, arg);
        n.getPairs().accept(this, arg);
    }

    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        n.getTypeArguments().ifPresent(p -> p.accept(this, arg));
        n.getArguments().forEach(p -> p.accept(this, arg));
        n.getAnonymousClassBody().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(Parameter n, Void arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        declareVar(n.getNameAsString());
        n.getName().accept(this, arg);
    }

    @Override
    public void visit(PrimitiveType n, Void arg) {
        TokenRange range = n.getTokenRange().get();
        JavaToken tok = range.getBegin();
        String ren = rename(n.asString());
        JavaToken newTok = new JavaToken(Kind.IDENTIFIER.getKind(), ren);
        newTok.setRange(tok.getRange().get());
        replaceToken(tok, newTok);
    }

    @Override
    public void visit(SimpleName n, Void arg) {
        replaceIdent(n, n.getIdentifier());
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Void arg) {
        if (ignoreAnnotations) {
            return;
        }
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
    }

    @Override
    public void visit(SuperExpr n, Void arg) {
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
        if (treatSuperThisAsIdentifier) {
            TokenRange range = n.getTokenRange().get();
            replaceSuper(range);
        }
    }

    @Override
    public void visit(SynchronizedStmt n, Void arg) {
        n.getExpression().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(SwitchStmt n, Void arg) {
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ThisExpr n, Void arg) {
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
        if (treatSuperThisAsIdentifier) {
            TokenRange range = n.getTokenRange().get();
            replaceThis(range);
        }
    }

    @Override
    public void visit(TryStmt n, Void arg) {
        rs.enterBlock();
        n.getResources().forEach(p -> p.accept(this, arg));
        n.getTryBlock().accept(this, arg);
        n.getCatchClauses().forEach(p -> p.accept(this, arg));
        n.getFinallyBlock().ifPresent(p -> p.accept(this, arg));
        rs.exitBlock();
    }

    @Override
    public void visit(TypeParameter n, Void arg) {
        n.getName().accept(this, arg);
        n.getTypeBound().accept(this, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        n.getAnnotations().accept(this, arg);
        n.getModifiers().accept(this, arg);
        boolean first = true;
        for (VariableDeclarator var : n.getVariables()) {
            visitVar(var, arg, first);
            first = false;
        }
    }

    private void visitVar(VariableDeclarator n, Void arg, boolean first) {
        if (first) {
            n.getType().accept(this, arg);
        }
//        if (!first) {
//            removeLastIdentifier();
//        }
        declareVar(n.getNameAsString());
        n.getName().accept(this, arg);
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
    }

    private void removeLastIdentifier() {
        int size = identifiers.size();
        identifiers.remove(size - 1);
    }

    @Override
    public void visit(VoidType n, Void arg) {
        if (treatVoidAsType) {
            TokenRange range = n.getTokenRange().get();
            JavaToken tok = range.getBegin();
            String ren = rename("void");
            JavaToken newTok = new JavaToken(Kind.IDENTIFIER.getKind(), ren);
            newTok.setRange(tok.getRange().get());
            replaceToken(tok, newTok);
        }
    }

    @Override
    public void visit(WildcardType n, Void arg) {
        if (n.getSuperType().isPresent()) {
            if (treatSuperThisAsIdentifier) {
                identifiers.add("super");
            }
            n.getSuperType().ifPresent(p -> p.accept(this, arg));
        } else {
            n.getExtendedType().ifPresent(p -> p.accept(this, arg));
        }
    }
}
