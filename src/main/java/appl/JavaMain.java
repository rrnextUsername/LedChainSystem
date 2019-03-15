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
        chainSystem.addChainLink(new ChainLinkActor("seg2", 500), new LedSegmentAdapter("seg2", 20, 10, 120, 0));
        chainSystem.addChainLink(new ChainLinkActor("seg3", 500), new LedSegmentAdapter("seg3", 20, 10, 240, 0));
        chainSystem.addChainLink(new ChainLinkActor("seg4", 500), new LedSegmentAdapter("seg4", 20, 10, 360, 0));
        chainSystem.addChainLink(new ChainLinkActor("seg5", 500), new LedSegmentAdapter("seg5", 20, 10, 480, 0));


        Utils.delay(5000);

        for (int k = 1; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                chainSystem.addChainLink(new ChainLinkActor("seg" + i + "" + k, 500), new LedSegmentAdapter("seg2", 20, 10, i * 120, k * 100));
                Utils.delay(10000);
            }
        }
    }
}
