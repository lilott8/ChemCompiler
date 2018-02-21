package symboltable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.Variable;

/**
 * @created: 2/8/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class SymbolTable {

    public static final String DEFAULT_SCOPE = "DEFAULT";

    public static final Logger logger = LogManager.getLogger(SymbolTable.class);

    // Maps variable name to variable class.
    private Map<String, Variable> symbols = new HashMap<>();
    // Maps scope name to scopes.
    private Map<String, Scope> scopes = new HashMap<>();
    // Maps methods to their declaration.
    private Map<String, Method> methods = new HashMap<>();
    // private Map<String, Map<String, Symbol>> scopedSymbols = new HashMap<>();
    // Keep tabs of depth of stack.
    private Deque<Scope> scopeStack = new ArrayDeque<>();

    public SymbolTable() {
        // Add the default scope to the stack.
        Scope origins = new Scope(DEFAULT_SCOPE);
        this.scopeStack.push(origins);
        this.scopes.put(DEFAULT_SCOPE, origins);
        // this.scopedSymbols.put(DEFAULT_SCOPE, new HashMap<>());
    }

    public Scope newScope(String name) {
        // Create a new context and push it onto the stack.
        this.scopeStack.push(new Scope(name));
        // Save the scope in the hashmap.
        this.scopes.put(name, this.scopeStack.peek());
        // Create the scoped symbol hashmap.
        // this.scopedSymbols.put(name, new HashMap<>());
        // Return the new scope.
        return this.scopeStack.peek();
    }

    public Scope endScope() {
        // Remove the most recent element.
        Scope s = this.scopeStack.pop();
        // Return the context we return to.
        return this.scopeStack.peek();
    }

    public void addLocal(Variable symbol) {
        // Cache it for right now.
        Scope s = this.scopeStack.pop();
        // Add the scope to the symbol.
        symbol.addScope(s);
        // Add it to the scope.
        s.addSymbol(symbol);
        // Add it to the global list.
        this.symbols.put(symbol.getName(), symbol);
        // Save the variable in the correct scope.
        // this.scopedSymbols.get(s.getName()).put(symbol.getName(), symbol);

        // Push it back onto the stack!
        this.scopeStack.push(s);
    }

    public void addLocals(List<Variable> symbols) {
        // Cache it for right now.
        Scope s = this.scopeStack.pop();

        for (Variable symbol : symbols) {
            // Add the scope to the symbol.
            symbol.addScope(s);
            s.addSymbol(symbol);
            this.symbols.put(symbol.getName(), symbol);
            // this.scopedSymbols.get(s.getName()).put(symbol.getName(), symbol);
        }

        // Push it back onto the stack!
        this.scopeStack.push(s);
    }

    public void addMethod(Method method) {
        this.methods.put(method.getName(), method);
    }

    public Map<String, Variable> getSymbols() {
        return symbols;
    }

    public Map<String, Scope> getScopes() {
        return scopes;
    }

    public Map<String, Method> getMethods() {
        return this.methods;
    }

    public Scope getCurrentScope() {
        return this.scopeStack.peek();
    }

    public String getCurrentScopeName() {
        return this.scopeStack.peek().getName();
    }

    public Variable getScopedSymbol(String scope, String varName) {
        Variable var = null;
        if (this.scopes.containsKey(scope) && this.scopes.get(scope).symbols.containsKey(varName)) {
            var = this.scopes.get(scope).symbols.get(varName);
        } else {
            logger.warn(String.format("%s doesn't exist in scope %s", varName, this.scopeStack.peek().getName()));
        }
        return var;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Variable> entry : this.symbols.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
        }
        sb.append("=========================").append(System.lineSeparator());
        for (Map.Entry<String, Scope> entry : this.scopes.entrySet()) {
            sb.append("Scope: ").append(entry.getKey()).append(": ")
                    .append(System.lineSeparator()).append(entry.getValue());
        }
        return sb.toString();
    }
}