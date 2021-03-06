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
 * f0 -> <MODULE>
 * f1 -> Identifier()
 */
public class Module implements Node {
    public NodeToken f0;
    public Identifier f1;

    public Module(NodeToken n0, Identifier n1) {
        f0 = n0;
        f1 = n1;
    }

    public Module(Identifier n0) {
        f0 = new NodeToken("module");
        f1 = n0;
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

