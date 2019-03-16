package appl;

import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;

public class ButtonMain extends MainSegChainFramework {
    public ButtonMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        systemSetup(chainSystem);

        System.out.println("-----------------------ADDING BUTTON----------------------------");
        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));
    }
}
