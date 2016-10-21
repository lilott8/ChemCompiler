package DataFlow;

import CFGBuilder.BasicBlock;
import CFGBuilder.InstructionNode;
import executable.Executable;
import executable.instructions.Instruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chriscurtis on 10/19/16.
 */
abstract public class DataFlowNode {
    protected Integer __ID;
    protected Set<String> __gen;
    protected Set<String> __kill;
    protected List<String> __out;
    protected List<String> __in;

    public DataFlowNode(Integer id){
        __ID = id;
        __gen = null;
        __kill = null;
        __in = new ArrayList<String>();
        __out = new ArrayList<String>();
    }
    public DataFlowNode(BasicBlock bb) {
        __gen = new HashSet<String>();
        __kill = new HashSet<String>();
        __in = new ArrayList<String>();
        __out = new ArrayList<String>();
        __ID = bb.ID();


        for(InstructionNode i :bb.getInstructions()) {
            for(String out: i.Instruction().getOutputs().keySet())
                __gen.add(out);
        }



    }

}
