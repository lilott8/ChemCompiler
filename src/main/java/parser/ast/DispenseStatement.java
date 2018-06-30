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
 * f0 -> <DISPENSE>
 * f1 -> ( VolumeUnit() <OF> )?
 * f2 -> Identifier()
 */
public class DispenseStatement implements Node {
    public NodeToken f0;
    public NodeOptional f1;
    public Identifier f2;

    public DispenseStatement(NodeToken n0, NodeOptional n1, Identifier n2) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
    }

    public DispenseStatement(NodeOptional n0, Identifier n1) {
        f0 = new NodeToken("dispense");
        f1 = n0;
        f2 = n1;
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

