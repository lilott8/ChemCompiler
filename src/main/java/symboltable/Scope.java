package symboltable;

import java.util.HashMap;
import java.util.Map;

import shared.Variable;

/**
 * @created: 2/8/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class Scope {

    public enum Visibility {
        FUNCTION, GLOBAL, LOOP, BRANCH
    }

    private String name;
    // Will probably never be used, but jic.
    private int frameSize = 0;
    // Will probably never be used, but jic.
    private Visibility type = Visibility.GLOBAL;
    Map<String, Variable> symbols = new HashMap<>();

    public Scope(String name) {
        this.name = name;
    }

    public Scope(String name, Visibility type) {
        this.name = name;
        this.type = type;
    }

    public Scope(String name, Visibility type, int frameSize) {
        this.name = name;
        this.type = type;
        this.frameSize = frameSize;
    }

    public void addSymbol(Variable symbol) {
        this.symbols.put(symbol.getName(), symbol);
    }

    public String getName() {
        return name;
    }

    public Visibility getType() {
        return type;
    }

    public Map<String, Variable> getVariables() {
        return symbols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Variable> entry : this.symbols.entrySet()) {
            sb.append("\t").append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
