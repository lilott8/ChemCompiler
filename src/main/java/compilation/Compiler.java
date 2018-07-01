package compilation;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compilation.datastructures.CFGBuilder;
import compilation.datastructures.basicblock.BasicBlock;
import compilation.datastructures.basicblock.DependencySlicedBasicBlock;
import compilation.datastructures.cfg.CFG;
import compilation.datastructures.ssi.StaticSingleInformation;
import config.Config;
import config.ConfigFactory;
import executable.Experiment;
import manager.Benchtop;
import parser.BioScriptParser;
import parsing.BioScript.BenchtopParser;
import shared.Facade;
import shared.Phase;
import shared.Statistics;
import translators.TranslatorFacade;
import typesystem.Inference;

/**
 * Created by chriscurtis on 9/29/16.
 */
public class Compiler {
    public static final Logger logger = LogManager.getLogger(Compiler.class);
    private static boolean abandonShip = false;
    private static String abandonShipReasons = "";
    private Integer idGen = 0;
    private List<CFG> experimentControlFlowGraphs = new ArrayList<>();
    private CFG benchtopControlFlowGraph = new CFG();
    private Benchtop benchtop;
    private StaticSingleInformation SSI;
    private CFG controlFlow;
    private Config config;
    private Map<String, List<Phase>> phases = new HashMap<>();

    public Compiler(Config config) {
        this.config = config;
        this.phases.put(this.config.getInputFile(), new ArrayList<>());
        this.phases.get(this.config.getInputFile()).add(new BioScriptParser(this.config.getInputFile()));
    }

    public static void abandonShip(String reason) {
        abandonShip = true;
        abandonShipReasons += reason + System.lineSeparator();
    }

    public void runPhases() {
        if (!this.config.getErrorLevel().disabled()) {
            for (CFG experiment : this.experimentControlFlowGraphs) {
                if (!abandonShip) {
                    long beginTime = System.nanoTime();
                    Inference phase = new Inference();
                    phase.runPhase(experiment);
                    long inferenceTime = System.nanoTime() - beginTime;
                    Statistics.INSTANCE.addStatsCategory(Statistics.TYPE_CHECKING);
                    Statistics.INSTANCE.addRecord(Statistics.TYPE_CHECKING, String.format("Time (seconds) running inference: %.02f", inferenceTime / 1000000000.0));
                } else {
                    logger.fatal(abandonShipReasons);
                    logger.fatal("Killing program in compiler.");
                    System.exit(0);
                }
            }
        }
    }


    public void runAllOps() {
        this.runTranslations();
    }

    public void runTranslations() {
        if (ConfigFactory.getConfig().translationsEnabled()) {
            if (ConfigFactory.getConfig().isDebug()) {
                // logger.info("Translators are set to be run.");
            }
            for (CFG experiment : this.experimentControlFlowGraphs) {
                Facade translator = new TranslatorFacade(ConfigFactory.getConfig(), experiment);
                translator.start();
            }
        }
    }

    public StaticSingleInformation getSSI() {
        return this.SSI;
    }

    public CFG getControlFlow() {
        return this.controlFlow;
    }

    public List<CFG> getExperiments() {
        return experimentControlFlowGraphs;
    }

    public void compile(int x) {
        this.benchtop = Benchtop.INSTANCE;
        try {
            for (String experimentKey : this.benchtop.getExperiments().keySet()) {
                for (Experiment experiment : this.benchtop.getExperiments().get(experimentKey)) {
                    this.controlFlow = CFGBuilder.buildControlFlowGraph(experiment);
                    this.SSI = new StaticSingleInformation(this.controlFlow);

                    logger.fatal(this.SSI);

                    //replaces basic graph with dependancy sliced versions
                    for (BasicBlock bb : this.SSI.getBasicBlocks().values()) {
                        this.SSI.newBasicBlock(new DependencySlicedBasicBlock(bb, this.SSI));
                    }

                    DependencySlicedBasicBlock.getInOutSets(this.SSI);
                    experimentControlFlowGraphs.add(this.SSI);
                }
            }
        } catch (Exception e) {
            logger.fatal(e);
            e.printStackTrace();
        }

        this.runTranslations();
    }

    public void compile() {

        for (Map.Entry<String, List<Phase>> entry : this.phases.entrySet()) {
            logger.info(entry.getKey());
            for (Phase phase : entry.getValue()) {
                if (!abandonShip) {
                    logger.info("Running: " + phase.getName());
                    Statistics.INSTANCE.addStatsCategory(StringUtils.upperCase(phase.getName()));
                    long start = System.nanoTime();
                    phase.run();
                    long finish = System.nanoTime() - start;
                    Statistics.INSTANCE.addRecord(StringUtils.upperCase(phase.getName()),
                            String.format("Time (seconds) in %s: %.02f",
                                    StringUtils.upperCase(phase.getName()), finish / 1000000000.0));
                } else {
                    logger.fatal(abandonShipReasons);
                    logger.fatal("Killing program in compiler.");
                    System.exit(0);
                }
            }
        }

        BenchtopParser.parseFromString(this.phases.get(this.config.getInputFile()).get(0).getOutput());
        this.benchtop = Benchtop.INSTANCE;
        try {
            long start = System.nanoTime();
            for (String experimentKey : this.benchtop.getExperiments().keySet()) {
                for (Experiment experiment : this.benchtop.getExperiments().get(experimentKey)) {
                    this.controlFlow = CFGBuilder.buildControlFlowGraph(experiment);
                    this.SSI = new StaticSingleInformation(this.controlFlow);

                    //logger.fatal(this.SSI.getBasicBlocks());

                    //replaces basic graph with dependancy sliced versions
                    for (BasicBlock bb : this.SSI.getBasicBlocks().values()) {
                        this.SSI.newBasicBlock(new DependencySlicedBasicBlock(bb, this.SSI));
                    }

                    DependencySlicedBasicBlock.getInOutSets(this.SSI);
                    experimentControlFlowGraphs.add(this.SSI);
                }
            }
            long finish = System.nanoTime() - start;
            Statistics.INSTANCE.addStatsCategory(Statistics.COMPILATION);
            Statistics.INSTANCE.addRecord(Statistics.COMPILATION,
                    String.format("Time (seconds) in %s: %.02f", Statistics.COMPILATION, finish / 1000000000.0));
        } catch (Exception e) {
            logger.fatal(e);
            e.printStackTrace();
        }

        this.runPhases();
        this.runTranslations();

    }
}