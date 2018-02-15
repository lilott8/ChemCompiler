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
 * f0 -> <SPLIT>
 * f1 -> PrimaryExpression()
 * f2 -> <INTO>
 * f3 -> IntegerLiteral()
 */
public class SplitInstruction implements Node {
    public NodeToken f0;
    public PrimaryExpression f1;
    public NodeToken f2;
    public IntegerLiteral f3;

    public SplitInstruction(NodeToken n0, PrimaryExpression n1, NodeToken n2, IntegerLiteral n3) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
    }

    public SplitInstruction(PrimaryExpression n0, IntegerLiteral n1) {
        f0 = new NodeToken("split");
        f1 = n0;
        f2 = new NodeToken("into");
        f3 = n1;
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

