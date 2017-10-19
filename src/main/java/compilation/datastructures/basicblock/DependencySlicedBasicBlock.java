package compilation.datastructures.basicblock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import compilation.datastructures.InstructionEdge;
import compilation.datastructures.InstructionNode;
import compilation.datastructures.ssa.PHIInstruction;
import compilation.datastructures.ssa.StaticSingleAssignment;
import compilation.datastructures.ssi.SigmaInstruction;

import java.util.*;

/**
 * Created by Tyson on 4/13/17.
 * Extends a basicblock with data dependence edges between instructions
 */
public class DependencySlicedBasicBlock extends BasicBlock{

    public static final Logger logger = LogManager.getLogger(DependencySlicedBasicBlock.class);

    public DependencySlicedBasicBlock(BasicBlock bb, StaticSingleAssignment CFG) {
        //copy bb object
        super(bb);

        //don't care about entry/exit block dependencies
        if (CFG.getEntry() == bb) {
            CFG.setEntry(this);
            return;
        } else if (CFG.getExit() == bb) {
            CFG.setExit(this);
            return;
        }

        //process updates for data dependency edges
        ArrayList<InstructionEdge> instructionEdges = new ArrayList<InstructionEdge>();
        ArrayList<InstructionNode> instructions = new ArrayList<InstructionNode>();
        for (Integer index = 0; index < bb.getInstructions().size(); ++index) {
            InstructionNode instruction = bb.getInstructions().get(index);
            if (instruction instanceof PHIInstruction || instruction instanceof SigmaInstruction)
                continue;
            if (instruction.getId() != -1)
                instructions.add(instruction);
            processInstructionOutput(instruction, instructionEdges, bb, index, CFG);
            processInstructionInput(instruction, instructionEdges, bb, index, CFG);
        }



        // if A->B, A->C and B->C then A->C is redundant
        removeRedundancies(instructionEdges, bb, CFG);
        //ProcessTransfers(CFG);

        this.getEdges().clear();
        this.getEdges().addAll(instructionEdges);

        Integer index = 0;
        if (instructions.size() > 0) {
            index = instructions.size()-1;
            for (String exitKey : instructions.get(index).getOutSet()) {
                CFG.getBasicBlock(bb.ID()).getBasicBlockExitTable().put(exitKey, new HashSet<Integer>(instructions.get(index).getId()));
            }
        }



    }

