package typesystem.rules;

import java.util.List;

import compilation.datastructures.node.InstructionNode;
import typesystem.Inference.InferenceType;
import typesystem.elements.Instruction;
import typesystem.elements.Term;
import shared.Variable;
import substance.Property;
import chemical.combinator.Combiner;
import chemical.combinator.CombinerFactory;
import chemical.identification.Identifier;
import chemical.identification.IdentifierFactory;

import static chemical.epa.ChemTypes.REAL;

/**
 * @created: 9/1/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public abstract class NodeAnalyzer extends Rule {

    protected Identifier identifier;
    protected Combiner combiner;

    protected NodeAnalyzer(InferenceType type) {
        super(type);
    }

    protected NodeAnalyzer(InferenceType type, Class<? extends NodeAnalyzer> clazz) {
        super(type, clazz);
    }

    {
        this.identifier = IdentifierFactory.getIdentifier();
        this.combiner = CombinerFactory.getCombiner();
    }

    public abstract Rule gatherAllConstraints(InstructionNode node);

    protected Instruction gatherProperties(Instruction instruction, List<Property> properties) {
        for (Property p : properties) {
            Variable prop = new Term(Rule.createHash(p.toString()));
            prop.addTypingConstraint(REAL);
            instruction.addInputVariable(prop);
            addVariable(prop);
        }
        return instruction;
    }

    /*protected Rule gatherConstraints(InstructionNode node) {
        /*
         * Temp = Mix x with y for n
         *  ^def_use  |      |     |
         *         getUse   |     |
         *                getUse  |
         *                      property
         */
        /*
        logger.debug("Id: " + instruction.getId());
        logger.debug("Classification: " + instruction.getInstruction().getClassification());
        logger.debug("Input Symbols: " + instruction.getInputSymbols());
        logger.debug("Properties: " + instruction.getInstruction().getProperties());
        logger.debug("Use: " + instruction.getUse());
        logger.debug("Def: " + instruction.get_def());
        logger.debug("Output Symbols: " + instruction.getOutputSymbols());
        logger.debug("toString(): " + instruction.toString());
        logger.warn("=================================");
        return this;
    }*/
}
