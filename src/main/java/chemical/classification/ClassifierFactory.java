package chemical.classification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.ConfigFactory;
import config.InferenceConfig;

/**
 * @created: 9/13/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class ClassifierFactory {
    private static InferenceConfig config = ConfigFactory.getConfig();
    private static final Classifier classifier;
    public static final Logger logger = LogManager.getLogger(ClassifierFactory.class);

    static {
        String message = "";
        if (config.buildFilters()) {
            message = "Using ChemAxon classifier.";
            classifier = new ChemAxonClassifier();
        } else {
            message = "Using naive classifier.";
            classifier = new NaiveClassifier();
        }
        if (config.isDebug()) {
            logger.info(message);
        }
    }

    public static Classifier getClassifier() {
        return classifier;
    }

}
