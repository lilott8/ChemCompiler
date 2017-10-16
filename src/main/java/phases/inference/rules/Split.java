package phases.inference.rules;

import compilation.datastructures.InstructionNode;
import phases.inference.Inference.InferenceType;
import phases.inference.elements.Instruction;
import phases.inference.elements.Term;
import phases.inference.elements.Variable;
import substance.Property;
import phases.inference.satsolver.constraints.Constraint.ConstraintType;

import static typesystem.epa.ChemTypes.MAT;
import static typesystem.epa.ChemTypes.NAT;
import static typesystem.epa.ChemTypes.REAL;

/**
 * @created: 7/27/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
@InferenceRule(ruleName = "split", ruleType = "instruction")
public class Split extends NodeAnalyzer {

    public Split(InferenceType type) {
        super(type, Split.class);
    }

    @Override
    public Rule gatherAllConstraints(InstructionNode node) {
        logger.fatal("Using generic implementation for split!");

        logger.info(node);
        logger.info(node.Instruction());
        logger.info(node.Instruction());
        logger.info("Uses: " + node.get_use());
        logger.info("Defs: " + node.get_def());
        logger.info("Props: " + node.Instruction().getProperties());
        logger.info(node.getDispenseSymbols());
        logger.info("=========================");

        Instruction instruction = new Instruction(node.ID(), Split.class.getName());

        Variable input = null;
        for (String s : node.get_use()) {
            input = new Term(s);
            input.addTypingConstraints(getTypingConstraints(input));
            instruction.addInputVariable(input);
            addVariable(input);
        }

        Variable output = null;
        for (String s : node.get_def()) {
            output = new Term(s);
            if (input == null) {
                logger.warn("Input is null!?");
            }
            output.addTypingConstraints(getTypingConstraints(input));
            instruction.addOutputVariable(output);
            addVariable(output);
        }

        for (Property p : node.Instruction().getProperties()) {
            Variable prop = new Term(Rule.createHash(p.toString()));
            prop.addTypingConstraint(REAL);
            instruction.addInputVariable(prop);
            addVariable(prop);
        }

        addInstruction(instruction);
        return this;
    }
}
