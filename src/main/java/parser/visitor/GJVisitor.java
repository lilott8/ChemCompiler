//
// Generated by JTB 1.3.2
//

package parser.visitor;

import parser.ast.*;

import java.util.*;

/**
 * All GJ visitors must implement this interface.
 */

public interface GJVisitor<R, A> {

    //
    // GJ Auto class visitors
    //

    R visit(NodeList n, A argu);

    R visit(NodeListOptional n, A argu);

    R visit(NodeOptional n, A argu);

    R visit(NodeSequence n, A argu);

    R visit(NodeToken n, A argu);

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> ( Module() )*
     * f1 -> ( Stationary() )*
     * f2 -> ( Manifest() )+
     * f3 -> <FUNCTIONS>
     * f4 -> ( FunctionDefinition() )*
     * f5 -> <INSTRUCTIONS>
     * f6 -> ( Statements() )+
     * f7 -> <EOF>
     */
    R visit(BSProgram n, A argu);

    /**
     * f0 -> <MODULE>
     * f1 -> Identifier()
     */
    R visit(Module n, A argu);

    /**
     * f0 -> <STATIONARY>
     * f1 -> ( TypingList() )?
     * f2 -> PrimaryExpression()
     */
    R visit(Stationary n, A argu);

    /**
     * f0 -> <MANIFEST>
     * f1 -> ( TypingList() )?
     * f2 -> PrimaryExpression()
     */
    R visit(Manifest n, A argu);

    /**
     * f0 -> <FUNCTION>
     * f1 -> Identifier()
     * f2 -> <LPAREN>
     * f3 -> ( FormalParameterList() )*
     * f4 -> <RPAREN>
     * f5 -> ( <COLON> TypingList() )?
     * f6 -> <LBRACE>
     * f7 -> ( Statements() )+
     * f8 -> ( <RETURN> Identifier() )?
     * f9 -> <RBRACE>
     */
    R visit(FunctionDefinition n, A argu);

    /**
     * f0 -> AssignmentStatement()
     * | BranchStatement()
     * | RepeatStatement()
     * | WhileStatement()
     * | HeatStatement()
     * | DrainStatement()
     * | FunctionInvoke()
     */
    R visit(Statements n, A argu);

    /**
     * f0 -> ( TypingList() )*
     * f1 -> Identifier()
     * f2 -> <ASSIGN>
     * f3 -> RightOp()
     */
    R visit(AssignmentStatement n, A argu);

    /**
     * f0 -> MixStatement()
     * | DetectStatement()
     * | SplitStatement()
     * | DispenseStatement()
     * | FunctionInvoke()
     * | VariableAlias()
     */
    R visit(RightOp n, A argu);

    /**
     * f0 -> Type()
     * f1 -> ( TypingRest() )*
     */
    R visit(TypingList n, A argu);

    /**
     * f0 -> MatLiteral()
     * | NatLiteral()
     * | RealLiteral()
     */
    R visit(Type n, A argu);

    /**
     * f0 -> <COMMA>
     * f1 -> Type()
     */
    R visit(TypingRest n, A argu);

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    R visit(FormalParameterList n, A argu);

    /**
     * f0 -> ( TypingList() )*
     * f1 -> Identifier()
     */
    R visit(FormalParameter n, A argu);

    /**
     * f0 -> <COMMA>
     * f1 -> FormalParameter()
     */
    R visit(FormalParameterRest n, A argu);

    /**
     * f0 -> <MIX>
     * f1 -> ( VolumeUnit() <OF> )?
     * f2 -> PrimaryExpression()
     * f3 -> <WITH>
     * f4 -> ( VolumeUnit() <OF> )?
     * f5 -> PrimaryExpression()
     * f6 -> ( <FOR> TimeUnit() )?
     */
    R visit(MixStatement n, A argu);

