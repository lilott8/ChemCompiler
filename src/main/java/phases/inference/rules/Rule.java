package phases.inference.rules;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import config.ConfigFactory;
import config.InferenceConfig;
import phases.inference.satsolver.constraints.MatSMT;
import phases.inference.satsolver.constraints.NumSMT;
import typesystem.epa.ChemTypes;
import phases.inference.Inference.InferenceType;
import phases.inference.satsolver.constraints.Constraint;
import static phases.inference.satsolver.constraints.Constraint.ConstraintType;

/**
 * @created: 7/31/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public abstract class Rule {

    protected InferenceType type;

    protected final Logger logger;

    public final String CONST = "constant";

    protected InferenceConfig config = ConfigFactory.getConfig();

    // This implicitly allows us to do union sets with types
    protected Map<String, Constraint> constraints = new HashMap<>();

    protected Rule(InferenceType type) {
        this.type = type;
        this.logger = LogManager.getLogger(Rule.class);
    }
    protected Rule(InferenceType type, Class<? extends Rule> clazz) {
        this.type = type;
        logger = LogManager.getLogger(clazz);
    }

    public Map<String, Constraint> getConstraints() {
        return constraints;
    }

    public InferenceType getType() {
        return type;
    }

    protected void addConstraints(String key, Set<ChemTypes> value, ConstraintType type) {
        for (ChemTypes t : value) {
            this.addConstraint(key, t, type);
        }
    }

    protected void addConstraint(String key, ChemTypes value, ConstraintType type) {
        if (!this.constraints.containsKey(key)) {
            switch (type) {
                default:
                    constraints.put(key, new NumSMT(key, type));
                    break;
                case MIX:
                case ASSIGN:
                case HEAT:
                    constraints.put(key, new MatSMT(key, type));
                    break;
            }
        }
        constraints.get(key).addConstraint(value);
    }

    public static boolean isNumeric(String value) {
        return Rule.isNaturalNumber(value) || Rule.isRealNumber(value);
    }

    public static boolean isNaturalNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isRealNumber(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static String createHash(String input) {
        Logger log = LogManager.getLogger(Rule.class);
        log.debug("creating hash for: " + input);
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            int x = 0;
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
                x++;
            }
            if (Rule.isNumeric(Character.toString(sb.charAt(0)))) {
                sb.insert(0, "a");
            }
            log.debug("Hash of: " + input + ":\t " + sb.toString());
            return sb.toString();
        } catch(NoSuchAlgorithmException e) {
            input = "a" + input;
            log.debug("Hash of: " + input + ":\t " + input);
            return input;
        }
    }

    public static boolean isMaterial(String value) {
        return true;
    }
}
