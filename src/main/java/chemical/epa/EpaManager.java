package chemical.epa;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import config.ConfigFactory;
import config.InferenceConfig;
import shared.ReportingLevel;
import shared.errors.CompatabilityException;
import shared.io.file.read.FileReader;
import shared.io.file.read.SimpleReader;
import shared.Tuple;
import shared.substances.BaseCompound;

/**
 * Responsible for managing all the groups which exist in the EPA Compatability Cheat Sheet. This is
 * an enum so we don't have to go through the expense of parsing the XML file every time we want to
 * classify things
 */
public enum EpaManager {
    INSTANCE;

    public static final Logger logger = LogManager.getLogger(EpaManager.class);
    // Maps reactive group id to the manifested group replete with classifier data.
    public Map<ChemTypes, Group> groupMap = new HashMap<>();
    // Sparse matrix of reactive group to reactions.
    //public Map<ChemTypes, HashMap<ChemTypes, Reaction>> reactionMap = new HashMap<>();
    public Table<ChemTypes, ChemTypes, Reaction> reactionTable = HashBasedTable.create();
    // Config object for convenience.
    private final InferenceConfig config = ConfigFactory.getConfig();
    private final Table<Integer, Integer, Set<Integer>> reactionMatrix = HashBasedTable.create();

    /**
     * Instantiates the EpaManager and parses the XML file that contains all* the chemical groups
     * which exist in the EPA PDF cheat sheet
     */
    EpaManager() {
        // necessary for logging in the constructor of an instance enum.
        Logger log = LogManager.getLogger(EpaManager.class);

        if (this.config.isDebug()) {
            log.info("Initializing EPAManager.");
        }

        if (!this.config.buildFilters()) {
            log.warn("EPAManager is not building the classification filters.");
        }

        if (config.smartsLength() >= 1) {
            log.warn("SMARTS filters must be length of: " + config.smartsLength() + " to be used.");
        }

        log.info("Building reactive matrix");
        buildReactiveMatrix();
        log.info("Done building reactive matrix.");


        try {
            // buildFromSAX();
            buildFromTree();
        } catch (DocumentException e) {
            log.fatal("Cannot read the chemical reactions file: " + e.toString());
        } catch (Exception e) {
            log.fatal(e.toString());
            e.printStackTrace();
        }

        if (this.config.isDebug()) {
            log.info("EPAManager initialized successfully.");
        }
    }

