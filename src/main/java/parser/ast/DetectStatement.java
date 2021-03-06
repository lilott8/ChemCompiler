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
 * f0 -> <DETECT>
 * f1 -> PrimaryExpression()
 * f2 -> <ON>
 * f3 -> PrimaryExpression()
 * f4 -> ( <FOR> TimeUnit() )?
 */
public class DetectStatement implements Node {
    public NodeToken f0;
    public PrimaryExpression f1;
    public NodeToken f2;
    public PrimaryExpression f3;
    public NodeOptional f4;

    public DetectStatement(NodeToken n0, PrimaryExpression n1, NodeToken n2, PrimaryExpression n3, NodeOptional n4) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
    }

    public DetectStatement(PrimaryExpression n0, PrimaryExpression n1, NodeOptional n2) {
        f0 = new NodeToken("detect");
        f1 = n0;
        f2 = new NodeToken("on");
        f3 = n1;
        f4 = n2;
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

