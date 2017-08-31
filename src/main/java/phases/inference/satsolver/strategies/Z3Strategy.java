package phases.inference.satsolver.strategies;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * @created: 8/24/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class Z3Strategy implements SolverStrategy {

    public static final Logger logger = LogManager.getLogger(Z3Strategy.class);

    @Override
    public void solveConstraints(Map<String, Set<String>> constraints) {
        logger.error("Mutating constraints for testing!");
        //constraints.get("Water").add("Int");
        this.solveWithSMT2(this.generateSMT2(constraints));
    }

    private String generateSMT2(Map<String, Set<String>> constraints) {
        StringBuilder sb = new StringBuilder();
        sb.append("(declare-datatypes () ((Type Int Real Mat)))").append(System.lineSeparator());
        boolean printAssert = true;

        for (Map.Entry<String, Set<String>> entry : constraints.entrySet()) {
            sb.append("(declare-const ").append(entry.getKey()).append(" Type)").append(System.lineSeparator());

            //sb.append("(");
            if (entry.getValue().size() > 1) {
                sb.append("(assert (and").append(System.lineSeparator());
                printAssert = false;
            }
            for (String infer : entry.getValue()) {
                if (printAssert) {
                    sb.append("(assert ");
                }
                sb.append("(= ").append(entry.getKey()).append(" ").append(StringUtils.capitalize(infer)).append(")").append(System.lineSeparator());
                if (printAssert) {
                    sb.append(")").append(System.lineSeparator());
                }
            }
            if (entry.getValue().size() > 1) {
                sb.append(")").append(System.lineSeparator()).append(")").append(System.lineSeparator());
            }
            //sb.append(")");
            // reset this each iteration.
            printAssert = true;
        }
        //sb.append("(check-sat)").append(System.lineSeparator());
        //sb.append("(get-model)").append(System.lineSeparator());
        logger.info(sb.toString());
        return sb.toString();
    }

    private void solveWithSMT2(String smt2) {
        Context context = new Context();
        BoolExpr expr = context.parseSMTLIB2String(smt2, null, null, null, null);
        Solver solver = context.mkSolver();
        solver.add(expr);
        Status status = solver.check();
        if (status == Status.SATISFIABLE) {
            logger.trace("SAT!");
        } else {
            logger.error("UNSAT");
        }

    }
}