    /**
     * f0 -> <SPLIT>
     * f1 -> PrimaryExpression()
     * f2 -> <INTO>
     * f3 -> IntegerLiteral()
     */
    R visit(SplitStatement n, A argu);

    /**
     * f0 -> <DRAIN>
     * f1 -> PrimaryExpression()
     */
    R visit(DrainStatement n, A argu);

    /**
     * f0 -> <DISPENSE>
     * f1 -> ( VolumeUnit() <OF> )?
     * f2 -> Identifier()
     */
    R visit(DispenseStatement n, A argu);

    /**
     * f0 -> <HEAT>
     * f1 -> PrimaryExpression()
     * f2 -> <AT>
     * f3 -> TempUnit()
     * f4 -> ( <FOR> TimeUnit() )?
     */
    R visit(HeatStatement n, A argu);

    /**
     * f0 -> <DETECT>
     * f1 -> PrimaryExpression()
     * f2 -> <ON>
     * f3 -> PrimaryExpression()
     * f4 -> ( <FOR> TimeUnit() )?
     */
    R visit(DetectStatement n, A argu);

    /**
     * f0 -> <REPEAT>
     * f1 -> IntegerLiteral()
     * f2 -> <TIMES>
     * f3 -> <LBRACE>
     * f4 -> ( Statements() )+
     * f5 -> <RBRACE>
     */
    R visit(RepeatStatement n, A argu);

    /**
     * f0 -> <WHILE>
     * f1 -> <LPAREN>
     * f2 -> Conditional()
     * f3 -> <RPAREN>
     * f4 -> <LBRACE>
     * f5 -> ( Statements() )+
     * f6 -> <RBRACE>
     */
    R visit(WhileStatement n, A argu);

    /**
     * f0 -> <IF>
     * f1 -> <LPAREN>
     * f2 -> Identifier()
     * f3 -> Conditional()
     * f4 -> Primitives()
     * f5 -> <RPAREN>
     * f6 -> <LBRACE>
     * f7 -> ( Statements() )+
     * f8 -> <RBRACE>
     * f9 -> ( ElseIfBranchStatement() )*
     * f10 -> ( ElseBranchStatement() )?
     */
    R visit(BranchStatement n, A argu);

    /**
     * f0 -> <ELSE_IF>
     * f1 -> <LPAREN>
     * f2 -> Conditional()
     * f3 -> <RPAREN>
     * f4 -> <LBRACE>
     * f5 -> ( Statements() )+
     * f6 -> <RBRACE>
     */
    R visit(ElseIfBranchStatement n, A argu);

    /**
     * f0 -> <ELSE>
     * f1 -> <LBRACE>
     * f2 -> ( Statements() )+
     * f3 -> <RBRACE>
     */
    R visit(ElseBranchStatement n, A argu);

    /**
     * f0 -> <LESSTHAN>
     * | <LESSTHANEQUAL>
     * | <NOTEQUAL>
     * | <EQUALITY>
     * | <GREATERTHAN>
     * | <GREATERTHANEQUAL>
     */
    R visit(Conditional n, A argu);

    /**
     * f0 -> ConditionalParenthesis()
     * | AndExpression()
     * | LessThanExpression()
     * | LessThanEqualExpression()
     * | GreaterThanExpression()
     * | GreaterThanEqualExpression()
     * | NotEqualExpression()
     * | EqualityExpression()
     * | OrExpression()
     */
    R visit(TraditionalConditional n, A argu);

    /**
     * f0 -> MathParenthesis()
     * | PlusExpression()
     * | MinusExpression()
     * | TimesExpression()
     * | DivideExpression()
     */
    R visit(MathStatement n, A argu);

    /**
     * f0 -> Identifier()
     * f1 -> <LPAREN>
     * f2 -> ( ArgumentList() )?
     * f3 -> <RPAREN>
     */
    R visit(FunctionInvoke n, A argu);

