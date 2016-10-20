package CFGBuilder;

import ChemicalInteractions.ChemicalInteraction;
import com.sun.org.apache.xpath.internal.operations.Bool;
import executable.instructions.Instruction;
/**
 * Created by chriscurtis on 9/28/16.
 */
public class InstructionNode {
    private Integer __ID;
    private Instruction __instruction;
    private ChemicalInteraction __interaction;
    private Boolean __leader;

    public InstructionNode(Integer id, Instruction instruction) {
        __ID = id;
        __instruction = instruction;
        __interaction = null;
        __leader = false;
    }
    public InstructionNode(Integer id, Instruction instruction, Boolean isLeader) {
        __ID = id;
        __instruction = instruction;
        __interaction = null;
        __leader = isLeader;
    }

    public Integer ID() {
        return __ID;
    }

    public Instruction Instruction() {
        return __instruction;
    }
    public ChemicalInteraction getChemicalInteraction() {return __interaction; }

    public void addChemicalInteraction(ChemicalInteraction ci) { __interaction = ci; }
    public void setLeader(Boolean isleader) { __leader = isleader; }

    public Boolean isLeader() { return __leader; }

    public String toString() {
        return this.toString("");
    }
    public String toString(String indentBuffer) {
        return indentBuffer + __ID.toString() + ":" + __instruction.getName();
    }

}
