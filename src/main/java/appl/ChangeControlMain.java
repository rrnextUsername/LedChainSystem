package appl;

import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;
import it.unibo.bls.utils.Utils;

public class ChangeControlMain extends MainSegChainFramework {
    public ChangeControlMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {
        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        stateMachineSystemSetup(chainSystem);
        System.out.println("-----------------------ADDING BUTTON----------------------------");
        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));

        Utils.delay(5000);
        System.out.println("-----------------------CONTROL LINK CHANGED----------------------------");
        chainSystem.addButtonObserver(chainSystem.getLinkAt(3));

    }
}
