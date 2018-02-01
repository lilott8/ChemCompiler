//
// Generated by JTB 1.3.2
//

package parser.visitor;

import parser.ast.AndExpression;
import parser.ast.AssignmentStatement;
import parser.ast.BSProgram;
import parser.ast.BranchStatement;
import parser.ast.DetectStatement;
import parser.ast.DrainStatement;
import parser.ast.EqualityExpression;
import parser.ast.Expression;
import parser.ast.FalseLiteral;
import parser.ast.FormalParameter;
import parser.ast.FormalParameterList;
import parser.ast.FormalParameterRest;
import parser.ast.Function;
import parser.ast.GreaterThanEqualExpression;
import parser.ast.GreaterThanExpression;
import parser.ast.HeatStatement;
import parser.ast.Identifier;
import parser.ast.Instruction;
import parser.ast.IntegerLiteral;
import parser.ast.LessThanEqualExpression;
import parser.ast.LessThanExpression;
import parser.ast.Manifest;
import parser.ast.MatLiteral;
import parser.ast.MinusExpression;
import parser.ast.MixStatement;
import parser.ast.NatLiteral;
import parser.ast.NodeList;
import parser.ast.NodeListOptional;
import parser.ast.NodeOptional;
import parser.ast.NodeSequence;
import parser.ast.NodeToken;
import parser.ast.NotEqualExpression;
import parser.ast.NotExpression;
import parser.ast.OrExpression;
import parser.ast.ParenthesisExpression;
import parser.ast.PlusExpression;
import parser.ast.PrimaryExpression;
import parser.ast.RealLiteral;
import parser.ast.RepeatStatement;
import parser.ast.SplitStatement;
import parser.ast.Statement;
import parser.ast.Stationary;
import parser.ast.TimesExpression;
import parser.ast.TrueLiteral;
import parser.ast.Type;
import parser.ast.TypingList;
import parser.ast.TypingRest;
import parser.ast.WhileStatement;

/**
 * All GJ visitors with no argument must implement this interface.
 */

public interface GJNoArguVisitor<R> {

    //
    // GJ Auto class visitors with no argument
    //

    R visit(NodeList n);

    R visit(NodeListOptional n);

    R visit(NodeOptional n);

    R visit(NodeSequence n);

    R visit(NodeToken n);

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> Stationary()
     * f1 -> Manifest()
     * f2 -> <INSTRUCTIONS>
     * f3 -> Instruction()
     * f4 -> <EOF>
     */
    R visit(BSProgram n);

    /**
     * f0 -> ( <STATIONARY> Identifier() )*
     */
    R visit(Stationary n);

    /**
     * f0 -> ( <MANIFEST> Identifier() )*
     */
    R visit(Manifest n);

    /**
     * f0 -> Instruction()
     * | BranchStatement()
     * | WhileStatement()
     */
    R visit(Statement n);

    /**
     * f0 -> MixStatement()
     * | SplitStatement()
     * | DrainStatement()
     * | HeatStatement()
     * | DetectStatement()
     * | RepeatStatement()
     * | AssignmentStatement()
     */
    R visit(Instruction n);

    /**
     * f0 -> <FUNCTION>
     * f1 -> Identifier()
     * f2 -> <LPAREN>
     * f3 -> FormalParameterList()
     * f4 -> <RPAREN>
     * f5 -> ( <COLON> ( TypingList() )* )?
     * f6 -> <LBRACE>
     * f7 -> Statement()
     * f8 -> <LBRACE>
     */
    R visit(Function n);

    /**
     * f0 -> Type()
     * f1 -> ( TypingRest() )*
     */
    R visit(TypingList n);

    /**
     * f0 -> <COMMA>
     * f1 -> Type()
     */
    R visit(TypingRest n);

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    R visit(FormalParameterList n);

    /**
     * f0 -> TypingList()
     * f1 -> Identifier()
     */
    R visit(FormalParameter n);

    /**
     * f0 -> <COMMA>
     * f1 -> FormalParameter()
     */
    R visit(FormalParameterRest n);

    /**
     * f0 -> <MIX> PrimaryExpression() <WITH> PrimaryExpression()
     * | <FOR> IntegerLiteral()
     */
    R visit(MixStatement n);

    /**
     * f0 -> <SPLIT>
     * f1 -> PrimaryExpression()
     * f2 -> <INTO>
     * f3 -> PrimaryExpression()
     */
    R visit(SplitStatement n);

