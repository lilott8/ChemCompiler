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
 * f0 -> IntegerLiteral()
 * f1 -> ( <LITRE> | <MILLILITRE> | <MICROLITRE> )?
 */
public class VolumeUnit implements Node {
    public IntegerLiteral f0;
    public NodeOptional f1;

    public VolumeUnit(IntegerLiteral n0, NodeOptional n1) {
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
