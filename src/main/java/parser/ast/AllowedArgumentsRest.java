//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.*;

/**
 * Grammar production:
 * f0 -> <COMMA>
 * f1 -> AllowedArguments()
 */
public class AllowedArgumentsRest implements Node {
   public NodeToken f0;
   public AllowedArguments f1;

   public AllowedArgumentsRest(NodeToken n0, AllowedArguments n1) {
      f0 = n0;
      f1 = n1;
   }

   public AllowedArgumentsRest(AllowedArguments n0) {
      f0 = new NodeToken(",");
      f1 = n0;
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

