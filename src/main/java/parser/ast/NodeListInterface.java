//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.GJNoArguVisitor;
import parser.visitor.GJVisitor;
import parser.visitor.GJVoidVisitor;
import parser.visitor.Visitor;

/**
 * The interface which NodeList, NodeListOptional, and NodeSequence
 * implement.
 */
public interface NodeListInterface extends Node {
    void addNode(Node n);

    Node elementAt(int i);

    java.util.Enumeration<Node> elements();

    int size();

    void accept(Visitor v);

    <R, A> R accept(GJVisitor<R, A> v, A argu);

    <R> R accept(GJNoArguVisitor<R> v);

    <A> void accept(GJVoidVisitor<A> v, A argu);
}