    /**
     * Parses the XML tree and instantiates the sparse matrix and classifier data.
     */
    private void buildFromTree() throws DocumentException {
        Logger log = LogManager.getLogger(EpaManager.class);
        // Set up the XML parser
        SAXReader reader = new SAXReader();
        Document document = reader.read(this.config.getEpaDefs());

        Element root = document.getRootElement();
        // Attributes that are central to a given group
        int id;
        String name;
        List<Node> groups = root.selectNodes("/chemicalgroups/group");
        int count = 0, completed = 0;
        // iterate the groups
        for (Node group : groups) {
            id = Integer.parseInt(group.selectSingleNode("id").getText());
            ChemTypes chemType = ChemTypes.getTypeFromId(id);
            name = group.selectSingleNode("name").getText();
            Node status = group.selectSingleNode("status");

            // see how complete the group is
            if (!Boolean.parseBoolean(status.selectSingleNode("complete").getText())) {
                //log.warn(String.format("%d - %s message: %s", id, name, status.selectSingleNode("message").getText()));
            } else {
                completed++;
            }

            Node classifiers = group.selectSingleNode("classifiers");
            // get the classification semantics for each of the groups
            final ArrayList<Tuple> words =
                    this.buildAttributes(classifiers.selectSingleNode("words"), "word");
            final ArrayList<Tuple> elements =
                    this.buildAttributes(classifiers.selectSingleNode("elements"), "element");
            final ArrayList<Tuple> smiles =
                    this.buildAttributes(classifiers.selectSingleNode("smiles"), "smiles");
            final ArrayList<Tuple> smarts =
                    this.buildAttributes(classifiers.selectSingleNode("smarts"), "smarts");
            /*
             * Create the group and add it to the mapping.
             */
            Map<Type, List<Tuple>> groupHashMap = new HashMap<>();
            groupHashMap.put(Type.ELEMENT, elements);
            groupHashMap.put(Type.WORD, words);
            groupHashMap.put(Type.SMILES, smiles);
            groupHashMap.put(Type.SMARTS, smarts);
            Group newGroup = new Group(id, name, groupHashMap);
            groupMap.put(ChemTypes.getTypeFromId(id), newGroup);

            // Get the reactions that can occur with each group
            Node reactions = group.selectSingleNode("reactivegroups");
            if (reactions != null) {
                // Initialize a hashmap if we have reactions...
                //if (!this.reactionMap.containsKey(id)) {
                //    this.reactionMap.put(chemType, new HashMap<>());
                //}
                Map<ChemTypes, Reaction> outcomes = this.buildReactionGroups(reactions);
                // To make things easy, we just fill out the lower-half sparse matrix
                // to make lookups easier by eating the space overhead
                for (Map.Entry<ChemTypes, Reaction> entry : outcomes.entrySet()) {
                    //if (!this.reactionMap.containsKey(entry.getKey())) {
                        //this.reactionMap.put(entry.getKey(), new HashMap<>());
                    //}
                    // Lower half
                    this.reactionTable.put(chemType, entry.getKey(), entry.getValue());
                    // symmetric case
                    this.reactionTable.put(entry.getKey(), chemType, entry.getValue());
                    //this.reactionMap.get(chemType).put(entry.getKey(), entry.getValue());
                    //this.reactionMap.get(entry.getKey()).put(chemType, entry.getValue());
                }
            }
            count++;
            if (this.config.isDebug()) {
                if (count % 5 == 0) {
                    log.debug(String.format("%.2f%% complete", 100 * (count / (double) groups.size())));
                }
            }
        }
        if (this.config.isDebug()) {
            float percent = (completed / (float) count) * 100;
            //log.info(String.format("We have: %d/%d -- %.2f%% complete", completed, count, percent));
        }
    }

    /**
     * Generalized parsing of classification semantics
     *
     * @param node elements that build a semantic
     * @param type type of semantic (word, smiles, smarts, etc)
     *
     * @return list of semantics defining a classifier
     */
    private ArrayList<Tuple> buildAttributes(Node node, String type) {
        ArrayList<Tuple> attributes = new ArrayList<Tuple>();

        Type t = Type.valueOf(type.toUpperCase());

        for (Node n : (List<Node>) node.selectNodes("element")) {
            Element e = (Element) n;
            // The data is captured in the "meta" in many places, but currently we only care
            // about the meta value for WORD types.
            String filter = StringUtils.strip(n.getText());
            if (t == Type.WORD && !StringUtils.isEmpty(e.attributeValue("meta"))) {
                attributes.add(new Tuple<Group.SearchBy, String>(
                        Group.SearchBy.valueOf(e.attributeValue("meta").toUpperCase()), filter));
            } else {
                if (!StringUtils.isEmpty(filter)) {
                    attributes.add(new Tuple<Type, String>(Type.valueOf(type.toUpperCase()), filter));
                }
            }
        }

        return attributes;
    }

    public List<ChemTypes> getReactiveGroupIds() {
        List<ChemTypes> results = new ArrayList<>();

        for (Map.Entry<ChemTypes, Group> entry : this.groupMap.entrySet()) {
            results.add(entry.getKey());
        }
        return results;
    }

    private void buildFromSAX() throws Exception {
        LogManager.getLogger().warn("SAX building doesn't work quite yet, a group's reactions " +
                "are not populating correctly, using tree parsing");
        buildFromTree();
        // Set up the XML parser
        //SAXParser parser= SAXParserFactory.newInstance().newSAXParser();
        //EpaSaxParser handler = new EpaSaxParser();
        //parser.parse(this.config.epaDefinitions, handler);

        //this.groupMap = handler.getGroupMap();
        //this.reactionMap = handler.getReactionMap();
    }

