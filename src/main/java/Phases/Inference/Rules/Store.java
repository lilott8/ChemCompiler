package Phases.Inference.Rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import CompilerDataStructures.InstructionNode;
import Phases.Inference.Callback;
import Phases.Inference.Inference.InferenceType;

/**
 * @created: 7/28/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "store", ruleType = "instruction")
public class Store extends Rule {

    public Store(InferenceType type) {
        super(type);
    }

    @Override
    public Rule gatherConstraints(InstructionNode node) {
        return null;
    }
}
