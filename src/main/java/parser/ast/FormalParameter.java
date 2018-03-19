//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.*;

/**
 * Grammar production:
 * f0 -> ( TypingList() )*
 * f1 -> Identifier()
 */
public class FormalParameter implements Node {
   public NodeListOptional f0;
   public Identifier f1;

   public FormalParameter(NodeListOptional n0, Identifier n1) {
      f0 = n0;
      f1 = n1;
   }

   public void accept(Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

