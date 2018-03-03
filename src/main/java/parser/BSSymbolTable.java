package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import ir.soot.instruction.Assign;
import ir.soot.instruction.Drain;
import ir.soot.instruction.Invoke;
import ir.soot.instruction.Mix;
import ir.soot.statement.Branch;
import parser.ast.Assignment;
import parser.ast.DetectInstruction;
import parser.ast.DrainInstruction;
import parser.ast.ElseIfStatement;
import parser.ast.ElseStatement;
import parser.ast.FormalParameter;
import parser.ast.FunctionDefinition;
import parser.ast.FunctionInvoke;
import parser.ast.HeatInstruction;
import parser.ast.IfStatement;
import parser.ast.Manifest;
import parser.ast.MixInstruction;
import parser.ast.Module;
import parser.ast.RepeatInstruction;
import parser.ast.SplitInstruction;
import parser.ast.Stationary;
import shared.variable.Variable;
import shared.variable.Method;
import symboltable.SymbolTable;
import chemical.epa.ChemTypes;

import static chemical.epa.ChemTypes.MODULE;
import static chemical.epa.ChemTypes.NAT;
import static typesystem.rules.Rule.InstructionType.FUNCTION;

/**
 * @created: 2/1/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class BSSymbolTable extends BSVisitor {

    public static final Logger logger = LogManager.getLogger(BSSymbolTable.class);
    // Arguments to functions, etc.
    private List<Variable> arguments = new ArrayList<>();

    public BSSymbolTable() {
    }

    @Override
    public BSVisitor run() {
        return this;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * f0 -> <STATIONARY>
     * f1 -> ( TypingList() )?
     * f2 -> PrimaryExpression()
     */
    @Override
    public BSVisitor visit(Stationary n) {
        // super.visit(n);
        // Get the types.
        n.f1.accept(this);
        // Get the identifier.
        n.f2.accept(this);

        // Type checking material.
        this.instruction = new Assign();
        Variable term = new Variable(this.name);
        term.addScope(this.symbolTable.getCurrentScope());
        term.addTypingConstraints(this.getTypingConstraints(term));
        addVariable(term);
        // End type checking.

        // Anything in this section is always default scope.
        this.symbolTable.addLocal(term);
        this.types.clear();
        return this;
    }

    /**
     * f0 -> <MANIFEST>
     * f1 -> ( TypingList() )?
     * f2 -> PrimaryExpression()
     */
    @Override
    public BSVisitor visit(Manifest n) {
        // super.visit(n);
        // Begin type checking.

        // Get the types.
        n.f1.accept(this);
        // Get the identifier.
        n.f2.accept(this);

        // Build the symbol.
        Variable term = new Variable(this.name);
        term.addScope(this.symbolTable.getCurrentScope());
        term.addTypingConstraints(this.getTypingConstraints(term));
        addVariable(term);
        // End type checking.

        // build the variable now
        this.symbolTable.addLocal(term);
        this.types.clear();

        return this;
    }

    /**
     * f0 -> <MODULE>
     * f1 -> Identifier()
     */
    @Override
    public BSVisitor visit(Module n) {
        // Get the name.
        n.f1.accept(this);
        // Build the symbol.
        Variable term = new Variable(this.name, this.symbolTable.getCurrentScope());
        term.addTypingConstraint(MODULE);
        addVariable(term);
        // Add the symbol to the scope.
        this.symbolTable.addLocal(term);

        return this;
    }

    /**
     * f0 -> ( TypingList() )*
     * f1 -> Identifier()
     * f2 -> <ASSIGN>
     * f3 -> Expression()
     */
    @Override
    public BSVisitor visit(Assignment n) {
        // Get the expression done before we get the identifier.
        // That way we can set the appropriate instruction.
        n.f3.accept(this);
        // Once we have established the expression,
        // We can identify the identifier.
        n.f1.accept(this);
        // Search the hierarchy for the output var.
        Variable output = this.symbolTable.searchScopeHierarchy(this.name, this.symbolTable.getCurrentScope());
        if (output == null) {
            output = new Variable(this.name, this.symbolTable.getCurrentScope());
            if (n.f0.present()) {
                n.f0.accept(this);
                output.addTypingConstraints(this.getTypingConstraints(output));
            }
            this.symbolTable.addLocal(output);
        }

        if (this.instruction instanceof Invoke) {
            output.addTypingConstraints(this.method.getTypes());
        } else {
            output.addTypingConstraints(this.getTypingConstraints(output));
        }
        this.instruction.addOutputVariable(output);
        addVariable(output);
        addInstruction(this.instruction);
        this.types.clear();
        return this;
    }

    /**
     * f0 -> Identifier()
     * f1 -> <LPAREN>
     * f2 -> ( ExpressionList() )?
     * f3 -> <RPAREN>
     */
    @Override
    public BSVisitor visit(FunctionInvoke n) {
        // Get the method name.
        n.f0.accept(this);
        // Get the method.
        Method method = this.symbolTable.getMethods().get(this.name);
        if (method == null) {
            logger.fatal("Undeclared function: " + this.name);
            return this;
        }

        this.instruction = new Invoke(method);
        //this.instruction.addInputVariable(method);

        n.f2.accept(this);
        return this;
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
    @Override
    public BSVisitor visit(FunctionDefinition n) {
        // Lets us know if we have a typed function,
        // If we do, then there must be a return statement.
        boolean needReturn = false;
        // Get the name of the method.
        n.f1.accept(this);
        // The method belongs to the parent scope.
        Variable term = new Variable(this.name, this.symbolTable.getCurrentScope());
        // Now we have a new scope
        this.symbolTable.newScope(this.name);
        // Start a new scope.
        this.method = new Method(this.name);

        // Get the parameters of this method
        n.f3.accept(this);
        // Add the parameters to the method.
        // Parameters are not local to the method.
        this.method.addParameters(this.arguments);
        // Clear the arguments list
        this.arguments.clear();

        // Get the type(s) of this method.
        if (n.f5.present()) {
            n.f5.accept(this);
            this.method.addReturnTypes(this.types);
            this.types.clear();
            needReturn = true;
        } else {
            this.types.add(ChemTypes.NULL);
            this.method.addReturnTypes(this.types);
            this.types.clear();
        }

        // Get the list of statements.
        n.f7.accept(this);

        if (n.f8.present()) {
            // Get the return statement.
            n.f8.accept(this);
            // If this symbol has already been processed, we don't have to do anything.
            // If there is a return type, then we must add that to the variable name.
            if (!BSVisitor.variables.containsKey(this.symbolTable.getCurrentScope().getName() + "_" + this.name)) {
                Variable ret = checkForOrCreateVariable(); // new Variable(this.name, this.symbolTable.getCurrentScope());
                ret.addTypingConstraints(method.getTypes());
                this.symbolTable.addLocal(ret);
                addVariable(ret);
            }

            // This will associate the type of the function with the type of what is being returned.
            if (!method.hasReturnTypes()) {
                method.addReturnTypes(BSVisitor.variables.get(this.symbolTable.getCurrentScope().getName() + "_" + this.name).getTypes());
            }
        } else {
            if (needReturn) {
                logger.fatal("You need a return statement!");
            }
        }

        this.symbolTable.addMethod(this.method);
        addVariable(term);

        this.symbolTable.endScope();
        // Remove the lock for functions.

        return this;
    }

    /**
     * f0 -> ( TypingList() )*
     * f1 -> Identifier()
     */
    @Override
    public BSVisitor visit(FormalParameter n) {
        // super.visit(n);
        // Go fetch the typing list
        n.f0.accept(this);
        // Go fetch the name
        n.f1.accept(this);
        // save the record.
        Variable v = new Variable(this.name, this.types, this.symbolTable.getCurrentScope());
        // this.arguments.add(v);
        this.symbolTable.addLocal(v);
        this.types.clear();

        return this;
    }

    /**
     * f0 -> <REPEAT>
     * f1 -> IntegerLiteral()
     * f2 -> <TIMES>
     * f3 -> <LBRACE>
     * f4 -> Statement()
     * f5 -> <RBRACE>
     */
    @Override
    public BSVisitor visit(RepeatInstruction n) {
        // super.visit(n);
        // Start a new scope.
        String scopeName = String.format("%s_%d", REPEAT, scopeId++);
        this.symbolTable.newScope(scopeName);

        this.name = String.format("%s_%d", NAT, integerId++);
        Variable term = new Variable(this.name, this.symbolTable.getCurrentScope());
        term.addTypingConstraint(NAT);
        addVariable(term);


        // Get the statements.
        n.f4.accept(this);
        // Return back to old scoping.
        this.symbolTable.endScope();

        return this;
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
    @Override
    public BSVisitor visit(IfStatement n) {
        // Build the instruction.
        logger.fatal("You need to reset the instruction in If");
        //this.instruction = new Branch();
        // Build the name.
        this.name = String.format("%s_%d", BRANCH, scopeId++);
        // Create a new scope.
        this.symbolTable.newScope(this.name);
        // Build the variable that resolves a branch evaluation.
        Variable term = new Variable(String.format("%s_%d", NAT, integerId++), this.symbolTable.getCurrentScope());
        term.addTypingConstraint(NAT);
        addVariable(term);
        this.instruction.addInputVariable(term);
        addInstruction(this.instruction);
        // End type checking.

        this.symbolTable.addLocal(term);
        // Get the expression.
        n.f2.accept(this);
        // Get the statement.
        n.f5.accept(this);
        // Return back to old scoping.
        this.symbolTable.endScope();

        return this;
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
    @Override
    public BSVisitor visit(ElseIfStatement n) {
        // super.visit(n);
        String scopeName = String.format("%s_%d", BRANCH, scopeId++);
        this.symbolTable.newScope(scopeName);

        // Begin type checking.
        logger.fatal("You need to reset the instruction in ElseIf");
        //this.instruction = new Branch();
        Variable term = new Variable(this.name, this.symbolTable.getCurrentScope());
        this.name = String.format("%s_%d", NAT, integerId);
        term.addTypingConstraint(NAT);
        addVariable(term);
        this.instruction.addInputVariable(term);
        addInstruction(this.instruction);
        // End type checking.

        this.symbolTable.addLocal(term);
        n.f2.accept(this);
        n.f5.accept(this);
        // Return back to old scoping.
        this.symbolTable.endScope();

        return this;
    }

    /**
     * f0 -> <ELSE>
     * f1 -> <LBRACE>
     * f2 -> Statement()
     * f3 -> <RBRACE>
     */
    @Override
    public BSVisitor visit(ElseStatement n) {
        // super.visit(n);
        String scopeName = String.format("%s_%d", BRANCH, scopeId++);
        this.symbolTable.newScope(scopeName);
        n.f2.accept(this);
        // Return back to old scoping.
        this.symbolTable.endScope();

        return this;
    }

    /**
     * f0 -> <MIX>
     * f1 -> PrimaryExpression()
     * f2 -> <WITH>
     * f3 -> PrimaryExpression()
     * f4 -> ( <FOR> IntegerLiteral() )?
     */
    @Override
    public BSVisitor visit(MixInstruction n) {
        this.instruction = new Mix();

        // Get the first material.
        n.f1.accept(this);
        checkForOrCreateVariable();

        // Get the other material.
        n.f3.accept(this);
        checkForOrCreateVariable();

        if (n.f4.present()) {
            n.f4.accept(this);
            checkForOrCreateVariable();
        }

        return this;
    }

    /**
     * f0 -> <SPLIT>
     * f1 -> PrimaryExpression()
     * f2 -> <INTO>
     * f3 -> IntegerLiteral()
     */
    @Override
    public BSVisitor visit(SplitInstruction n) {
        //super.visit(n);
        n.f1.accept(this);
        checkForOrCreateVariable();

        n.f3.accept(this);
        // Use the generated name for the integer.
        Variable term = new Variable(String.format("%s_%d", INTEGER, integerId++), this.symbolTable.getCurrentScope());
        term.addTypingConstraint(NAT);
        addVariable(term);
        this.symbolTable.addLocal(term);
        this.instruction.addProperty(term);

        return this;
    }

    private Variable checkForOrCreateVariable() {
        Variable declaration = this.symbolTable.searchScopeHierarchy(this.name, this.symbolTable.getCurrentScope());
        if (declaration == null) {
            Variable term;
            term = new Variable(this.name, this.symbolTable.getCurrentScope());
            term.addTypingConstraints(this.getTypingConstraints(term));
            addVariable(term);
            this.instruction.addInputVariable(term);
            this.symbolTable.addLocal(term);
            this.types.clear();
            return term;
        } else {
            this.instruction.addInputVariable(declaration);
            this.types.clear();
            return declaration;
        }
    }

    /**
     * f0 -> <DRAIN>
     * f1 -> PrimaryExpression()
     */
    @Override
    public BSVisitor visit(DrainInstruction n) {
        //super.visit(n);
        n.f1.accept(this);
        Variable term = this.checkForOrCreateVariable();

        this.instruction = new Drain();
        this.instruction.addInputVariable(term);
        addInstruction(this.instruction);

        return this;
    }

    /**
     * f0 -> <HEAT>
     * f1 -> PrimaryExpression()
     * f2 -> <AT>
     * f3 -> IntegerLiteral()
     * f4 -> ( <FOR> IntegerLiteral() )?
     */
    @Override
    public BSVisitor visit(HeatInstruction n) {
        // super.visit(n);

        n.f1.accept(this);
        this.checkForOrCreateVariable();

        n.f3.accept(this);
        this.checkForOrCreateVariable();
        if (n.f4.present()) {
            n.f4.accept(this);
            Variable term = new Variable(this.name, this.symbolTable.getCurrentScope());
            term.addTypingConstraint(NAT);
            addVariable(term);
            this.instruction.addProperty(term);
            this.symbolTable.addLocal(new Variable(String.format("%s_%d", INTEGER, integerId++), this.types));
            this.types.clear();
        }
        addInstruction(this.instruction);
        return this;
    }

    /**
     * f0 -> <DETECT>
     * f1 -> PrimaryExpression()
     * f2 -> <ON>
     * f3 -> PrimaryExpression()
     * f4 -> ( <FOR> IntegerLiteral() )?
     */
    @Override
    public BSVisitor visit(DetectInstruction n) {
        // super.visit(n);
        // Get the name
        n.f1.accept(this);
        this.checkForOrCreateVariable();

        n.f3.accept(this);
        Variable input = this.checkForOrCreateVariable();
        this.instruction.addInputVariable(input);
        this.types.clear();

        if (n.f4.present()) {
            n.f4.accept(this);
            this.checkForOrCreateVariable();
        }

        return this;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String toString() {
        return this.symbolTable.toString();
        //return BSVisitor.instructions.toString();
    }


}
