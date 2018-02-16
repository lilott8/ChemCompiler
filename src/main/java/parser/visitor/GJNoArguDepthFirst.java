//
// Generated by JTB 1.3.2
//

package parser.visitor;

import java.util.Enumeration;

import parser.ast.AndExpression;
import parser.ast.Assignment;
import parser.ast.BSProgram;
import parser.ast.BranchInstruction;
import parser.ast.DetectInstruction;
import parser.ast.DrainInstruction;
import parser.ast.ElseIfStatement;
import parser.ast.ElseStatement;
import parser.ast.EqualityExpression;
import parser.ast.Expression;
import parser.ast.ExpressionList;
import parser.ast.ExpressionRest;
import parser.ast.FalseLiteral;
import parser.ast.FormalParameter;
import parser.ast.FormalParameterList;
import parser.ast.FormalParameterRest;
import parser.ast.FunctionDefinition;
import parser.ast.FunctionInvoke;
import parser.ast.GreaterThanEqualExpression;
import parser.ast.GreaterThanExpression;
import parser.ast.HeatInstruction;
import parser.ast.Identifier;
import parser.ast.IfStatement;
import parser.ast.IntegerLiteral;
import parser.ast.LessThanEqualExpression;
import parser.ast.LessThanExpression;
import parser.ast.Manifest;
import parser.ast.MatLiteral;
import parser.ast.MinusExpression;
import parser.ast.MixInstruction;
import parser.ast.Module;
import parser.ast.NatLiteral;
import parser.ast.Node;
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
import parser.ast.RepeatInstruction;
import parser.ast.Sequence;
import parser.ast.SplitInstruction;
import parser.ast.Statement;
import parser.ast.Stationary;
import parser.ast.TimesExpression;
import parser.ast.TrueLiteral;
import parser.ast.Type;
import parser.ast.TypingList;
import parser.ast.TypingRest;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class GJNoArguDepthFirst<R> implements GJNoArguVisitor<R> {
    //
    // Auto class visitors--probably don't need to be overridden.
    //
    public R visit(NodeList n) {
        R _ret = null;
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
        }
        return _ret;
    }

    public R visit(NodeListOptional n) {
        if (n.present()) {
            R _ret = null;
            int _count = 0;
            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                e.nextElement().accept(this);
                _count++;
            }
            return _ret;
        } else
            return null;
    }

    public R visit(NodeOptional n) {
        if (n.present())
            return n.node.accept(this);
        else
            return null;
    }

    public R visit(NodeSequence n) {
        R _ret = null;
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
        }
        return _ret;
    }

    public R visit(NodeToken n) {
        return null;
    }

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
    public R visit(BSProgram n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        return _ret;
    }

    /**
     * f0 -> <MODULE>
     * f1 -> Identifier()
     */
    public R visit(Module n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> <STATIONARY>
     * f1 -> ( TypingList() )?
     * f2 -> PrimaryExpression()
     */
    public R visit(Stationary n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> <MANIFEST>
     * f1 -> ( TypingList() )?
     * f2 -> PrimaryExpression()
     */
    public R visit(Manifest n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> FunctionDefinition()
     * | Statement()
     */
    public R visit(Sequence n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <FUNCTION>
     * f1 -> Identifier()
     * f2 -> <LPAREN>
     * f3 -> ( FormalParameterList() )*
     * f4 -> <RPAREN>
     * f5 -> ( <COLON> TypingList() )?
     * f6 -> <LBRACE>
     * f7 -> ( Statement() )+
     * f8 -> ( <RETURN> Expression() )?
     * f9 -> <RBRACE>
     */
    public R visit(FunctionDefinition n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        n.f6.accept(this);
        n.f7.accept(this);
        n.f8.accept(this);
        n.f9.accept(this);
        return _ret;
    }

    /**
     * f0 -> Assignment()
     * | BranchInstruction()
     * | RepeatInstruction()
     * | HeatInstruction()
     * | DrainInstruction()
     * | Expression()
     */
    public R visit(Statement n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> ( TypingRest() )*
     */
    public R visit(TypingList n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> MatLiteral()
     * | NatLiteral()
     * | RealLiteral()
     */
    public R visit(Type n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <COMMA>
     * f1 -> Type()
     */
    public R visit(TypingRest n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public R visit(FormalParameterList n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> ( TypingList() )*
     * f1 -> Identifier()
     */
    public R visit(FormalParameter n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> <COMMA>
     * f1 -> FormalParameter()
     */
    public R visit(FormalParameterRest n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> IfStatement()
     * f1 -> ( ElseIfStatement() )*
     * f2 -> ( ElseStatement() )?
     */
    public R visit(BranchInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> <IF>
     * f1 -> <LPAREN>
     * f2 -> Expression()
     * f3 -> <RPAREN>
     * f4 -> <LBRACE>
     * f5 -> Statement()
     * f6 -> <RBRACE>
     */
    public R visit(IfStatement n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        n.f6.accept(this);
        return _ret;
    }

    /**
     * f0 -> <ELSE_IF>
     * f1 -> <LPAREN>
     * f2 -> Expression()
     * f3 -> <RPAREN>
     * f4 -> <LBRACE>
     * f5 -> Statement()
     * f6 -> <RBRACE>
     */
    public R visit(ElseIfStatement n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        n.f6.accept(this);
        return _ret;
    }

    /**
     * f0 -> <ELSE>
     * f1 -> <LBRACE>
     * f2 -> Statement()
     * f3 -> <RBRACE>
     */
    public R visit(ElseStatement n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public R visit(ExpressionList n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> <COMMA>
     * f1 -> Expression()
     */
    public R visit(ExpressionRest n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> ( TypingList() )?
     * f1 -> Identifier()
     * f2 -> <ASSIGN>
     * f3 -> Expression()
     */
    public R visit(Assignment n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        return _ret;
    }

    /**
     * f0 -> <MIX>
     * f1 -> PrimaryExpression()
     * f2 -> <WITH>
     * f3 -> PrimaryExpression()
     * f4 -> ( <FOR> IntegerLiteral() )?
     */
    public R visit(MixInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        return _ret;
    }

    /**
     * f0 -> <SPLIT>
     * f1 -> PrimaryExpression()
     * f2 -> <INTO>
     * f3 -> IntegerLiteral()
     */
    public R visit(SplitInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        return _ret;
    }

    /**
     * f0 -> <DRAIN>
     * f1 -> PrimaryExpression()
     */
    public R visit(DrainInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> <HEAT>
     * f1 -> PrimaryExpression()
     * f2 -> <AT>
     * f3 -> IntegerLiteral()
     * f4 -> ( <FOR> IntegerLiteral() )?
     */
    public R visit(HeatInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        return _ret;
    }

    /**
     * f0 -> <DETECT>
     * f1 -> PrimaryExpression()
     * f2 -> <ON>
     * f3 -> PrimaryExpression()
     * f4 -> ( <FOR> IntegerLiteral() )?
     */
    public R visit(DetectInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        return _ret;
    }

    /**
     * f0 -> <REPEAT>
     * f1 -> IntegerLiteral()
     * f2 -> <TIMES>
     * f3 -> <LBRACE>
     * f4 -> Statement()
     * f5 -> <RBRACE>
     */
    public R visit(RepeatInstruction n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> <LPAREN>
     * f2 -> ( ExpressionList() )?
     * f3 -> <RPAREN>
     */
    public R visit(FunctionInvoke n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        return _ret;
    }

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
     * | FunctionInvoke()
     * | MixInstruction()
     * | SplitInstruction()
     * | DetectInstruction()
     * | PrimaryExpression()
     */
    public R visit(Expression n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * | TrueLiteral()
     * | FalseLiteral()
     * | ParenthesisExpression()
     * | IntegerLiteral()
     */
    public R visit(PrimaryExpression n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public R visit(IntegerLiteral n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <NAT>
     */
    public R visit(NatLiteral n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <MAT>
     */
    public R visit(MatLiteral n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <REAL>
     */
    public R visit(RealLiteral n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <TRUE>
     */
    public R visit(TrueLiteral n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <FALSE>
     */
    public R visit(FalseLiteral n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public R visit(Identifier n) {
        R _ret = null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <AND>
     * f2 -> PrimaryExpression()
     */
    public R visit(AndExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHAN>
     * f2 -> PrimaryExpression()
     */
    public R visit(LessThanExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHANEQUAL>
     * f2 -> PrimaryExpression()
     */
    public R visit(LessThanEqualExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <GREATERTHAN>
     * f2 -> PrimaryExpression()
     */
    public R visit(GreaterThanExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <GREATERTHANEQUAL>
     * f2 -> PrimaryExpression()
     */
    public R visit(GreaterThanEqualExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <NOTEQUAL>
     * f2 -> PrimaryExpression()
     */
    public R visit(NotEqualExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <OR>
     * f2 -> PrimaryExpression()
     */
    public R visit(EqualityExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <LESSTHAN>
     * f2 -> PrimaryExpression()
     */
    public R visit(OrExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <ADD>
     * f2 -> PrimaryExpression()
     */
    public R visit(PlusExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <MINUS>
     * f2 -> PrimaryExpression()
     */
    public R visit(MinusExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> <MULTIPLY>
     * f2 -> PrimaryExpression()
     */
    public R visit(TimesExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

    /**
     * f0 -> <BANG>
     * f1 -> Expression()
     */
    public R visit(NotExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> <LPAREN>
     * f1 -> Expression()
     * f2 -> <RPAREN>
     */
    public R visit(ParenthesisExpression n) {
        R _ret = null;
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        return _ret;
    }

}
