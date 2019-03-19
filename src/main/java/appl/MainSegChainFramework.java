package appl;

import applLogic.ChainLinkActor;
import interfaces.IChainActor;
import interfaces.ILedActorModel;
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
        ILedActorModel ledModel = LedActorModel.Companion.createLed(actor1.getName());
        actor1.setLedModel(ledModel.getChannel());
        chainSystem.addChainLink(actor1);
        chainSystem.addLedModel(ledModel);
        chainSystem.addConcreteLed(chainSystem.getLinkAt(0), new LedSegmentAdapter("seg1", 20, 10, 100, 0));

        //the creation of the LedModels can be left to the framework
        chainSystem.addChainLink(new ChainLinkActor("seg2", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg2", 20, 10, 250, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg3", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg3", 20, 10, 400, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg4", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg4", 20, 10, 550, 0));

        chainSystem.addChainLink(new ChainLinkActor("seg5", 500));
        chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg5", 20, 10, 700, 0));
    }

    public static void main(String[] args) {

        ISegChainFramework chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM");

        MainSegChainFramework.systemSetup(chainSystem);


        for (int k = 1; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                chainSystem.addChainLink(new ChainLinkActor("seg"+i+""+k, 500));
                chainSystem.addConcreteLed(chainSystem.getLastLink(), new LedSegmentAdapter("seg"+i+""+k, 20, 10,100+150*i,200*k));
                Utils.delay(1000);
            }
        }
        System.out.println("-----------------------TESTING START/STOP----------------------------");
        MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.startMsg(), chainSystem.getFirstLink().getChannel());
        Utils.delay(10000);
        MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.stoptMsg(), chainSystem.getFirstLink().getChannel());
    }


}
