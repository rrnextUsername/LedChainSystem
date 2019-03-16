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
    private ButtonObserverMessage buttonObserverMessage;
    private ButtonModel buttonModel;
    private IObservable concreteButton;
    private IChainActor buttonControl;


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

        if(link.getLedModel()==null)
            link.setLedModel(LedActorModel.Companion.createLed(link.getName()));

            ILedObserver ledObs = LedObserver.create();
            ledObs.setLed(led);
            link.getLedModel().addObserver(ledObs);
    }

    @Override
    public void addChainLink(IChainActor link) {
        if (chain.isEmpty()) {
            //if the link is the first one to be added, add it to the list and connect it to the observer
            chain.add(link);
            link.setHead(true);

            setControlLink(link);
        } else {
            if (buttonControl.getClickCount() %2 == 0) {//the chain hasn't started or is stopped, i don't need to start/stop the system | i know chain.get(0) exists, or i'd be in the main if branch
                 addLink(link);
            }else {
                sendClick();
                addLink(link);
                sendClick();
            }
        }
    }

    @Override
    public void setButtonControl(IChainActor link) {
        if (chain.isEmpty()) {
            chain.add(link);

            setControlLink(link);
        } else {

            if (buttonControl.getClickCount() % 2 == 0) {//the chain hasn't started or is stopped, i don't need to start/stop the system
                setControlLink(link);
            } else {
                sendClick();
                Utils.delay(50);
                setControlLink(link);
                sendClick();
            }
        }
    }

    private void setControlLink(IChainActor link){
        buttonControl=link;
        buttonObserverMessage.setControl(buttonControl.getChannel());
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
    public IChainActor getLinkAt(int position) {
        return chain.get(position);
    }

    @Override
    public IChainActor getFirstlink() {
        return getLinkAt(0);
    }

    @Override
    public IChainActor getLastLink() {
        return getLinkAt(chain.size()-1);
    }

    private void sendClick(){
        MsgUtil.INSTANCE.forward(new ApplMessage("click", "dispatch", "main", "buttonControl", "click", "0"), buttonControl.getChannel());
    }
}
