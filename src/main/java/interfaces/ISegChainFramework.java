package interfaces;

import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.blsFramework.interfaces.IButtonModel;

public interface ISegChainFramework {
    void addConcreteButton(IObservable button);
    void addConcreteLed(IChainActor link, ILed led);

    void addChainLink(IChainActor link);

    void addButtonObserver(IChainActor buttonControl);

    void addLedModel(ILedActorModel model);

    ILedActorModel getLedModelOf(IChainActor link);

    IObservable getConcreteButton();

    IButtonModel getButtonModel();

    IChainActor getLinkAt(int position);

    IChainActor getFirstLink();
    IChainActor getLastLink();

}