    public static void getInOutSets(StaticSingleAssignment CFG) {
        HashMap<BasicBlock, InstructionNode> firstInstructions = new LinkedHashMap<BasicBlock, InstructionNode>();
        HashMap<BasicBlock, InstructionNode> lastInstructions = new LinkedHashMap<BasicBlock, InstructionNode>();
        ArrayList<InstructionNode> instructions = new ArrayList<InstructionNode>();
        Integer last = 0;
        for (BasicBlock bb : CFG.getBasicBlocks().values()) {
            instructions.addAll(bb.getInstructions());
            last = bb.ID();
        }

        for (BasicBlock bb : CFG.getBasicBlocks().values()) {
            firstInstructions.put(bb, bb.getInstructions().get(0));
            for (InstructionNode i : bb.getInstructions()) {
                if (!(i instanceof SigmaInstruction) && !(i instanceof PHIInstruction)) {
                    firstInstructions.put(bb, i);
                    break;
                }
            }
            lastInstructions.put(bb, bb.getInstructions().get(bb.getInstructions().size()-1));
            for (Integer index = bb.getInstructions().size()-1; index >-1; index--) {
                InstructionNode i = bb.getInstructions().get(index);
                if (!(i instanceof SigmaInstruction) && !(i instanceof PHIInstruction)) {
                    lastInstructions.put(bb, i);
                    break;
                }
            }
        }
        firstInstructions.remove(CFG.getEntry());
        firstInstructions.remove(CFG.getExit());
        lastInstructions.remove(CFG.getExit());
        lastInstructions.remove(CFG.getEntry());

        //ephemeral instructions from entry node
        for (Integer i = 0; i < CFG.getBasicBlocks().get(0).getInstructions().size(); i++) {
            instructions.remove(0);
        }
        //ephemeral instructions from exit node
        for (Integer i = 0; i < CFG.getBasicBlock(last).getInstructions().size(); i++) {
            instructions.remove(instructions.size()-1);
        }

        //ephemeral phi and sigma instructions
        for (Integer i = instructions.size()-1; i > 0; i--) {
            InstructionNode instr = instructions.get(i);
            if (instr instanceof PHIInstruction || instr instanceof SigmaInstruction) {
                instructions.remove(instr);
            }
        }


        for (Integer index = 0; index < instructions.size(); ++index) {
            InstructionNode instr = instructions.get(index);
            //if phi or sigma, remove from
            try {
                ArrayList<LinkedHashSet<String>> def_use = new ArrayList<LinkedHashSet<String>>();
                LinkedHashSet<String> _defs = new LinkedHashSet<String>();
                LinkedHashSet<String> _uses = new LinkedHashSet<String>();
                if (instr.getInstruction() != null) {
                    for (String def : instr.getInstruction().getOutputs().keySet()) {
                        _defs.add(def);
                    }
                    for (String use : instr.getInstruction().getInputs().keySet()) {
                        _uses.add(use);
                    }
                }
                else {
                    if (instr instanceof PHIInstruction) {
                        _defs.add(((PHIInstruction) instr).getOriginalName());
                        _uses.add(((PHIInstruction) instr).getOriginalName());
                    }
                    else if (instr instanceof SigmaInstruction) {
                        _defs.add(instr.getInputSymbols().get(0));
                        _defs.add(instr.getInputSymbols().get(0));
                    }
                    else {
                        logger.error("error");
                    }
                }

                def_use.add(_defs);
                def_use.add(_uses);
                instr.setDef(def_use.remove(0));
                instr.setUse(def_use.remove(0));
                for (String var : instr.getUse()) {
                    if (!CFG.getEntry().getDefinitions().contains(var))
                        instr.getInSet().add(var);
                }


                if (index != 0) {
                    instr.getPred().add(instructions.get(index - 1));
                    instructions.get(index - 1).getSucc().add(instr);
                }
                if (index <= instructions.size() - 2) {
                    instr.getSucc().add(instructions.get(index + 1));
                    instructions.get(index + 1).getPred().add(instr);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Integer index = instructions.size() - 1; index > -1; index--) {
                InstructionNode instr = instructions.get(index);
                if (instr instanceof PHIInstruction || instr instanceof SigmaInstruction) {
                    continue;
                }

                //OUT[n] = U {s in succ(n)} IN[s]
                for (InstructionNode succ : instr.getSucc()) {
                    for (String var : succ.getInSet()) {
                        if (!(instr.getOutSet().contains(var))) {
                            instr.getOutSet().add(var);
                            changed = true;
                        }
                    }
                }

                //IN[n] = USE[n] U (OUT[n] - DEF[n])
                for (String var : instr.getUse()) {
                    if (!(instr.getInSet().contains(var))) {
                        if (!CFG.getEntry().getDefinitions().contains(var)) {
                            instr.getInSet().add(var);
                            changed = true;
                        }
                    }
                }
                for (String var : instr.getOutSet()) {
                    if (!(instr.getDef().contains(var))) {
                        if (!(instr.getInSet().contains(var))) {
                            if (!CFG.getEntry().getDefinitions().contains(var)) {
                                instr.getInSet().add(var);
                                changed = true;
                            }
                        }
                    }
                }

            }
        }
        instructions.get(0).getInSet().clear();

        //Get Dispense symbols
        for (InstructionNode instruction : instructions) {
            for (String instructionInput : instruction.getInstruction().getInputs().keySet()) {
                if (CFG.getEntry().getDefinitions().contains(instructionInput)) {
                    instruction.addImplicitDispense(instructionInput);
                }
            }
        }


        for (BasicBlock bb: CFG.getBasicBlocks().values()) {
            InstructionNode firstInstruction = firstInstructions.get(bb);
            InstructionNode lastInstruction = lastInstructions.get(bb);

            if (bb == CFG.getEntry() || bb == CFG.getExit()) {
                continue;
            }
            //get transfer ins
            for (String in : firstInstruction.getInSet()) {
                if (bb != null && bb != CFG.getEntry() && bb != CFG.getExit()) {
                    if (!bb.getBasicBlockEntryTable().containsKey(in)) {
                        bb.getBasicBlockEntryTable().put(in, new HashSet<Integer>());
                    }
                    bb.getBasicBlockEntryTable().get(in).add(firstInstruction.getId());
                }
            }

            if (bb.getBasicBlockEntryTable().isEmpty()) {
                if (firstInstruction.isLeader()) {
                    //do nothing
                } else {
                    //check Exit table for bb's leading to this bb, transfer in and out those variables
                    for (BasicBlockEdge edge : CFG.getBasicBlockEdges()) {
                        if (edge.getDestination() == bb.ID()) {
                            for (String var : CFG.getBasicBlock(edge.getSource()).getBasicBlockExitTable().keySet()) {
                                if (!bb.getBasicBlockEntryTable().containsKey(var)) {
                                    bb.getBasicBlockEntryTable().put(var, new HashSet<Integer>());
                                }
                                bb.getBasicBlockEntryTable().get(var).add(firstInstruction.getId());
                                if (!bb.getBasicBlockExitTable().containsKey(var)) {
                                    bb.getBasicBlockExitTable().put(var, new HashSet<Integer>());
                                }
                                bb.getBasicBlockExitTable().get(var).add(firstInstruction.getId());
                            }
                        }
                    }
                }
            }

            //get transfer outs
            for (String out : lastInstruction.getOutSet()) {
                if (bb != null && bb != CFG.getEntry() && bb != CFG.getExit()) {
                    if (!bb.getBasicBlockExitTable().containsKey(out)) {
                        bb.getBasicBlockExitTable().put(out, new HashSet<Integer>());
                    }
                    bb.getBasicBlockExitTable().get(out).add(lastInstruction.getId());
                }
            }
        }
    }

    private void processInstructionInput(InstructionNode instruction, ArrayList<InstructionEdge> update, BasicBlock bb, Integer index, StaticSingleAssignment CFG) {

        // loop through all inputs (reads)
        for (String inputKey : instruction.getInputSymbols()) {


            for (Integer successorIndex=index+1; successorIndex < bb.getInstructions().size(); ++successorIndex) {
                InstructionNode successor = bb.getInstructions().get(successorIndex);
                //check if input is used by successor
                if (successor.getInputSymbols().contains(inputKey) && successor.getId() != -1) {
                    // input of successor matches input of predecessor, RAR dependency
                    update.add(new InstructionEdge(instruction.getId(), successor.getId()));
                }
            }
        }
    }

    private void processInstructionOutput(InstructionNode instruction, ArrayList<InstructionEdge> update, BasicBlock bb, Integer index, StaticSingleAssignment CFG) {
        // loop through all outputs (writes)
        Set<Integer> exitTable = new HashSet<Integer>();

        for (String outputKey : instruction.getOutputSymbols()) {
            exitTable.add(instruction.getId());

            // currently putting each instructions outputs into the exit table for the basic block,
            // not necesarily needed, only outputs from last instruction needed
            //bb.getBasicBlockExitTable().put(outputKey, exitTable);

            for (Integer successorIndex=index+1; successorIndex < bb.getInstructions().size(); ++successorIndex) {
                InstructionNode successor = bb.getInstructions().get(successorIndex);
                //check if output is used by successor (read)
                if (successor.getInputSymbols().contains(outputKey) && successor.getId() != -1) {
                    // input of successor matches output of predecessor, RAW dependency
                    update.add(new InstructionEdge(instruction.getId(), successor.getId()));
                }
            }
        }
    }


    //removes redundant transitive edges (A->B, A->{C, D, ...}, B->{C, D, ...} :: A->{C, D...} is redundant)
    private void removeRedundancies(ArrayList<InstructionEdge> update, BasicBlock bb, StaticSingleAssignment CFG) {
        LinkedHashMap<Integer, LinkedHashSet<Integer>> sourceDestsMap = new LinkedHashMap<Integer, LinkedHashSet<Integer>>();

        //populate hashmap
        for (InstructionEdge edge : update) {
            if (!sourceDestsMap.containsKey(edge.getSource())) {
                sourceDestsMap.put(edge.getSource(), new LinkedHashSet<Integer>());
            }
            sourceDestsMap.get(edge.getSource()).add(edge.getDestination());
        }


        HashMap<Integer, LinkedHashSet<Integer>> remove = new HashMap<Integer, LinkedHashSet<Integer>>();
        //A->
        for (Integer source : sourceDestsMap.keySet()) {
            //{B, ...}
            LinkedHashSet<Integer> dests = sourceDestsMap.get(source);
            //B->
            for (Integer dest : dests) {
                //{C, ...}
                LinkedHashSet<Integer> dest_dests = sourceDestsMap.get(dest);
                if (dest_dests != null) {
                    for (Integer dest_dest : dest_dests) {
                        //check for redundancy
                        if (dests.contains(dest_dest)) {
                            if (!remove.containsKey(source)) {
                                remove.put(source, new LinkedHashSet<Integer>());
                            }
                            remove.get(source).add(dest_dest);
                        }
                    }
                }
            }
        }

        //remove redundancies from sourceDests map
        for (Integer source : remove.keySet()) {
            sourceDestsMap.get(source).removeAll(remove.get(source));
        }

        //update instructionEdge list
        for (Integer i = update.size()-1; i > -1; i--) {
            Integer source = update.get(i).getSource();
            if (!sourceDestsMap.get(source).contains(update.get(i).getDestination())) {
                update.remove(i.intValue());
            }
        }

        //remove repeat edges
        for (Integer i = 0; i < update.size(); i++) {
            for (Integer j = i+1; j < update.size(); j++) {
                if (update.get(i).equals(update.get(j))) {
                    update.remove(j.intValue());
                    j--;
                }
            }
        }

    }

    private ArrayList<Integer> getDestinations(Integer source, ArrayList<InstructionEdge> edges) {
        ArrayList<Integer> destinations = new ArrayList<Integer>();
        for (Integer edgeIndex = 0; edgeIndex < edges.size(); ++edgeIndex) {
            InstructionEdge edge = edges.get(edgeIndex);
            if (!(edge.getSource().equals(source))) {
                continue;
            }
            destinations.add(edge.getDestination());
            //get all destinations of source
            for (Integer destIndex = edgeIndex+1; destIndex < edges.size(); ++destIndex) {
                InstructionEdge destNode = edges.get(destIndex);
                if (destNode.getSource().equals(source)) {
                    ++edgeIndex;
                    destinations.add(destNode.getDestination());
                }
            }
        }
        return destinations;
    }
}