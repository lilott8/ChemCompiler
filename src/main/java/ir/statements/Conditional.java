package ir.statements;

import java.util.List;

/**
 * @created: 3/2/18
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public interface Conditional extends Statement {

    String getCondition();

    void setCondition(String condition);
    Statement getTrueTarget();
    List<Statement> getTrueBranch();
    void addToTrueBranch(Statement s);

    void setTrueTarget(Statement target);
    Statement getFalseTarget();
    void setFalseTarget(Statement target);
    List<Statement> getFalseBranch();
    void addToFalseBranch(Statement s);

    String getScopeName();

    void setScopeName(String name);
}
