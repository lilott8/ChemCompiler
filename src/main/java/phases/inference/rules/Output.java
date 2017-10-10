package phases.inference.rules;

import compilation.datastructures.InstructionNode;
import phases.inference.Inference.InferenceType;
import phases.inference.satsolver.constraints.Constraint.ConstraintType;
import substance.Property;

import static typesystem.epa.ChemTypes.MAT;

/**
 * @created: 7/28/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "output", ruleType = "instruction")
public class Output extends NodeAnalyzer {

    public Output(InferenceType type) {
        super(type);
    }

    @Override
    public Rule gatherAllConstraints(InstructionNode node) {
        return super.gatherConstraints(node);
    }

    @Override
    public Rule gatherUseConstraints(String input) {
        this.addConstraint(input, MAT, ConstraintType.OUTPUT);
        return this;
    }

    @Override
    public Rule gatherDefConstraints(String input) {
        return this;
    }

    @Override
    public Rule gatherConstraints(Property property) {
        return this;
    }
}