    /**
     * Takes the XML input and builds the reaction groups of the corresponding reactive groups.
     *
     * @param node node of reaction groups
     *
     * @return hashmap of reactions
     */
    private Map<ChemTypes, Reaction> buildReactionGroups(Node node) {
        Map<ChemTypes, Reaction> results = new HashMap<>();
        List<Node> reactants = node.selectNodes("reaction");
        int reactantId;
        // Get each reaction in that reacts with the respective group
        for (Node reactant : reactants) {
            reactantId = Integer.parseInt(reactant.selectSingleNode("id").getText());
            // Get all the outcomes of a given reaction
            //Node outcomes = reactant.selectSingleNode("outcomes");
            // a place to store the consequences for each reaction
            List<Consequence> consequences = new ArrayList<Consequence>();
            // get the outcome(s) for each reaction that exists in the reactant
            for (Node outcome : (List<Node>) reactant.selectNodes("outcome")) {
                consequences.add(Consequence.valueOf(outcome.getText().toUpperCase()));
            }
            results.put(ChemTypes.getTypeFromId(reactantId), new Reaction(consequences));
        }
        return results;
    }

    public void initialize() {
        // this intentionally does nothing.
    }

    private void buildReactiveMatrix() {
        Logger logger = LogManager.getLogger(EpaManager.class);
        FileReader reader = new SimpleReader(config.getReactiveMatrix());

        String line;
        while ((line = reader.nextLine()) != null) {
            String[] coordinates = StringUtils.split(line, "|");
            String[] groupings = StringUtils.split(coordinates[2], "_");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            Set<Integer> groups = new HashSet<>();
            for (String s : groupings) {
                groups.add(Integer.parseInt(s));
            }
            this.reactionMatrix.put(x, y, groups);
        }
        reader.close();
    }

    /**
     * Test the mixing of a reaction, True: safe to mix False: unsafe to mix
     *
     * @param x category id of compound to mix
     * @param y category id of compound to mix
     *
     * @return true on safe to mix, otherwise false
     */
    public boolean test(int x, int y) {
        return test(ChemTypes.getTypeFromId(x), ChemTypes.getTypeFromId(y));
    }

    public boolean test(ChemTypes x, ChemTypes y) {
        if (this.config.isDebug()) {
            // logger.trace(String.format("Testing for: %s, %s", x, y));
        }

        try {
            return validate(x, y);
        } catch (CompatabilityException ce) {
            return false;
        }
    }

