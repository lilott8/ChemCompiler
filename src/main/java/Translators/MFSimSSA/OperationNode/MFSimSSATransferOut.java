package Translators.MFSimSSA.OperationNode;


import executable.instructions.Store;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chriscurtis on 10/28/16.
 */
public class MFSimSSATransferOut extends MFSimSSANode{
    private static final Logger logger = LogManager.getLogger(MFSimSSATransferOut.class);

    public MFSimSSATransferOut(Integer id, String name) {
        super(id, OperationClassifier.TRANSFER_OUT, name);

    }

    public String toString() {
        String ret = "NODE (" + this.__nodeID + ", " + this.__opType + ", "  + this.__nodeName + ")\n";

        for (Integer successor : this.__successorIDs) {
            ret += "EDGE (" + this.__nodeID + ", " + successor + ")\n";
        }
        return ret;
    }
}