    /**
     * f0 -> <DRAIN>
     * f1 -> PrimaryExpression()
     */
    R visit(DrainStatement n);

    /**
     * f0 -> <HEAT> PrimaryExpression() <AT> IntegerLiteral()
     * | <FOR> IntegerLiteral()
     */
    R visit(HeatStatement n);

    /**
     * f0 -> <DETECT> Identifier() <ON> PrimaryExpression()
     * | <FOR> <INTEGER_LITERAL>
     */
    R visit(DetectStatement n);

    /**
     * f0 -> WhileStatement()
     */
    R visit(RepeatStatement n);

    /**
     * f0 -> ( TypingList() )*
     * f1 -> Identifier()
     * f2 -> <ASSIGN>
     * f3 -> Expression()
     */
    R visit(AssignmentStatement n);

    /**
     * f0 -> MatLiteral()
     * | NatLiteral()
     * | RealLiteral()
     */
    R visit(Type n);

    /**
     * f0 -> <IF> <LPAREN> Expression() <RPAREN> <LBRACE> Statement() <RBRACE>
     * | <ELSE_IF> <LPAREN> Expression() <RPAREN> <LBRACE> Statement() <RBRACE>
     * | <ELSE> <LBRACE> Statement() <RBRACE>
     */
    R visit(BranchStatement n);

    /**
     * f0 -> <REPEAT>
     * f1 -> IntegerLiteral()
     * f2 -> <TIMES>
     * f3 -> <LBRACE>
     * f4 -> Statement()
     * f5 -> <RBRACE>
     */
    R visit(WhileStatement n);

    /**
     * f0 -> IntegerLiteral()
     * | TrueLiteral()
     * | FalseLiteral()
     * | Identifier()
     * | ParenthesisExpression()
     */
    R visit(PrimaryExpression n);

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    R visit(IntegerLiteral n);

    /**
     * f0 -> <NAT>
     */
    R visit(NatLiteral n);

    /**
     * f0 -> <MAT>
     */
    R visit(MatLiteral n);

    /**
     * f0 -> <REAL>
     */
    R visit(RealLiteral n);

    /**
     * f0 -> <TRUE>
     */
    R visit(TrueLiteral n);

    /**
     * f0 -> <FALSE>
     */
    R visit(FalseLiteral n);

    /**
     * f0 -> <IDENTIFIER>
     */
    R visit(Identifier n);

    /**
     * f0 -> AndExpression()
     * | LessThanExpression()
     * | LessThanEqualExpression()
     * | GreaterThanExpression()
     * | GreaterThanEqualExpression()
     * | NotEqualExpression()
     * | EqualityExpression()
     * | OrExpression()
     * | PlusExpression()
     * | MinusExpression()
     * | TimesExpression()
     * | PrimaryExpression()
     */
    R visit(Expression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <AND>
     * f2 -> PrimaryExpression()
     */
    R visit(AndExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHAN>
     * f2 -> PrimaryExpression()
     */
    R visit(LessThanExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHANEQUAL>
     * f2 -> PrimaryExpression()
     */
    R visit(LessThanEqualExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <GREATERTHAN>
     * f2 -> PrimaryExpression()
     */
    R visit(GreaterThanExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <GREATERTHANEQUAL>
     * f2 -> PrimaryExpression()
     */
    R visit(GreaterThanEqualExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <NOTEQUAL>
     * f2 -> PrimaryExpression()
     */
    R visit(NotEqualExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <OR>
     * f2 -> PrimaryExpression()
     */
    R visit(EqualityExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHAN>
     * f2 -> PrimaryExpression()
     */
    R visit(OrExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <ADD>
     * f2 -> PrimaryExpression()
     */
    R visit(PlusExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <MINUS>
     * f2 -> PrimaryExpression()
     */
    R visit(MinusExpression n);

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <MULTIPLY>
     * f2 -> PrimaryExpression()
     */
    R visit(TimesExpression n);

    /**
     * f0 -> <BANG>
     * f1 -> Expression()
     */
    R visit(NotExpression n);

    /**
     * f0 -> <LPAREN>
     * f1 -> Expression()
     * f2 -> <RPAREN>
     */
    R visit(ParenthesisExpression n);

}

