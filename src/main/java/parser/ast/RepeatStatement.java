//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.GJNoArguVisitor;
import parser.visitor.GJVisitor;
import parser.visitor.GJVoidVisitor;
import parser.visitor.Visitor;

/**
 * Grammar production:
 * f0 -> <REPEAT>
 * f1 -> IntegerLiteral()
 * f2 -> <TIMES>
 * f3 -> <LBRACE>
 * f4 -> Statements()
 * f5 -> <RBRACE>
 */
public class RepeatStatement implements Node {
    public NodeToken f0;
    public IntegerLiteral f1;
    public NodeToken f2;
    public NodeToken f3;
    public Statements f4;
    public NodeToken f5;

    public RepeatStatement(NodeToken n0, IntegerLiteral n1, NodeToken n2, NodeToken n3, Statements n4, NodeToken n5) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
    }

    public RepeatStatement(IntegerLiteral n0, Statements n1) {
        f0 = new NodeToken("repeat");
        f1 = n0;
        f2 = new NodeToken("times");
        f3 = new NodeToken("{");
        f4 = n1;
        f5 = new NodeToken("}");
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}

