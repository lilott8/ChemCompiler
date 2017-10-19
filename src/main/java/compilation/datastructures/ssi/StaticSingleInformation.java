package compilation.datastructures.ssi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import compilation.datastructures.cfg.CFG;
import compilation.datastructures.dominatortree.PostDominatorTree;
import compilation.datastructures.ssa.StaticSingleAssignment;
import config.ConfigFactory;

/**
 * Created by chriscurtis on 4/10/17.
 */
public class StaticSingleInformation extends StaticSingleAssignment{
    private PostDominatorTree postDominatorTree;
    protected HashMap<String, HashSet<Integer>> sigmaPlacedAt;


   // HashMap<Integer, Integer> hasAlready = new HashMap<Integer, Integer>();

    public StaticSingleInformation(CFG controlFlowGraph){
        super(controlFlowGraph);
        postDominatorTree = new PostDominatorTree(controlFlowGraph);
        sigmaPlacedAt = new HashMap<>();

        createBasicBlockSymbolDefinitionAndUseTables();

        Boolean changesMade = true;
        while (changesMade){
            changesMade = this.placePhiNodes();
            if(this.PlaceSigmaNodes())
                changesMade = true;
        }
        this.renameVariables();
    }

    protected Boolean PlaceSigmaNodes(){
        return this.PlaceSigmaNodes(null);
    }

    protected Boolean PlaceSigmaNodes(Set<String> symbols) {
        Boolean changed = false;
        List<Integer> workList = new ArrayList<>();

        for (String symbol : this.basicBlockSymbolDefinitionTable.keySet()) {
            if (symbols != null && !symbols.contains(symbol) || !this.basicBlockSymbolUseTable.containsKey(symbol)) {
                continue;
            }

            for (Integer BBID : this.basicBlockSymbolUseTable.get(symbol)) {
                if (!workList.contains(BBID))
                    workList.add(BBID);
            }

            while (!workList.isEmpty()) {
                int basicBlockID = workList.get(0);
                workList.remove(0);
                for (int domFrontierBBID : this.postDominatorTree.getFrontier(basicBlockID)) {


                    if (!this.sigmaPlacedAt.containsKey(symbol) || !this.sigmaPlacedAt.get(symbol).contains(domFrontierBBID)) {
                        changed = true;
                        if (ConfigFactory.getConfig().isDebug()) {
                            logger.debug("Adding Sigma node for:" + symbol + " at Basic Block:" + domFrontierBBID);
                        }

                        this.basicBlocks.get(domFrontierBBID).addInstruction(new SigmaInstruction(symbol, this.getSuccessors(domFrontierBBID).size()));

                        //A_SIGMA[v] <- A_SIGMA[v] U {y}
                        if (this.sigmaPlacedAt.containsKey(symbol))
                            this.sigmaPlacedAt.get(symbol).add(domFrontierBBID);
                        else {
                            HashSet<Integer> IDS = new HashSet<Integer>();
                            IDS.add(domFrontierBBID);
                            this.sigmaPlacedAt.put(symbol, IDS);
                        }


                        for (Integer succ : this.basicBlockSuccessorSet.get(domFrontierBBID)) {
                            if (this.basicBlockSymbolDefinitionTable.containsKey(symbol))
                                this.basicBlockSymbolDefinitionTable.get(symbol).add(succ);
                            else {
                                HashSet<Integer> IDS = new HashSet<Integer>();
                                IDS.add(succ);
                                this.basicBlockSymbolDefinitionTable.put(symbol, IDS);
                            }
                        }


                        if (!this.basicBlockSymbolUseTable.containsKey(symbol) || !this.basicBlockSymbolUseTable.get(symbol).contains(domFrontierBBID)) {
                            if (this.basicBlockSymbolUseTable.containsKey(symbol))
                                this.basicBlockSymbolUseTable.get(symbol).add(domFrontierBBID);
                            else {
                                HashSet<Integer> IDS = new HashSet<Integer>();
                                IDS.add(domFrontierBBID);
                                this.basicBlockSymbolUseTable.put(symbol, IDS);
                            }

                            workList.add(domFrontierBBID);
                        }
                    }
                }

            }
        }
        return changed;
    }
}
