package appl;

import applLogic.StateMachineLinkActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;
import it.unibo.bls.utils.Utils;
import segments.LedSegmentAdapter;

public class AddLinksMain extends MainSegChainFramework {
    public AddLinksMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {
        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        stateMachineSystemSetup(chainSystem);
        System.out.println("-----------------------ADDING BUTTON----------------------------");
        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));

        Utils.delay(2000);
        System.out.println("-----------------------ADDING MORE LINKS----------------------------");

        for (int k = 1; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                chainSystem.addChainLink(new StateMachineLinkActor("seg" + i + "" + k, 500, false));
                chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg" + i + "" + k, 20, 10, 1000 + 120 * i, 100 * k));
                Utils.delay(2000);
            }
        }
    }
}
