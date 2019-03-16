package interfaces;

import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.blsFramework.interfaces.IButtonModel;

import java.util.List;

public interface ISegChainFramework {
    void addConcreteButton(IObservable button);
    void addConcreteLed(IChainActor link, ILed led);

    void addChainLink(IChainActor link);
    void setButtonControl(IChainActor buttonControl);

    IObservable getConcreteButton();

    IButtonModel getButtonModel();

    IChainActor getLinkAt(int position);
    IChainActor getFirstlink();
    IChainActor getLastLink();

}
