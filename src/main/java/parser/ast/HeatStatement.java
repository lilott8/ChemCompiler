//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.*;

/**
 * Grammar production:
 * f0 -> <HEAT>
 * f1 -> PrimaryExpression()
 * f2 -> <AT>
 * f3 -> IntegerLiteral()
 * f4 -> ( <FOR> IntegerLiteral() )?
 */
public class HeatStatement implements Node {
   public NodeToken f0;
   public PrimaryExpression f1;
   public NodeToken f2;
   public IntegerLiteral f3;
   public NodeOptional f4;

   public HeatStatement(NodeToken n0, PrimaryExpression n1, NodeToken n2, IntegerLiteral n3, NodeOptional n4) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
   }

   public HeatStatement(PrimaryExpression n0, IntegerLiteral n1, NodeOptional n2) {
      f0 = new NodeToken("heat");
      f1 = n0;
      f2 = new NodeToken("at");
      f3 = n1;
      f4 = n2;
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

