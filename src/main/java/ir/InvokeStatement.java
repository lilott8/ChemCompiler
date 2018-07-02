package ir;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import chemical.epa.ChemTypes;
import parser.BSVisitor;
import shared.variable.ManifestVariable;
import shared.variable.Method;
import shared.properties.Property;
import shared.variable.Variable;
import typesystem.elements.Formula;

import static chemical.epa.ChemTypes.NAT;

/**
 * @created: 3/2/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class InvokeStatement extends BaseStatement implements Invoke {

    public static final Logger logger = LogManager.getLogger(InvokeStatement.class);

    private Method method;
    private Map<String, Variable> scopeMatcher = new HashMap<>();

    public InvokeStatement(Method method) {
        super(method.getName());
        this.method = method;
        this.containsInvoke = true;
    }

    @Override
    public Method getMethod() {
        return this.method;
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

    /**
     * This method will be the only instigator of
     * Running alphaConversion().  This is because
     * This entry point is the only known place in which
     * The replacement variables enter a function.
     */
    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        // logger.info(this.inputVariables);
        // logger.info(this.method.getIndexedParameters());

        int comma = 0;
        for (Statement s : this.method.getStatements()) {
            // Convert the variables in the function to
            // Their respective variables passed in.
            s.alphaConversion(this.inputVariables);
            sb.append(s.toJson());

            if (comma < this.method.getStatements().size() - 1) {
                sb.append(",").append(NL);
                comma++;
            }
        }

        // TODO: figure out how to return numbers.
        if (!this.method.getReturnStatement().getOutputVariables().get(0).getTypes().contains(ChemTypes.getNums())) {
            sb.append(",").append(NL);
            MixStatement statement = new MixStatement();
            statement.addInputVariable(this.method.getReturnStatement().getOutputVariables().get(0));
            statement.addInputVariable(this.method.getReturnStatement().getOutputVariables().get(0));
            statement.addOutputVariable(this.outputVariables.get(0));
            sb.append(statement.toJson());
        } else {
            sb.append(",").append(NL);
            MathStatement statement = new MathStatement();
            statement.addInputVariable(this.method.getReturnStatement().getOutputVariables().get(0));
            Variable v = new ManifestVariable<Integer>(BSVisitor.CONST + "_0");
            v.addTypingConstraint(NAT);
            v.setValue(0);
            statement.addInputVariable(v);
            statement.addOutputVariable(this.outputVariables.get(0));
            sb.append(statement.toJson());
        }


        return sb.toString();
    }
}
