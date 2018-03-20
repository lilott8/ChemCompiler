package shared.variable;

import java.util.Set;

import chemical.epa.ChemTypes;
import symboltable.Scope;

import static ir.Statement.NL;

/**
 * @created: 3/15/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class DefinedVariable<Value> extends Variable<Value> {

    {
        this.isVariable = false;
    }

    public DefinedVariable(String name) {
        super(name);
    }

    public DefinedVariable(String name, Set<ChemTypes> type) {
        super(name, type);
    }

    public DefinedVariable(String name, Scope scope) {
        super(name, scope);
    }

    public DefinedVariable(String name, Set<ChemTypes> type, Scope scope) {
        super(name, type, scope);
    }

    @Override
    public String buildUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"INPUT_TYPE\" : \"VARIABLE\",").append(NL);
        sb.append("\"CHEMICAL\" : {").append(NL);
        sb.append("\"VARIABLE\" : {").append(NL);
        sb.append("\"NAME\" : \"").append(this.name).append("\"").append(NL);
        sb.append("}").append(NL);
        sb.append("}").append(NL);
        // Variables cannot close the chemical tag, because there might be a property.
        return sb.toString();
    }

    @Override
    public String buildDeclaration() {
        StringBuilder sb = new StringBuilder("");

        sb.append("{").append(NL);
        sb.append("\"VARIABLE_DECLARATION\" : {").append(NL);
        sb.append("\"ID\" : ").append(this.id).append(",").append(NL);
        sb.append("\"NAME\" : \"").append(this.name).append("\",").append(NL);
        sb.append("\"TYPE\" : \"CHEMICAL\", ").append(NL);
        sb.append(this.addInferredTypes());
        sb.append("}").append(NL);
        sb.append("}").append(NL);

        return sb.toString();
    }
}
