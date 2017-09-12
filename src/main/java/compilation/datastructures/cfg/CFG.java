package compilation.datastructures.cfg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chemicalInteractions.ChemicalResolution;
import compilation.datastructures.basicblock.BasicBlock;
import compilation.datastructures.basicblock.BasicBlockEdge;
import compilation.datastructures.InstructionNode;
import compilation.symboltable.NestedSymbolTable;
import executable.instructions.Instruction;
import substance.Chemical;
import substance.Substance;
import variable.Instance;
import variable.Variable;

/**
 * Created by chriscurtis on 9/29/16.
 */
public class CFG implements Serializable {
    public static final Logger logger = LogManager.getLogger(CFG.class);



    protected LinkedHashMap<Integer, BasicBlock> __basicBlocks;
    protected BasicBlock __entry;
    protected BasicBlock __exit;
    protected ArrayList<BasicBlockEdge> __edges;
    protected HashMap< Integer, Set<Integer>> __basicBlockPredecessorSet;
    protected HashMap< Integer, Set<Integer>> __basicBlockSuccessorSet;

    protected NestedSymbolTable __symbolTable;

    protected Integer __UniqueIDs;
    protected Integer __ID;

    private void initializeData(){
        __basicBlocks = new LinkedHashMap<Integer, BasicBlock>();
        __edges = new ArrayList<BasicBlockEdge>();
        __symbolTable = new NestedSymbolTable();

        __ID = 0;
        __UniqueIDs = __ID++;


        __basicBlockPredecessorSet = new HashMap<Integer, Set<Integer>>();
        __basicBlockSuccessorSet = new HashMap<Integer, Set<Integer>>();

    }


    public CFG(){
        initializeData();

    }
    public CFG(CFG controlFlowGraph){
//        initializeData();
        this.__basicBlocks = controlFlowGraph.__basicBlocks;
        this.__edges = controlFlowGraph.__edges;
        this.__basicBlockPredecessorSet = controlFlowGraph.__basicBlockPredecessorSet;
        this.__basicBlockSuccessorSet = controlFlowGraph.__basicBlockSuccessorSet;
        this.__symbolTable = controlFlowGraph.__symbolTable;
        this.__UniqueIDs = controlFlowGraph.__UniqueIDs;
        this.__ID = controlFlowGraph.__ID;
        this.__entry = controlFlowGraph.__entry;
        this.__exit = controlFlowGraph.__exit;


    }
    public CFG(Integer id){
        initializeData();
        __ID = id;
        __UniqueIDs = __ID++;
    }
    public CFG(Integer id, NestedSymbolTable table){
        initializeData();
        __symbolTable = table;
        __ID = id;
        __UniqueIDs = __ID++;
    }



    public Integer getID() { return __ID; }

    public Integer getNewID() { return __UniqueIDs++;}


    private void AddBasicBlock(BasicBlock block) {
        __basicBlocks.put(block.ID(), block);
    }
    public void SetEntry(BasicBlock entry) { this.__entry = entry; }
    public BasicBlock GetEntry() { return __entry; }
    public void SetExit(BasicBlock exit) { this.__exit = exit; }
    public BasicBlock GetExit() { return __exit; }

    public BasicBlock newBasicBlock() {
        NestedSymbolTable newTable = new NestedSymbolTable();
        newTable.setParent(__symbolTable);
        BasicBlock ret = new BasicBlock(this.getNewID(), newTable);
        this.AddBasicBlock(ret);

        return ret;
    }

    public void newBasicBlock(BasicBlock bb) {
        this.AddBasicBlock(bb);
    }


    public void insertInstructionNode(BasicBlock bb, Instruction instruction, Boolean isLeader) {
        InstructionNode node = new InstructionNode(this.getNewID(),instruction,isLeader);
        bb.addInstruction(node);
    }



    public void addEdge(BasicBlock source, BasicBlock destination) {
        this.addEdge(source,destination,"UNCONDITIONAL");
    }

    public void addEdge(BasicBlock source, BasicBlock destination, String condition) {
        __edges.add(new BasicBlockEdge(source.ID(),destination.ID(), condition));
        this.addPredecessor(source,destination);
        this.addSuccessor(source,destination);
    }

    public void addEdge(BasicBlock source, BasicBlock destination, String condition, String name) {
        __edges.add(new BasicBlockEdge(source.ID(),destination.ID(), condition, name));
        this.addPredecessor(source,destination);
        this.addSuccessor(source,destination);
    }

