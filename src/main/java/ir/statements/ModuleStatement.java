package ir.statements;

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
    public String toJson() {
        return this.toJson("");
    }

    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder("");
        sb.append("\"SENSOR_DECLARATION\" : {").append(Statement.NL);
        sb.append("\"ID\" : ").append(this.id).append(",").append(Statement.NL);
        sb.append("\"NAME\" : \"").append(this.inputVariables.get(0).getName()).append("\",").append(Statement.NL);
        sb.append("\"TYPE\" : \"SENSOR\"").append(Statement.NL).append("}").append(Statement.NL);
        sb.append(",").append(Statement.NL);
        return sb.toString();
    }
}