    /**
     * f0 -> AllowedArguments()
     * f1 -> ( AllowedArgumentsRest() )*
     */
    R visit(ArgumentList n, A argu);

    /**
     * f0 -> <COMMA>
     * f1 -> AllowedArguments()
     */
    R visit(AllowedArgumentsRest n, A argu);

    /**
     * f0 -> Identifier()
     * | TrueLiteral()
     * | FalseLiteral()
     * | IntegerLiteral()
     * | RealLiteral()
     */
    R visit(PrimaryExpression n, A argu);

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    R visit(IntegerLiteral n, A argu);

    /**
     * f0 -> <NAT>
     */
    R visit(NatLiteral n, A argu);

    /**
     * f0 -> <MAT>
     */
    R visit(MatLiteral n, A argu);

    /**
     * f0 -> <REAL>
     */
    R visit(RealLiteral n, A argu);

    /**
     * f0 -> <TRUE>
     */
    R visit(TrueLiteral n, A argu);

    /**
     * f0 -> <FALSE>
     */
    R visit(FalseLiteral n, A argu);

    /**
     * f0 -> <IDENTIFIER>
     */
    R visit(Identifier n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <AND>
     * f2 -> PrimaryExpression()
     */
    R visit(AndExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHAN>
     * f2 -> PrimaryExpression()
     */
    R visit(LessThanExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHANEQUAL>
     * f2 -> PrimaryExpression()
     */
    R visit(LessThanEqualExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <GREATERTHAN>
     * f2 -> PrimaryExpression()
     */
    R visit(GreaterThanExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <GREATERTHANEQUAL>
     * f2 -> PrimaryExpression()
     */
    R visit(GreaterThanEqualExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <NOTEQUAL>
     * f2 -> PrimaryExpression()
     */
    R visit(NotEqualExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <OR>
     * f2 -> PrimaryExpression()
     */
    R visit(EqualityExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHAN>
     * f2 -> PrimaryExpression()
     */
    R visit(OrExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <ADD>
     * f2 -> PrimaryExpression()
     */
    R visit(PlusExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <MINUS>
     * f2 -> PrimaryExpression()
     */
    R visit(MinusExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <MULTIPLY>
     * f2 -> PrimaryExpression()
     */
    R visit(TimesExpression n, A argu);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <DIVIDE>
     * f2 -> PrimaryExpression()
     */
    R visit(DivideExpression n, A argu);

    /**
     * f0 -> <BANG>
     * f1 -> PrimaryExpression()
     */
    R visit(NotExpression n, A argu);

    /**
     * f0 -> <LPAREN>
     * f1 -> Conditional()
     * f2 -> <RPAREN>
     */
    R visit(ConditionalParenthesis n, A argu);

    /**
     * f0 -> <LPAREN>
     * f1 -> MathStatement()
     * f2 -> <RPAREN>
     */
    R visit(MathParenthesis n, A argu);

    /**
     * f0 -> Identifier()
     * | Primitives()
     */
    R visit(AllowedArguments n, A argu);

    /**
     * f0 -> IntegerLiteral()
     * | RealLiteral()
     * | TrueLiteral()
     * | FalseLiteral()
     */
    R visit(Primitives n, A argu);

    /**
     * f0 -> Identifier()
     */
    R visit(VariableAlias n, A argu);

    /**
     * f0 -> IntegerLiteral()
     * f1 -> ( <SECOND> | <MILLISECOND> | <MICROSECOND> | <HOUR> | <MINUTE> )?
     */
    R visit(TimeUnit n, A argu);

    /**
     * f0 -> IntegerLiteral()
     * f1 -> ( <LITRE> | <MILLILITRE> | <MICROLITRE> )?
     */
    R visit(VolumeUnit n, A argu);

    /**
     * f0 -> IntegerLiteral()
     * f1 -> ( <CELSIUS> | <FAHRENHEIT> )?
     */
    R visit(TempUnit n, A argu);

}