    private void addPredecessor(BasicBlock source, BasicBlock destination){
        Set predecessorSet;
        if (__basicBlockPredecessorSet.containsKey(destination.ID())){
            predecessorSet = __basicBlockPredecessorSet.get(destination.ID());
        }
        else
            predecessorSet = new HashSet();
        predecessorSet.add(source.ID());
        __basicBlockPredecessorSet.put(destination.ID(),predecessorSet);
    }
    private void addSuccessor(BasicBlock source, BasicBlock destination) {
        Set successorSet;

        if (__basicBlockSuccessorSet.containsKey(source.ID())){
            successorSet = __basicBlockSuccessorSet.get(source.ID());
        }
        else
            successorSet = new HashSet();
        successorSet.add(destination.ID());
        __basicBlockSuccessorSet.put(source.ID(),successorSet);
    }





    public void addResolution(String key, Variable variable, Boolean isGlobal){
        ChemicalResolution resolution = ResolveVariable(variable);
        resolution.setisGlobal(isGlobal);
        if (variable instanceof Instance) {
            resolution.setIsStationary(((Instance)variable).getIsStationary());
        }
        __symbolTable.put(key,resolution);
    }

    public ChemicalResolution ResolveVariable(Variable variable) {
        if(__symbolTable.contains(variable.getID()))
            return __symbolTable.get(variable.getID());

        ChemicalResolution resolution = new ChemicalResolution(variable.getName());
        if(variable instanceof Instance) {
            //logger.info("Found Instance Literal");
            resolution.setIsLiteral(false);
        }

        // for(Substance v : variable.getSubstance().values()) {
        //     resolution.addReference(ResolveSubstance(v));
        // }
        return resolution;
    }

    private ChemicalResolution ResolveSubstance(Substance substance){
        if(__symbolTable.contains(substance.getName()))
            return __symbolTable.get(substance.getName());

        ChemicalResolution resolution = new ChemicalResolution(substance.getName());
        resolution.setIsLiteral(true);
        for(Chemical c: substance.getChemicals().values())
            resolution.addLiteral(c);

        __symbolTable.put(substance.getName(),resolution);
        return resolution;
    }


    public NestedSymbolTable getSymbolTable() { return __symbolTable; }
    public void setSymbolTable(NestedSymbolTable table) { __symbolTable = table; }

    public LinkedHashMap<Integer, BasicBlock> getBasicBlocks() { return __basicBlocks; }
    public BasicBlock getBasicBlock(Integer id) {
        return this.__basicBlocks.get(id);
    }
    public BasicBlock getBasicBlockByInstructionID(Integer id) {
        for (BasicBlock bb: __basicBlocks.values()) {
            if (bb.containsInstruction(id)) {
                return bb;
            }
        }
        return null;
    }

    public List<BasicBlockEdge> getBasicBlockEdges() { return __edges; }
    public HashMap< Integer, Set<Integer>> getPredecessorTable() { return this.__basicBlockPredecessorSet; }
    public Set<Integer> getPredecessors(Integer basicBlockID) {
        return __basicBlockPredecessorSet.get(basicBlockID);
    }
    public Boolean hasPredecessors(Integer basicBlockID) {
        return __basicBlockPredecessorSet.containsKey(basicBlockID);
    }

    public Set<Integer> getSuccessors(Integer basicBlockID) {
        return __basicBlockSuccessorSet.get(basicBlockID);
    }

    public HashMap< Integer, Set<Integer>>  getSuccessorTable() { return __basicBlockSuccessorSet; }

    public String toString(){
        return this.toString("");
    }

    public String toString(String indentBuffer){
        String ret = indentBuffer + "CFG: \n";
        for(BasicBlock bb: this.__basicBlocks.values()) {
            ret += bb.toString(indentBuffer+'\t') + '\n';
        }
        ret +=indentBuffer + "CFG Edges: \n";
        for(BasicBlockEdge edge: __edges) {
            ret += edge.toString(indentBuffer+'\t') + '\n';
        }

        ret+="\n SYMBOL TABLE\n";
        ret+=__symbolTable.toString();
        return ret;
    }

    public String toJSONString(){
        List<InstructionNode> instructions = new ArrayList<InstructionNode>();
        Map<Integer, Set<Integer>> childern = new HashMap<Integer, Set<Integer>>();

        for(BasicBlock bb : this.__basicBlocks.values()){
            for(InstructionNode node : bb.getInstructions()){
                instructions.add(node);
            }
        }


        String ret = "";

        return ret;
    }
}