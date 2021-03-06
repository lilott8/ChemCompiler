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
 * f0 -> ( TypingList() )*
 * f1 -> Identifier()
 * f2 -> <ASSIGN>
 * f3 -> RightOp()
 */
public class AssignmentStatement implements Node {
    public NodeListOptional f0;
    public Identifier f1;
    public NodeToken f2;
    public RightOp f3;

    public AssignmentStatement(NodeListOptional n0, Identifier n1, NodeToken n2, RightOp n3) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
    }

    public AssignmentStatement(NodeListOptional n0, Identifier n1, RightOp n2) {
        f0 = n0;
        f1 = n1;
        f2 = new NodeToken("=");
        f3 = n2;
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

