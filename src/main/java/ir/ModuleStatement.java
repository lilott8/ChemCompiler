package ir;

import shared.properties.Property;
import shared.variable.Variable;
import typesystem.elements.Formula;

/**
 * @created: 3/6/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class ModuleStatement extends BaseNop {

    public static final String INSTRUCTION = "MODULE";

    public ModuleStatement() {
        super(INSTRUCTION);
    }

    @Override
    public String compose(Formula instruction) {
        return super.defaultCompose(instruction);
    }

    @Override
    public String compose(Variable variable) {
        return super.defaultCompose(variable);
    }

    @Override
    public String compose(Property property) {
        return super.defaultCompose(property);
    }

    @Override
    public String toJson() {
        return this.toJson("");
    }

    @Override
    public String toJson(String indent) {
        return this.inputVariables.get(0).buildDeclaration();
    }
}
