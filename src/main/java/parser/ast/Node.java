//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.*;

/**
 * The interface which all syntax tree classes must implement.
 */
public interface Node extends java.io.Serializable {
   public void accept(Visitor v);
   public <R,A> R accept(GJVisitor<R,A> v, A argu);
   public <R> R accept(GJNoArguVisitor<R> v);
   public <A> void accept(GJVoidVisitor<A> v, A argu);
}

