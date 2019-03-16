package appl;

import applLogic.ChainLinkActor;
import interfaces.IChainActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.devices.gui.ButtonAsGui;
import it.unibo.bls.devices.gui.LedAsGui;
import it.unibo.bls.utils.Utils;
import model.LedActorModel;
import segments.LedSegmentAdapter;

public class JavaMain extends SegChainFramework {

    public JavaMain(String cmdName) {
        super(cmdName);
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"));

        //the first one is automatically set up for receiving button clicked messages
        IChainActor actor1= new ChainLinkActor("seg1", 500);
        actor1.setLedModel(LedActorModel.Companion.createLed(actor1.getName()));
        chainSystem.addChainLink(actor1);
        chainSystem.addConcreteLed(chainSystem.getLinkAt(0), new LedSegmentAdapter("seg1", 20, 10,1000,0));



        //the creation of the LedModels can be left to the framework
        chainSystem.addChainLink(new ChainLinkActor("seg2", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 1120, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg3", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg3", 20, 10, 1240, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg4", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg4", 20, 10, 1360, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg5", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg5", 20, 10, 1480, 0));



        Utils.delay(5000);
        System.out.println("-----------------------ADDING MORE LINKS----------------------------");

        for (int k = 1; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                chainSystem.addChainLink(new ChainLinkActor("seg"+i+""+k, 500));
                chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg"+i+""+k, 20, 10,1000+120*i,100*k));
                Utils.delay(2000);
            }
        }

        chainSystem.setButtonControl(chainSystem.getLinkAt(3));
        System.out.println("-----------------------CONTROL LINK CHANGED----------------------------");

        for (int k = 1; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                chainSystem.addChainLink(new ChainLinkActor("seg"+i+""+k, 500));
                chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg"+i+""+k, 20, 10,1000+120*i,200*k));
                Utils.delay(2000);
            }
        }

        System.out.println("-----------------------ADDING MORE CONCRETE LEDS----------------------------");
        chainSystem.addConcreteLed(chainSystem.getFirstlink(), LedAsGui.createLed());

        chainSystem.addChainLink(new ChainLinkActor("seg5", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), LedAsGui.createLed());
    }
}
