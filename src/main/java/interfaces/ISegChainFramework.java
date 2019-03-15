package interfaces;

import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.blsFramework.interfaces.IButtonModel;

import java.util.List;

public interface ISegChainFramework {
    void addConcreteButton(IObservable button);

    void addChainLink(IChainActor link, ILed concreteLed);
    void setButtonControl(IChainActor buttonControl);

    IObservable getConcreteButton();

    IButtonModel getButtonModel();

    List<IChainActor> getChain();
}
