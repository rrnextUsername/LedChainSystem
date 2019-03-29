package appl;

import applLogic.ChainLinkActor;
import applLogic.StateMachineLinkActor;
import interfaces.IChainActor;
import interfaces.ILedActorModel;
import interfaces.ISegChainFramework;
import it.unibo.bls.utils.Utils;
import model.LedActorModel;
import segments.LedSegmentAdapter;

public class MainSegChainFramework extends SegChainFramework {

    public MainSegChainFramework(String cmdName) {
        super(cmdName);
    }

    public static void systemSetup(ISegChainFramework chainSystem, int delay) {


        //the first one is automatically set up for receiving button clicked messages
        IChainActor actor1 = new ChainLinkActor("seg1", delay);
        ILedActorModel ledModel = LedActorModel.Companion.createLed(actor1.getName());
        actor1.setLedModel(ledModel.getChannel());
        chainSystem.addChainLink(actor1);
        chainSystem.addLedModel(ledModel);
        chainSystem.addConcreteLed(chainSystem.getLinkAt(0), new LedSegmentAdapter("seg1", 20, 10, 1000, 0));

        //the creation of the LedModels can be left to the framework
        chainSystem.addChainLink(new ChainLinkActor("seg2", delay));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 1120, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg3", delay));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg3", 20, 10, 1240, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg4", delay));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg4", 20, 10, 1360, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg5", delay));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg5", 20, 10, 1480, 0));
    }

    public static void systemSetup(ISegChainFramework chainSystem) {
        systemSetup(chainSystem, 500);
    }

    public static void stateMachineSystemSetup(ISegChainFramework chainSystem, int delay) {
        chainSystem.addChainLink(new StateMachineLinkActor("seg1", delay, true));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg1", 20, 10, 1000, 0));

        //the creation of the LedModels can be left to the framework
        chainSystem.addChainLink(new StateMachineLinkActor("seg2", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 1120, 0));

        chainSystem.addChainLink(new StateMachineLinkActor("seg3", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg3", 20, 10, 1240, 0));

        chainSystem.addChainLink(new StateMachineLinkActor("seg4", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg4", 20, 10, 1360, 0));

        chainSystem.addChainLink(new StateMachineLinkActor("seg5", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg5", 20, 10, 1480, 0));
    }

    public static void stateMachineSystemSetup(ISegChainFramework chainSystem) {
        stateMachineSystemSetup(chainSystem, 500);
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        MainSegChainFramework.stateMachineSystemSetup(chainSystem);

        System.out.println("-----------------------TESTING START/STOP----------------------------");
        //MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE., chainSystem.getFirstLink().getChannel());
        Utils.delay(6000);
        // MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.stoptMsg(), chainSystem.getFirstLink().getChannel());
    }


}
