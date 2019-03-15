import it.unibo.bls.devices.gui.ButtonAsGui;
import it.unibo.blsFramework.concreteDevices.LedObserver;
import it.unibo.blsFramework.interfaces.ILedObserver;
import it.unibo.blsFramework.models.ButtonModel;
import it.unibo.blsFramework.models.LedModel;
import it.unibo.chain.segment7.LedSegmHorizontal;

import javax.swing.*;
import java.awt.*;

public class JavaMain {

    public static void main(String[] args) {
        //--------------create frame---------------
        JFrame f = new JFrame();
        f.setSize(600, 400);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(1, 2));
        f.setVisible(true);

        LedSegmHorizontal seg1 = new LedSegmHorizontal("seg1", 40, 20);
        LedSegmHorizontal seg2 = new LedSegmHorizontal("seg1", 20, 10);
        LedSegmHorizontal seg3 = new LedSegmHorizontal("seg1", 20, 10);
        LedSegmHorizontal seg4 = new LedSegmHorizontal("seg1", 20, 10);
        LedSegmHorizontal seg5 = new LedSegmHorizontal("seg1", 20, 10);

        f.add(seg1);
        f.add(seg2);
        f.add(seg3);
        f.add(seg4);
        f.add(seg5);

        //--------------set LED observers---------------
        ILedObserver obs1 = LedObserver.create();
        ILedObserver obs2 = LedObserver.create();
        ILedObserver obs3 = LedObserver.create();
        ILedObserver obs4 = LedObserver.create();
        ILedObserver obs5 = LedObserver.create();

        obs1.setLed(seg1);
        obs2.setLed(seg2);
        obs3.setLed(seg3);
        obs4.setLed(seg4);
        obs5.setLed(seg5);


        //--------------create actors---------------
        ChainLinkActor actor1 = new ChainLinkActor("actor1", 500);
        ChainLinkActor actor2 = new ChainLinkActor("actor2", 500);
        ChainLinkActor actor3 = new ChainLinkActor("actor3", 500);
        ChainLinkActor actor4 = new ChainLinkActor("actor4", 500);
        ChainLinkActor actor5 = new ChainLinkActor("actor5", 500);

        actor1.setLedModel(LedModel.createLed(obs1));
        actor2.setLedModel(LedModel.createLed(obs2));
        actor3.setLedModel(LedModel.createLed(obs3));
        actor4.setLedModel(LedModel.createLed(obs4));
        actor5.setLedModel(LedModel.createLed(obs5));

        //--------------chain actors---------------
        actor1.setNext(actor2.getChannel());

        actor2.setNext(actor3.getChannel());
        actor2.setPrev(actor1.getChannel());

        actor3.setNext(actor4.getChannel());
        actor3.setPrev(actor2.getChannel());

        actor4.setNext(actor5.getChannel());
        actor4.setPrev(actor3.getChannel());

        actor5.setNext(actor1.getChannel());
        actor5.setPrev(actor4.getChannel());

        //--------------set button observer---------------
        ButtonModel buttonModel = ButtonModel.createButton("click me");
        ButtonObserver buttonObserver = new ButtonObserver();
        buttonModel.addObserver(buttonObserver);
        ButtonAsGui.createButton("click me").addObserver(buttonModel);

        //--------------bind button observer to chain---------------
        buttonObserver.setControl(actor4.getChannel());

        System.out.println("test");
    }
}
