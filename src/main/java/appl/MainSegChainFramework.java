package appl;

import applLogic.ChainLinkActor;
import applLogic.StateMachineLinkActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.utils.Utils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import segments.LedSegmentAdapter;

public class MainSegChainFramework extends SegChainFramework {

    public MainSegChainFramework(String cmdName) {
        super(cmdName);
    }

    public static void systemSetup(ISegChainFramework chainSystem, int delay) {


        //the first one is automatically set up for receiving button clicked messages
        chainSystem.addChainLink(new ChainLinkActor("seg1", delay, true));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg1", 20, 10, 1000, 0));

        //the creation of the LedModels can be left to the framework
        chainSystem.addChainLink(new ChainLinkActor("seg2", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 1120, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg3", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg3", 20, 10, 1240, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg4", delay, false));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg4", 20, 10, 1360, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg5", delay, false));
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

        stateMachineSystemSetup(chainSystem);
        //MainSegChainFramework.systemSetup(chainSystem);


        for (int k = 1; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                chainSystem.addChainLink(new ChainLinkActor("seg"+i+""+k, 500));
                chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg"+i+""+k, 20, 10,100+150*i,200*k));
                Utils.delay(1000);
            }
        }
        System.out.println("-----------------------TESTING START/STOP----------------------------");
        MsgUtil.INSTANCE.sendMsg(new ApplMessage("ACTIVATE", "dispatch", "main", "buttonControl", "ACTIVATE", "0"), chainSystem.getFirstLink());
        Utils.delay(10000);
        MsgUtil.INSTANCE.sendMsg(new ApplMessage("DEACTIVATE", "dispatch", "main", "buttonControl", "DEACTIVATE", "0"), chainSystem.getFirstLink());
    }


}