    public boolean test(BaseCompound one, BaseCompound two) {
        for (int x : (Set<Integer>) one.getReactiveGroups()) {
            for (int y : (Set<Integer>) two.getReactiveGroups()) {
                if (!test(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates whether a mixture is valid or not
     *
     * @param x category id of compound to mix
     * @param y category id of compound to mix
     *
     * @return boolean or exception as to the validity of a reaction
     */
    public boolean validate(ChemTypes x, ChemTypes y) {
        if (this.config.isDebug()) {
            // logger.trace(String.format("Testing for: %s, %s", x, y));
        }


        boolean throwException = false;
        if (this.reactionTable.get(x,y) != null) {
            Reaction reaction = this.reactionTable.get(x, y);
            // We are not ignoring warnings.
            if (config.getErrorLevel().warn()) {
                if (!reaction.getConsequences().isEmpty()) {
                    throwException = true;
                }
            } else {
                // We are ignoring warnings.
                Set<Consequence> consequences = new HashSet<>(reaction.getConsequences());
                // We are ignoring warnings, so we can simply remove them from the set.
                consequences.remove(Consequence.C);
                // If there is anything left, we should throw an error
                if (consequences.size() > 0) {
                    throwException = true;
                }
            }
        }


        throwException = false;
        if (throwException) {
            StringBuilder message = new StringBuilder();
            message.append("Combining: (").append(groupMap.get(x).groupId).append(") ")
                    .append(groupMap.get(x).groupName)
                    .append(" with: (").append(groupMap.get(y).groupId).append(") ")
                    .append(groupMap.get(y).groupName)
                    .append(" results in: ").append(this.reactionTable.get(x, y).toString());
            //logger.fatal(message.toString());
            throw new CompatabilityException(message.toString());
        }
        return true;
    }

    public boolean validate(int x, int y) {
        return validate(ChemTypes.getTypeFromId(x), ChemTypes.getTypeFromId(y));
    }

    public boolean validate(BaseCompound one, BaseCompound two) {
        for (int x : (Set<Integer>) one.getReactiveGroups()) {
            for (int y : (Set<Integer>) two.getReactiveGroups()) {
                if (!validate(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the reaction of two reactive groups
     *
     * @param x int, x coordinate on the matrix
     * @param y int, y coordinate on the matrix
     *
     * @return reaction object that details what could go wrong
     */
    public Reaction getReaction(int x, int y) {
        if (this.reactionTable.containsRow(x)) {
            return this.reactionTable.get(x, y);
        }
        return null;
    }

    public Reaction getReaction(ChemTypes a, ChemTypes b) {
        return this.getReaction(a.getValue(), b.getValue());
    }

    /**
     * toString overwrite
     *
     * @return string representation of object
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ChemTypes, Group> entry : groupMap.entrySet()) {
            sb.append("Id: ").append(entry.getKey()).append("\n");
            sb.append(entry.getValue().toString());
        }
        Map<ChemTypes, Map<ChemTypes, Reaction>> map = reactionTable.rowMap();
        for (ChemTypes row : map.keySet()) {
            sb.append("getId: ").append(row);
            sb.append(" has the following reactions: ").append(System.getProperty("line.separator"));
            Map<ChemTypes, Reaction> tmp = map.get(row);
            for (Map.Entry<ChemTypes, Reaction> pair : tmp.entrySet()) {
                sb.append(pair.getKey()).append(":\t").append(pair.getValue().toString());
            }
            sb.append(System.getProperty("line.separator"));
            sb.append("==========").append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    private String printMatrix() {
        StringBuilder sb = new StringBuilder("\n");
        /*
        for (Map.Entry<ChemTypes, HashMap<ChemTypes, Reaction>> outer : this.reactionMap.entrySet()) {
            sb.append(outer.getKey()).append("|\t");
            for (Map.Entry<ChemTypes, Reaction> inner : outer.getValue().entrySet()) {
                sb.append(inner.getKey()).append("\t");
            }
            sb.append("\n");
        }
        */
        return sb.toString();
    }

    public void buildEPAMap() {
    }

    public Set<ChemTypes> getFromComboTable() {
        return new HashSet<>();
    }

    public String printReactiveGroupAndIds() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ChemTypes, Group> entry : this.groupMap.entrySet()) {
            sb.append("put(\"").append(entry.getValue().groupName).append("\",").append(entry.getKey()).append(");\n");
        }
        return sb.toString();
    }

    public Set<ChemTypes> lookUp(Set<ChemTypes> types) {
        Set<ChemTypes> results = new HashSet<>();
        for (ChemTypes t1 : types) {
            for (ChemTypes t2 : types) {
                results.addAll(lookUp(t1, t2));
            }
        }
        return results;
    }

    public Set<ChemTypes> lookUp(ChemTypes a, ChemTypes b) {
        Set<ChemTypes> results = new HashSet<>();

        int x = a.getValue();
        int y = b.getValue();

        if (x > y) {
            int temp = x;
            x = y;
            y = temp;
        }

        if (this.reactionMatrix.get(x, y) != null) {
            for (int t : this.reactionMatrix.get(x, y)) {
                results.add(ChemTypes.getTypeFromId(t));
            }
        } else {
            results.add(ChemTypes.getTypeFromId(x));
            results.add(ChemTypes.getTypeFromId(y));
        }

        return results;
    }


    /**
     * Visibility of classifiers available to the system. SMARTS is the most accurate and complete, but
     * other options exists. SMARTS is the preferred method.
     */
    public enum Type {
        ELEMENT, WORD, SMILES, SMARTS
    }
}

