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
 * f0 -> ( Module() )*
 * f1 -> ( Stationary() )*
 * f2 -> ( Manifest() )+
 * f3 -> <FUNCTIONS>
 * f4 -> ( FunctionDefinition() )*
 * f5 -> <INSTRUCTIONS>
 * f6 -> ( Statements() )+
 * f7 -> <EOF>
 */
public class BSProgram implements Node {
    public NodeListOptional f0;
    public NodeListOptional f1;
    public NodeList f2;
    public NodeToken f3;
    public NodeListOptional f4;
    public NodeToken f5;
    public NodeList f6;
    public NodeToken f7;

    public BSProgram(NodeListOptional n0, NodeListOptional n1, NodeList n2, NodeToken n3, NodeListOptional n4, NodeToken n5, NodeList n6, NodeToken n7) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
        f6 = n6;
        f7 = n7;
    }

    public BSProgram(NodeListOptional n0, NodeListOptional n1, NodeList n2, NodeListOptional n3, NodeList n4) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = new NodeToken("functions");
        f4 = n3;
        f5 = new NodeToken("instructions");
        f6 = n4;
        f7 = new NodeToken("");
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

