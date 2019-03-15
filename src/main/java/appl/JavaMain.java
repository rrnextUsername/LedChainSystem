package appl;

import applLogic.ChainLinkActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;
import it.unibo.bls.utils.Utils;
import segments.LedSegmentAdapter;

public class JavaMain extends SegChainFramework {

    public JavaMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));

        chainSystem.addChainLink(new ChainLinkActor("seg1", 500), new LedSegmentAdapter("seg1", 20, 10));
        chainSystem.addChainLink(new ChainLinkActor("seg2", 500), new LedSegmentAdapter("seg2", 20, 10, 300, 0));
        chainSystem.addChainLink(new ChainLinkActor("seg3", 500), new LedSegmentAdapter("seg3", 20, 10, 600, 0));
        chainSystem.addChainLink(new ChainLinkActor("seg4", 500), new LedSegmentAdapter("seg4", 20, 10, 900, 0));
        chainSystem.addChainLink(new ChainLinkActor("seg5", 500), new LedSegmentAdapter("seg5", 20, 10, 1200, 0));


        Utils.delay(5000);

        for (int k = 1; k < 6; k++) {
            for (int i = 0; i < 5; i++) {
                Utils.delay(800);
                chainSystem.addChainLink(new ChainLinkActor("seg2", 500), new LedSegmentAdapter("seg2", 20, 10, i * 300, k * 100));
            }
        }
    }
}
