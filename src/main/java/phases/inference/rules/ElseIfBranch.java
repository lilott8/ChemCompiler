package phases.inference.rules;

import compilation.datastructures.InstructionNode;
import compilation.datastructures.basicblock.BasicBlockEdge;
import phases.inference.Inference.InferenceType;
import substance.Property;


/**
 * @created: 7/27/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "elseif", ruleType = "instruction", analyze = "edge")
public class ElseIfBranch extends EdgeAnalyzer {

    public ElseIfBranch(InferenceType type) {
        super(type);
    }

    @Override
    public Rule gatherConstraints(BasicBlockEdge edge) {
        return null;
    }
}
