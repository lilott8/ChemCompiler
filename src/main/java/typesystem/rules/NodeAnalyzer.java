package typesystem.rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chemical.combinator.Combiner;
import chemical.combinator.CombinerFactory;
import chemical.epa.ChemTypes;
import chemical.identification.Identifier;
import chemical.identification.IdentifierFactory;
import compilation.datastructures.node.InstructionNode;
import substance.Property;
import typesystem.Inference.InferenceType;
import typesystem.elements.Formula;

import static chemical.epa.ChemTypes.REAL;

/**
 * @created: 9/1/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public abstract class NodeAnalyzer extends Rule {

    protected final Set<ChemTypes> propertyTypes = new HashSet<>();
    protected Identifier identifier;
    protected Combiner combiner;

    {
        propertyTypes.add(REAL);
    }

    {
        this.identifier = IdentifierFactory.getIdentifier();
        this.combiner = CombinerFactory.getCombiner();
    }

    protected NodeAnalyzer(InferenceType type) {
        super(type);
    }

    protected NodeAnalyzer(InferenceType type, Class<? extends NodeAnalyzer> clazz) {
        super(type, clazz);
    }

    public abstract Rule gatherAllConstraints(InstructionNode node);

    protected Formula gatherProperties(Formula instruction, List<Property> properties) {
        /*for (Property p : properties) {
            Variable prop = new Term(Rule.createHash(p.toString()), this.propertyTypes);
            prop.addTypingConstraint(REAL);
            instruction.addInputVariable(prop);
            addVariable(prop);
        }*/
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
