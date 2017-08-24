package phases.inference.rules;

import phases.inference.satsolver.Solver;

import CompilerDataStructures.InstructionNode;
import phases.inference.Inference.InferenceType;
import substance.Property;

/**
 * @created: 7/27/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "math", ruleType = "term")
public class Math extends Rule {

    public Math(InferenceType type) {
        super(type);
    }

    @Override
    public Solver gatherConstraints(InstructionNode node, Solver solver) {
        return null;
    }

    @Override
    public Solver gatherUseConstraints(String input, Solver solver) {
        return null;
    }

    @Override
    public Solver gatherDefConstraints(String input, Solver solver) {
        return null;
    }

    @Override
    public Solver gatherConstraints(Property property, Solver solver) {
        return null;
    }
}
