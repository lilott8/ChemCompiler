package phases.inference.satsolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import phases.inference.elements.Instruction;
import phases.inference.elements.Term;
import phases.inference.elements.Variable;
import phases.inference.satsolver.constraints.Constraint;
import phases.inference.satsolver.strategies.SolverStrategy;

/**
 * @created: 8/30/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class SatSolver {

    public static final Logger logger = LogManager.getLogger(SatSolver.class);

    private SolverStrategy strategy;

    public SatSolver() {

    }

    public SatSolver setSatSolver(SolverStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public boolean solveConstraints(Map<String, Constraint> constraints) {
        return this.strategy.solveConstraints(constraints);
    }

    public boolean solveConstraints(Map<Integer, Instruction> instructions, Map<String, Variable> variables) {
        return this.strategy.solveConstraints(instructions, variables);
    }

}
