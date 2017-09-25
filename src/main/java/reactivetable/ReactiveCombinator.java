package reactivetable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import config.ConfigFactory;
import config.InferenceConfig;
import io.file.ThreadedFile;
import shared.substances.ChemAxonCompound;
import typesystem.classification.Classifier;
import typesystem.classification.ClassifierFactory;
import typesystem.combinator.Combiner;
import typesystem.combinator.CombinerFactory;
import typesystem.epa.ChemTypes;

/**
 * @created: 9/14/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class ReactiveCombinator implements Runnable {

    public static final Logger logger = LogManager.getLogger(ReactiveCombinator.class);
    // Actual table that holds the results.
    // X,y coordinates retrieve the resultant reactive groups of
    // Mixing the two groups together.
    private static final Table<Integer, Integer, Set<Integer>> comboTable = HashBasedTable.create();
    // Queue of what needs to be processed (the reactive group ids)
    private final Queue<Integer> queue = new ConcurrentLinkedQueue<>();
    // this houses all the reactive groups and their corresponding chemicals.
    private Map<Integer, Set<ChemAxonCompound>> reactiveGroupToChemicals = new HashMap<>();
    // file to write things to disk
    private final ThreadedFile writer;

    // Cache for combining compounds.
    private static final Table<Long, Long, ChemAxonCompound> comboCache = HashBasedTable.create();
    //private AtomicInteger numChems = new AtomicInteger(0);
    // total records to be processed (queue.size() before runn())
    private int totalRecords;
    // config for setting things
    private InferenceConfig config = ConfigFactory.getConfig();
    // List of id's we a
    private Set<Integer> reactiveGroupId = new LinkedHashSet<>();

    // handy globals to make things easier.
    private Combiner combiner = CombinerFactory.getCombiner();
    private Classifier classifier = ClassifierFactory.getClassifier();

    public ReactiveCombinator(ThreadedFile threadedFile) {
        this.writer = threadedFile;
        this.buildChemicals(this.parseFile());

        this.queue.addAll(this.reactiveGroupId);

        logger.error("Size of queue: " + this.queue);
    }

    public void run() {
        ChemAxonCompound compound;
        int currentReactiveGroup;
        logger.info("Starting thread: ");
        while (!this.queue.isEmpty()) {
            currentReactiveGroup = this.queue.poll();
            logger.info("Running combos with rg: " + currentReactiveGroup);
            // Set of chemicals that belong to the group.
            Set<ChemAxonCompound> set1 = this.reactiveGroupToChemicals.get(currentReactiveGroup);
            // iterate the "inner" loop
            for (Map.Entry<Integer, Set<ChemAxonCompound>> inner : this.reactiveGroupToChemicals.entrySet()) {
                // We don't need to do diagonal comparisons (3,3) or (5,5)
                if (currentReactiveGroup != inner.getKey()) {
                    // Set of chemicals that belong to the group.
                    Set<ChemAxonCompound> set2 = inner.getValue();
                    // Iterate as our "X"
                    for (ChemAxonCompound chemX : set1) {
                        // iterate as our "Y"
                        for (ChemAxonCompound chemY : set2) {
                            // we don't need to compare the same
                            if (chemX.equals(chemY)) {
                                continue;
                            }
                            compound = this.combineChems(chemX, chemY);
                            // Add the types to the map
                            Set<ChemTypes> types = this.classifyChem(compound);
                            this.addToTable(chemX, chemY, types);
                        }
                    }
                } else {
                    this.addToTable(currentReactiveGroup, inner.getKey(), currentReactiveGroup);
                    //this.addToTable(currentReactiveGroup, inner.getKey(), inner.getKey());
                }
            }
            if (this.queue.size() % 4 == 0) {
                logger.info(String.format("Done processing: %.4f%% of records.",
                        ((1-(this.queue.size() / (double) this.totalRecords)) * 100)));
                //this.writeToDisk();
            }
        }
    }

    /**
     * Ensures we always have a place to add the type to.
     * @param x int
     * @param y int
     * @param type int
     * @return boolean
     */
    private synchronized boolean addToTable(int x, int y, int type) {
        // If we do one way dominated, we guarantee a sparse matrix.
        if (x > y) {
            int temp = x;
            x = y;
            y = temp;
        }
        if (!comboTable.contains(x, y)) {
            comboTable.put(x, y, new HashSet<>());
        }
        comboTable.get(x, y).add(type);
        // just to be sure, add the rgs of the current chemicals.
        comboTable.get(x, y).add(x);
        comboTable.get(x, y).add(y);
        return true;
    }

    /**
     * "Public" exposing method to add a reaction to the table
     * @param chemX ChemAxonCompound
     * @param chemY ChemAxonCompound
     * @param types set of ChemTypes
     * @return boolean
     */
    private synchronized boolean addToTable(ChemAxonCompound chemX, ChemAxonCompound chemY, Set<ChemTypes> types) {
        for (ChemTypes x : chemX.getReactiveGroups()) {
            for (ChemTypes y : chemY.getReactiveGroups()) {
                for (ChemTypes type : types) {
                    this.addToTable(x.getValue(), y.getValue(), type.getValue());
                }
            }
        }
        return true;
    }

    /**
     * Wrapper function to classify the created compound.
     * @param compound ChemAxonCompound
     * @return Set of ReactiveGroups
     */
    private synchronized Set<ChemTypes> classifyChem(ChemAxonCompound compound) {
        return classifier.classify(compound);
    }

    /**
     * Wrapper funciton to add the molecule to the ChemAxonCompound
     * @param a ChemAxonCompound
     * @param chem Chemical object
     * @return ChemAxonCompound with molecule.
     */
    private synchronized ChemAxonCompound addMolecule(ChemAxonCompound a, Chemical chem) {
        try {
            a.setRepresentation(MolImporter.importMol(chem.canonicalSmiles, "smiles"));
        } catch(MolFormatException e) {
            logger.error("Could not instantiate: " + chem.pubChemId);
        }
        return a;
    }

    /**
     * Combine the chemicals to get the resultant reactive group(s).
     * @param a ChemAxonCompound
     * @param b ChemAxonCompound
     * @return ChemAxonCompound
     */
    private synchronized ChemAxonCompound combineChems(ChemAxonCompound a, ChemAxonCompound b) {
        if (a.getId() > b.getId()) {
            ChemAxonCompound temp = a;
            b = a;
            a = temp;
        }

        if (comboCache.contains(a.getId(), b.getId())) {
            return comboCache.get(a.getId(), b.getId());
        } else {
            try {
                comboCache.put(a.getId(), b.getId(), (ChemAxonCompound) combiner.combine(a, b));
                return comboCache.get(a.getId(), b.getId());
            } catch (Exception e) {
                comboCache.put(a.getId(), b.getId(), (ChemAxonCompound) combiner.combine(a, b));
                return comboCache.get(a.getId(), b.getId());
            }
        }
    }

    /**
     * Write portions of the table to disk.
     * Useful for obtaining intermediate results.
     */
    public synchronized void writeToDisk() {
        for (Table.Cell<Integer, Integer, Set<Integer>> cell: comboTable.cellSet()){
            StringBuilder sb = new StringBuilder();
            sb.append(cell.getRowKey()).append("|").append(cell.getColumnKey()).append("|");
            for (int type : cell.getValue()) {
                sb.append(type).append("_");
            }
            this.writer.push(sb.toString());
            //System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
        }
    }

    /**
     * This parses the chemicals to a temporary container.
     * This temporary container just makes it easier to manage
     * The data and not have to put ugliness in here.
     * @return List of chemicals.
     */
    private List<Chemical> parseFile() {
        BufferedReader reader = null;
        List<Chemical> results = new ArrayList<>();
        try {
            File file = new File(this.config.getFilesForCompilation().get(0));
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                // Number of chemicals that ultimately need to be mixed.
                this.totalRecords++;
                Chemical chem = new Chemical(line);
                results.add(chem);
                this.reactiveGroupId.add(chem.reactiveGroup);
                // Make sure the reactiveGroupToChemicals has the sets they need!
                if (!this.reactiveGroupToChemicals.containsKey(chem.reactiveGroup)) {
                    this.reactiveGroupToChemicals.put(chem.reactiveGroup, new HashSet<>());
                }
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch(IOException ee) {}
            }
        }
        return results;
    }

    /**
     * Use ChemAxon to instantiate the chemicals.
     * Store those in the cache.  "Rip the band-aid off first."
     * @param input
     */
    private void buildChemicals(List<Chemical> input) {
        int count = 0;

        // Cache the creation of the chemicals.
        Map<Long, ChemAxonCompound> chemicalCache = new ConcurrentHashMap<>();

        for (Chemical chem : input) {
            // If we have seen the chemical before, add to the reactive group
            if (chemicalCache.containsKey(chem.pubChemId)) {
                chemicalCache.get(chem.pubChemId).addReactiveGroup(chem.reactiveGroup);
                this.addChemicalToReactiveGroups(chemicalCache.get(chem.pubChemId));
            } else {
                ChemAxonCompound compound = new ChemAxonCompound(chem.pubChemId, chem.canonicalSmiles);
                compound.addReactiveGroup(chem.reactiveGroup);
                compound = this.addMolecule(compound, chem);
                chemicalCache.put(compound.getId(), compound);
                this.addChemicalToReactiveGroups(compound);
            }
            count++;
            double done = 100*(count / (double)input.size());
            if (this.config.isDebug()) {
                logger.trace("done with chemical " + count);
            }
            if (input.size() % 100 == 0) {
                logger.debug(String.format("Done processing: %.4f%% chemicals", done));
            }
        }
    }

    /**
     * Adds the instantiated chemicals to their
     * Respective reactive group.
     * @param compound ChemAxonCompound
     */
    private void addChemicalToReactiveGroups(ChemAxonCompound compound) {
        for (ChemTypes type : compound.getReactiveGroups()) {
            this.reactiveGroupToChemicals.get(type.getValue()).add(compound);
        }
    }
}
