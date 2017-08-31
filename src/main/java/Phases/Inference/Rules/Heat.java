package Phases.Inference.Rules;

import java.util.Set;

import CompilerDataStructures.InstructionNode;
import Phases.Inference.Callback;

/**
 * @created: 7/27/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "heat")
public class Heat implements Callback {

    public Set<String> callback(InstructionNode node) {
        return null;
    }
}
