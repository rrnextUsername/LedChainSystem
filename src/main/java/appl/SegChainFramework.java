package appl;

import applLogic.LinkState;
import interfaces.IChainActor;
import interfaces.ILedActorModel;
import interfaces.ISegChainFramework;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.bls.utils.Utils;
import it.unibo.blsFramework.concreteDevices.LedObserver;
import it.unibo.blsFramework.interfaces.IButtonModel;
import it.unibo.blsFramework.interfaces.ILedObserver;
import it.unibo.blsFramework.models.ButtonModel;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import listener.ButtonObserverMessage;
import model.LedActorModel;

import java.util.ArrayList;
import java.util.List;

public class SegChainFramework implements ISegChainFramework {

    public static SegChainFramework createTheSystem(String cmdName) {
        return new SegChainFramework(cmdName);
    }

    private final String cmdName;
    private List<IChainActor> chain;
    private List<ILedActorModel> ledActorModels;
    private ButtonObserverMessage buttonObserverMessage;
    private ButtonModel buttonModel;
    private IObservable concreteButton;
    private IChainActor currentButtonObserver;


    public SegChainFramework(String cmdName) {
        Utils.showSystemInfo();
        this.cmdName = cmdName;
        createLogicalComponents();
        configureSystemArchitecture();
    }

    protected void createLogicalComponents() {
        buttonModel = ButtonModel.createButton(cmdName);
        buttonObserverMessage = new ButtonObserverMessage();

        chain = new ArrayList<>();
        ledActorModels = new ArrayList<>();
    }


    protected void configureSystemArchitecture() {
        buttonModel.addObserver(buttonObserverMessage);
    }

    @Override
    public void addConcreteButton(IObservable button) {
        concreteButton = button;
        button.addObserver(buttonModel);
    }

    @Override
    public void addConcreteLed(IChainActor link, ILed led) {
        if (!chain.contains(link)) {
            return;
        }

        if (link.getLedModel() == null) {
            ILedActorModel ledActorModel = LedActorModel.Companion.createLed(link.getName());
            ledActorModels.add(ledActorModel); //i have to add it to the list, or it stops existing the moment the if ends
            link.setLedModel(ledActorModel.getChannel());
        }

            ILedObserver ledObs = LedObserver.create();
            ledObs.setLed(led);
        getLedModelOf(link).addObserver(ledObs);
    }

    @Override
    public void addChainLink(IChainActor link) {
        if (chain.isEmpty()) {
            //if the link is the first one to be added, add it to the list and connect it to the observer
            chain.add(link);
            link.setHasToken(true);

            addObserverLink(link);
        } else {
            if (currentButtonObserver.getState() == LinkState.STOPPED) {//the chain hasn't started or is stopped, i don't need to start/stop the system | i know chain.get(0) exists, or i'd be in the main if branch
                 addLink(link);
            }else {
                sendClick();
                addLink(link);
                sendClick();
            }
        }
    }

    @Override
    public void addButtonObserver(IChainActor link) {
        if (chain.isEmpty()) {
            chain.add(link);

            addObserverLink(link);
        } else {

            if (currentButtonObserver.getState() == LinkState.STOPPED) {//the chain hasn't started or is stopped, i don't need to start/stop the system
                addObserverLink(link);
            } else {
                sendClick();
                Utils.delay(50);
                addObserverLink(link);
                sendClick();
            }
        }
    }

    @Override
    public void addLedModel(ILedActorModel model) {
        ledActorModels.add(model);
    }

    @Override
    public ILedActorModel getLedModelOf(IChainActor link) {
        return ledActorModels.stream().filter(model -> model.getChannel().equals(link.getLedModel())).findFirst().get();
    }

    private void addObserverLink(IChainActor link) {
        currentButtonObserver = link;
        buttonObserverMessage.addObserver(currentButtonObserver.getChannel());
    }

    private void addLink(IChainActor link) {
        IChainActor last = chain.get(chain.size() - 1);
        IChainActor first = chain.get(0);

        chain.add(link);
        link.setHasToken(false); //in case the link wasn't properly set up

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
    public IChainActor getLinkAt(int position) {
        return chain.get(position);
    }

    @Override
    public IChainActor getFirstLink() {
        return getLinkAt(0);
    }

    @Override
    public IChainActor getLastLink() {
        return getLinkAt(chain.size()-1);
    }

    private void sendClick(){
        MsgUtil.INSTANCE.forward(new ApplMessage("click", "dispatch", "main", "buttonControl", "click", "0"), currentButtonObserver.getChannel());
    }
}
