package ir.statements;

import java.util.Set;

import chemical.epa.ChemTypes;
import shared.variable.Variable;
import typesystem.elements.Formula;
import typesystem.satsolver.strategies.SolverStrategy;

/**
 * @created: 3/2/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class AssignStatement extends BaseStatement implements Assign {

    public static final String INSTRUCTION = "ASSIGN";
    public static final String VERB = "VARAIBLE_DECLARATION";

    Variable leftOpt;
    Statement rightOp;

    public AssignStatement() {
        super(INSTRUCTION);
    }

    @Override
    public void setLeftOp(Variable variable) {
        this.leftOpt = variable;
    }

    @Override
    public void setRightOp(Statement statement) {
        this.rightOp = statement;
    }

    @Override
    public Variable getOutputVariable() {
        return this.leftOpt;
    }

    @Override
    public Set<ChemTypes> getTypes() {
        return this.leftOpt.getTypes();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", super.toString(), this.rightOp.getName());
    }

    @Override
    public String compose(Formula instruction) {
        StringBuilder sb = new StringBuilder();

        // Only output matters here Output = arbitrary input at beginning of file.
        for (Variable v : instruction.getOutput()) {
            sb.append(this.compose(v));
        }

        return sb.toString();
    }

    @Override
    public String compose(Variable variable) {
        StringBuilder sb = new StringBuilder();

        for (ChemTypes t : variable.getTypes()) {
            sb.append("(assert (= ").append(SolverStrategy.getSMTName(variable.getScopedName(), t)).append(" true))").append(NL);
        }

        return sb.toString();
    }

    @Override
    public String toJson() {
        return this.toJson("");
    }

    @Override
    public String toJson(String indent) {
        return null;
    }
}
