package appl;

import applLogic.SingleLedActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;
import segments.LedSegmentAdapter;

public class OneLinkMain extends MainSegChainFramework {
    public OneLinkMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        chainSystem.addChainLink(new SingleLedActor("seg1", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 320, 0));

        System.out.println("-----------------------ADDING BUTTON----------------------------");
        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));
    }
}
