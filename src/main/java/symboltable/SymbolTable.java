package symboltable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import shared.variable.Method;
import shared.variable.Variable;

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
    // Keep tabs of depth of stack.
    private Deque<Scope> scopeStack = new ArrayDeque<>();

    public SymbolTable() {
        // Add the default scope to the stack; it has no parent.
        Scope origins = new Scope(DEFAULT_SCOPE);
        // Push the scope to the stack.
        this.scopeStack.push(origins);
        this.scopes.put(DEFAULT_SCOPE, origins);
    }

    public Scope newScope(String name) {
        // Create a new context.
        Scope scope = new Scope(name, this.scopeStack.peek());
        // Push the new scope onto the stack.
        this.scopeStack.push(scope);
        // Save the scope in the hashmap.
        this.scopes.put(name, this.scopeStack.peek());
        // Return the new scope.
        return this.scopeStack.peek();
    }

    public Scope endScope() {
        // Remove the most recent element.
        this.scopeStack.pop();
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

    public Scope getParentScope() {
        return this.scopeStack.peek().getParentScope();
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

    public Variable searchScopesForVariable(String name) {
        for(Map.Entry<String, Scope> scope : this.scopes.entrySet()) {
            if (scope.getValue().getVariables().containsKey(name)) {
                return scope.getValue().getVariables().get(name);
            }
        }
        return null;
    }

    /**
     * Recursive function looking for a definition of a
     * variable in parent scope(s).
     * @param name name of variable
     * @param s scope
     * @return variable or null
     */
    @Nullable
    public Variable searchScopeHierarchy(String name, Scope s) {
        if (s == null) {
            return null;
        } else {
            if (s.getVariables().containsKey(name)) {
                return s.getVariables().get(name);
            } else {
                return searchScopeHierarchy(name, s.getParentScope());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //for (Map.Entry<String, Variable> entry : this.symbols.entrySet()) {
        //    sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
        //}
        //sb.append("=========================").append(System.lineSeparator());
        for (Map.Entry<String, Scope> entry : this.scopes.entrySet()) {
            sb.append("Scope: ").append(entry.getKey()).append(": ")
                    .append(System.lineSeparator()).append(entry.getValue());
            sb.append("===========================").append(System.lineSeparator());
        }
        return sb.toString();
    }
}
