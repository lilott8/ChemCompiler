//
// Generated by JTB 1.3.2
//

package parser.ast;

import parser.visitor.*;

/**
 * Grammar production:
 * f0 -> <FUNCTION>
 * f1 -> Identifier()
 * f2 -> <LPAREN>
 * f3 -> ( FormalParameterList() )*
 * f4 -> <RPAREN>
 * f5 -> ( <COLON> TypingList() )?
 * f6 -> <LBRACE>
 * f7 -> ( Statements() )*
 * f8 -> ( <RETURN> Expression() )?
 * f9 -> <RBRACE>
 */
public class FunctionDefinition implements Node {
   public NodeToken f0;
   public Identifier f1;
   public NodeToken f2;
   public NodeListOptional f3;
   public NodeToken f4;
   public NodeOptional f5;
   public NodeToken f6;
   public NodeListOptional f7;
   public NodeOptional f8;
   public NodeToken f9;

   public FunctionDefinition(NodeToken n0, Identifier n1, NodeToken n2, NodeListOptional n3, NodeToken n4, NodeOptional n5, NodeToken n6, NodeListOptional n7, NodeOptional n8, NodeToken n9) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
      f5 = n5;
      f6 = n6;
      f7 = n7;
      f8 = n8;
      f9 = n9;
   }

   public FunctionDefinition(Identifier n0, NodeListOptional n1, NodeOptional n2, NodeListOptional n3, NodeOptional n4) {
      f0 = new NodeToken("function");
      f1 = n0;
      f2 = new NodeToken("(");
      f3 = n1;
      f4 = new NodeToken(")");
      f5 = n2;
      f6 = new NodeToken("{");
      f7 = n3;
      f8 = n4;
      f9 = new NodeToken("}");
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

