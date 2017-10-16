package phases.inference.rules;

import compilation.datastructures.InstructionNode;
import phases.inference.Inference;
import substance.Property;

/**
 * @created: 9/5/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "dispense", ruleType = "term")
public class Dispense extends NodeAnalyzer {

    NodeAnalyzer assign = new Assign(Inference.InferenceType.TERM);

    public Dispense(Inference.InferenceType type) {
        super(type, Dispense.class);
    }

    @Override
    public Rule gatherAllConstraints(InstructionNode node) {
        return this.assign.gatherAllConstraints(node);
    }
}
