package parser.ast;

import parser.visitor.GJNoArguVisitor;
import parser.visitor.GJVisitor;
import parser.visitor.GJVoidVisitor;
import parser.visitor.Visitor;

/**
 * Grammar production:
 * f0 -> ConditionalParenthesis()
 * | AndExpression()
 * | LessThanExpression()
 * | LessThanEqualExpression()
 * | GreaterThanExpression()
 * | GreaterThanEqualExpression()
 * | NotEqualExpression()
 * | EqualityExpression()
 * | OrExpression()
 */
public class TraditionalConditional implements Node {
    public NodeChoice f0;

    public TraditionalConditional(NodeChoice n0) {
        f0 = n0;
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
