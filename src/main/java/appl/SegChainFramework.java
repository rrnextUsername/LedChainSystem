package appl;

import applLogic.AbstractChainActor;
import enums.LinkState;
import interfaces.ISegChainFramework;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.bls.utils.Utils;
import it.unibo.blsFramework.concreteDevices.LedObserver;
import it.unibo.blsFramework.interfaces.IButtonModel;
import it.unibo.blsFramework.interfaces.ILedModel;
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
    private List<AbstractChainActor> chain;
    private List<ILedModel> ledActorModels;
    private ButtonObserverMessage buttonObserverMessage;
    private ButtonModel buttonModel;
    private IObservable concreteButton;
    private AbstractChainActor currentButtonObserver;


    public SegChainFramework(String cmdName) {
        Utils.showSystemInfo();
        this.cmdName = cmdName;
        createLogicalComponents();
        configureSystemArchitecture();
    }

    protected void createLogicalComponents() {
        buttonModel = ButtonModel.createButton(cmdName);
        buttonObserverMessage = new ButtonObserverMessage("button observer");

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
    public void addConcreteLed(AbstractChainActor link, ILed led) {
        if (!chain.contains(link)) {
            return;
        }

        if (link.getLedModel() == null) {
            ILedModel ledActorModel = LedActorModel.Companion.createLed(link.toString());
            ledActorModels.add(ledActorModel);
            link.setLedModel((LedActorModel) ledActorModel);
        }

        ILedObserver ledObs = LedObserver.create();
        ledObs.setLed(led);
        getLedModelOf(link).addObserver(ledObs);
    }

    @Override
    public void addChainLink(AbstractChainActor link) {
        if (chain.isEmpty()) {
            //if the link is the first one to be added, add it to the list and connect it to the observer
            chain.add(link);

            addObserverLink(link);
        } else {
            if (currentButtonObserver.getState() == LinkState.SLEEP_TOKEN || currentButtonObserver.getState() == LinkState.SLEEP) {//the chain hasn't started or is stopped, i don't need to start/stop the system | i know chain.get(0) exists, or i'd be in the main if branch
                addLink(link);
            } else {
                sendStop();
                addLink(link);
                sendStart();
            }
        }
    }

    @Override
    public void addButtonObserver(AbstractChainActor link) {
        if (chain.isEmpty()) {
            chain.add(link);

            addObserverLink(link);
        } else {

            if (currentButtonObserver.getState() == LinkState.SLEEP_TOKEN || currentButtonObserver.getState() == LinkState.SLEEP) {//the chain hasn't started or is stopped, i don't need to start/stop the system
                addObserverLink(link);
            } else {
                sendStop();
                Utils.delay(50);
                addObserverLink(link);
                sendStart();
            }
        }
    }

    @Override
    public void addLedModel(ILedModel model) {
        ledActorModels.add(model);
    }

    @Override
    public ILedModel getLedModelOf(AbstractChainActor link) {
        return ledActorModels.stream().filter(model -> model.equals(link.getLedModel())).findFirst().get();
    }

    private void addObserverLink(AbstractChainActor link) {
        currentButtonObserver = link;
        buttonObserverMessage.addObserver(currentButtonObserver);
    }

    private void addLink(AbstractChainActor link) {
        AbstractChainActor last = chain.get(chain.size() - 1);
        AbstractChainActor first = chain.get(0);

        chain.add(link);

        last.setNext(link);

        link.setNext(first);
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
    public AbstractChainActor getLinkAt(int position) {
        return chain.get(position);
    }

    @Override
    public AbstractChainActor getFirstLink() {
        return getLinkAt(0);
    }

    @Override
    public AbstractChainActor getLastLink() {
        return getLinkAt(chain.size() - 1);
    }

    private void sendClick() {
        MsgUtil.INSTANCE.sendMsg(new ApplMessage("CLICK", "dispatch", "main", "buttonControl", "CLICK", "0"), currentButtonObserver);
    }

    private void sendStart() {
        MsgUtil.INSTANCE.sendMsg(new ApplMessage("ACTIVATE", "dispatch", "main", "buttonControl", "ACTIVATE", "0"), currentButtonObserver);
    }

    private void sendStop() {
        MsgUtil.INSTANCE.sendMsg(new ApplMessage("DEACTIVATE", "dispatch", "main", "buttonControl", "DEACTIVATE", "0"), currentButtonObserver);
    }
}
