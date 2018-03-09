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
 * f0 -> Expression()
 * f1 -> ( ExpressionRest() )*
 */
public class ExpressionList implements Node {
    public Expression f0;
    public NodeListOptional f1;

    public ExpressionList(Expression n0, NodeListOptional n1) {
        f0 = n0;
        f1 = n1;
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

