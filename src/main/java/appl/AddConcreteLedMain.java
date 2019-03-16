package appl;

import applLogic.ChainLinkActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;
import it.unibo.bls.devices.gui.LedAsGui;
import it.unibo.bls.utils.Utils;

public class AddConcreteLedMain extends MainSegChainFramework {
    public AddConcreteLedMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {
        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        systemSetup(chainSystem);
        System.out.println("-----------------------ADDING BUTTON----------------------------");
        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));


        Utils.delay(10000);
        System.out.println("-----------------------ADDING MORE CONCRETE LEDS----------------------------");
        chainSystem.addConcreteLed(chainSystem.getFirstlink(), LedAsGui.createLed());

        Utils.delay(2000);
        chainSystem.addChainLink(new ChainLinkActor("seg5", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), LedAsGui.createLed());
    }
}
