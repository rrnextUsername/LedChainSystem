package appl;

import interfaces.IChainActor;
import interfaces.ISegChainFramework;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.bls.utils.Utils;
import it.unibo.blsFramework.concreteDevices.LedObserver;
import it.unibo.blsFramework.interfaces.IButtonModel;
import it.unibo.blsFramework.interfaces.ILedObserver;
import it.unibo.blsFramework.models.ButtonModel;
import it.unibo.blsFramework.models.LedModel;
import listener.ButtonObserver;

import java.util.ArrayList;
import java.util.List;

public class SegChainFramework implements ISegChainFramework {

    public static SegChainFramework createTheSystem(String cmdName) {
        return new SegChainFramework(cmdName);
    }

    private final String cmdName;
    private List<IChainActor> chain;
    private ButtonObserver buttonObserver;
    private ButtonModel buttonModel;
    private IObservable concreteButton;


    public SegChainFramework(String cmdName) {
        Utils.showSystemInfo();
        this.cmdName = cmdName;
        createLogicalComponents();
        configureSystemArchitecture();
    }

    protected void createLogicalComponents() {
        buttonModel = ButtonModel.createButton(cmdName);
        buttonObserver = new ButtonObserver();

        chain = new ArrayList<>();
    }


    protected void configureSystemArchitecture() {
        buttonModel.addObserver(buttonObserver);
    }

    @Override
    public void addConcreteButton(IObservable button) {
        concreteButton = button;
        button.addObserver(buttonModel);
    }

    @Override
    public void addChainLink(IChainActor link, ILed concreteLed) {
        ILedObserver ledObs = LedObserver.create();
        ledObs.setLed(concreteLed);

        link.setLedModel(LedModel.createLed(ledObs));

        if (chain.isEmpty()) {
            //if the link is the first one to be added, add it to the list and connect it to the observer
            chain.add(link);
            link.setHead(true);
            buttonObserver.setControl(link.getChannel());

        } else {
            /*if(chain.get(0).getClickCount()==0){//the chain hasn't started, i don't need to start/stop the system | i know chain.get(0) exists, or i'd be in the main if branch
                 addLink(link);
            }else {
                MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.stoptMsg(),chain.get(0).getChannel());
                addLink(link);
                MsgUtil.INSTANCE.forward(MsgUtil.INSTANCE.startMsg(),chain.get(0).getChannel());
            }*/

            addLink(link);
        }
    }

    private void addLink(IChainActor link) {
        IChainActor last = chain.get(chain.size() - 1);
        IChainActor first = chain.get(0);

        chain.add(link);
        link.setHead(false); //in case the link wasn't properly set up

        last.setNext(link.getChannel());
        first.setPrev(link.getChannel());

        link.setPrev(last.getChannel());
        link.setNext(first.getChannel());
    }

    @Override
    public IObservable getConcreteButton() {
        return concreteButton;
    }

    @Override
    public IButtonModel getButtonModel() {
        return buttonModel;
    }

    @Override
    public List<IChainActor> getChain() {
        return chain;
    }
}
