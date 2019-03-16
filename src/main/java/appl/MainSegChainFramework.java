package appl;

import applLogic.ChainLinkActor;
import interfaces.IChainActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.utils.Utils;
import it.unibo.kactor.MsgUtil;
import model.LedActorModel;
import segments.LedSegmentAdapter;

public class MainSegChainFramework extends SegChainFramework {

    public MainSegChainFramework(String cmdName) {
        super(cmdName);
    }

    public static void systemSetup(ISegChainFramework chainSystem) {


        //the first one is automatically set up for receiving button clicked messages
        IChainActor actor1 = new ChainLinkActor("seg1", 500);
        actor1.setLedModel(LedActorModel.Companion.createLed(actor1.getName()));
        chainSystem.addChainLink(actor1);
        chainSystem.addConcreteLed(chainSystem.getLinkAt(0), new LedSegmentAdapter("seg1", 20, 10, 1000, 0));


        //the creation of the LedModels can be left to the framework
        chainSystem.addChainLink(new ChainLinkActor("seg2", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 1120, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg3", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg3", 20, 10, 1240, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg4", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg4", 20, 10, 1360, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg5", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg5", 20, 10, 1480, 0));
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        MainSegChainFramework.systemSetup(chainSystem);

        System.out.println("-----------------------TESTING START/STOP----------------------------");
        MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.startMsg(), chainSystem.getFirstlink().getChannel());
        Utils.delay(6000);
        MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.stoptMsg(), chainSystem.getFirstlink().getChannel());
    }


}
