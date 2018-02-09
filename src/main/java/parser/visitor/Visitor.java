//
// Generated by JTB 1.3.2
//

package parser.visitor;
import parser.ast.*;
import java.util.*;

/**
 * All void visitors must implement this interface.
 */

public interface Visitor {

   //
   // void Auto class visitors
   //

   public void visit(NodeList n);
   public void visit(NodeListOptional n);
   public void visit(NodeOptional n);
   public void visit(NodeSequence n);
   public void visit(NodeToken n);

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> ( Module() )*
    * f1 -> ( Stationary() )*
    * f2 -> ( Manifest() )+
    * f3 -> <INSTRUCTIONS>
    * f4 -> ( Sequence() )+
    * f5 -> <EOF>
    */
   public void visit(BSProgram n);

   /**
    * f0 -> <MODULE>
    * f1 -> Identifier()
    */
   public void visit(Module n);

   /**
    * f0 -> <STATIONARY>
    * f1 -> ( TypingList() )?
    * f2 -> PrimaryExpression()
    */
   public void visit(Stationary n);

   /**
    * f0 -> <MANIFEST>
    * f1 -> ( TypingList() )?
    * f2 -> PrimaryExpression()
    */
   public void visit(Manifest n);

   /**
    * f0 -> Method()
    *       | Statement()
    */
   public void visit(Sequence n);

   /**
    * f0 -> AssignmentInstruction()
    *       | BranchStatement()
    *       | RepeatStatement()
    *       | HeatStatement()
    *       | DrainStatement()
    *       | FunctionInvoke()
    */
   public void visit(Statement n);

   /**
    * f0 -> ( TypingList() )*
    * f1 -> Identifier()
    * f2 -> <ASSIGN>
    * f3 -> Expression()
    */
   public void visit(AssignmentInstruction n);

   /**
    * f0 -> <FUNCTION>
    * f1 -> Identifier()
    * f2 -> <LPAREN>
    * f3 -> ( FormalParameterList() )*
    * f4 -> <RPAREN>
    * f5 -> ( <COLON> TypingList() )?
    * f6 -> <LBRACE>
    * f7 -> ( Statement() )*
    * f8 -> ( <RETURN> Expression() )?
    * f9 -> <RBRACE>
    */
   public void visit(Function n);

   /**
    * f0 -> Visibility()
    * f1 -> ( TypingRest() )*
    */
   public void visit(TypingList n);

   /**
    * f0 -> MatLiteral()
    *       | NatLiteral()
    *       | RealLiteral()
    */
   public void visit(Type n);

   /**
    * f0 -> <COMMA>
    * f1 -> Visibility()
    */
   public void visit(TypingRest n);

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public void visit(FormalParameterList n);

   /**
    * f0 -> ( TypingList() )*
    * f1 -> Identifier()
    */
   public void visit(FormalParameter n);

   /**
    * f0 -> <COMMA>
    * f1 -> FormalParameter()
    */
   public void visit(FormalParameterRest n);

   /**
    * f0 -> <MIX>
    * f1 -> PrimaryExpression()
    * f2 -> <WITH>
    * f3 -> PrimaryExpression()
    * f4 -> ( <FOR> IntegerLiteral() )?
    */
   public void visit(MixStatement n);

   /**
    * f0 -> <SPLIT>
    * f1 -> PrimaryExpression()
    * f2 -> <INTO>
    * f3 -> IntegerLiteral()
    */
   public void visit(SplitStatement n);

   /**
    * f0 -> <DRAIN>
    * f1 -> PrimaryExpression()
    */
   public void visit(DrainStatement n);

   /**
    * f0 -> <HEAT>
    * f1 -> PrimaryExpression()
    * f2 -> <AT>
    * f3 -> IntegerLiteral()
    * f4 -> ( <FOR> IntegerLiteral() )?
    */
   public void visit(HeatStatement n);

   /**
    * f0 -> <DETECT>
    * f1 -> PrimaryExpression()
    * f2 -> <ON>
    * f3 -> PrimaryExpression()
    * f4 -> ( <FOR> IntegerLiteral() )?
    */
   public void visit(DetectStatement n);

   /**
    * f0 -> <REPEAT>
    * f1 -> IntegerLiteral()
    * f2 -> <TIMES>
    * f3 -> <LBRACE>
    * f4 -> Statement()
    * f5 -> <RBRACE>
    */
   public void visit(RepeatStatement n);

   /**
    * f0 -> <IF> <LPAREN> Expression() <RPAREN> <LBRACE> Statement() <RBRACE>
    *       | <ELSE_IF> <LPAREN> Expression() <RPAREN> <LBRACE> Statement() <RBRACE>
    *       | <ELSE> <LBRACE> Statement() <RBRACE>
    */
   public void visit(BranchStatement n);

   /**
    * f0 -> AndExpression()
    *       | LessThanExpression()
    *       | LessThanEqualExpression()
    *       | GreaterThanExpression()
    *       | GreaterThanEqualExpression()
    *       | NotEqualExpression()
    *       | EqualityExpression()
    *       | OrExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | FunctionInvoke()
    *       | PrimaryExpression()
    *       | InstructionAssignment()
    */
   public void visit(Expression n);

   /**
    * f0 -> Identifier()
    * f1 -> <LPAREN>
    * f2 -> ( ExpressionList() )?
    * f3 -> <RPAREN>
    */
   public void visit(FunctionInvoke n);

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n);

   /**
    * f0 -> <COMMA>
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n);

   /**
    * f0 -> MixStatement()
    *       | DetectStatement()
    *       | SplitStatement()
    *       | FunctionInvoke()
    */
   public void visit(InstructionAssignment n);

   /**
    * f0 -> Identifier()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | ParenthesisExpression()
    *       | IntegerLiteral()
    */
   public void visit(PrimaryExpression n);

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public void visit(IntegerLiteral n);

   /**
    * f0 -> <NAT>
    */
   public void visit(NatLiteral n);

   /**
    * f0 -> <MAT>
    */
   public void visit(MatLiteral n);

   /**
    * f0 -> <REAL>
    */
   public void visit(RealLiteral n);

   /**
    * f0 -> <TRUE>
    */
   public void visit(TrueLiteral n);

   /**
    * f0 -> <FALSE>
    */
   public void visit(FalseLiteral n);

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <AND>
    * f2 -> PrimaryExpression()
    */
   public void visit(AndExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <LESSTHAN>
    * f2 -> PrimaryExpression()
    */
   public void visit(LessThanExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <LESSTHANEQUAL>
    * f2 -> PrimaryExpression()
    */
   public void visit(LessThanEqualExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <GREATERTHAN>
    * f2 -> PrimaryExpression()
    */
   public void visit(GreaterThanExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <GREATERTHANEQUAL>
    * f2 -> PrimaryExpression()
    */
   public void visit(GreaterThanEqualExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <NOTEQUAL>
    * f2 -> PrimaryExpression()
    */
   public void visit(NotEqualExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <OR>
    * f2 -> PrimaryExpression()
    */
   public void visit(EqualityExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <LESSTHAN>
    * f2 -> PrimaryExpression()
    */
   public void visit(OrExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <ADD>
    * f2 -> PrimaryExpression()
    */
   public void visit(PlusExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <MINUS>
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n);

   /**
    * f0 -> PrimaryExpression()
    * f1 -> <MULTIPLY>
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n);

   /**
    * f0 -> <BANG>
    * f1 -> Expression()
    */
   public void visit(NotExpression n);

   /**
    * f0 -> <LPAREN>
    * f1 -> Expression()
    * f2 -> <RPAREN>
    */
   public void visit(ParenthesisExpression n);

}

