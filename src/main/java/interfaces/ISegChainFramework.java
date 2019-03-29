package interfaces;

import applLogic.AbstractChainActor;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;
import it.unibo.blsFramework.interfaces.IButtonModel;
import it.unibo.blsFramework.interfaces.ILedModel;

public interface ISegChainFramework {
    void addConcreteButton(IObservable button);

    void addConcreteLed(AbstractChainActor link, ILed led);

    void addChainLink(AbstractChainActor link);

    void addButtonObserver(AbstractChainActor buttonControl);

    void addLedModel(ILedModel model);

    ILedModel getLedModelOf(AbstractChainActor link);

    IObservable getConcreteButton();

    IButtonModel getButtonModel();

    AbstractChainActor getLinkAt(int position);

    AbstractChainActor getFirstLink();

    AbstractChainActor getLastLink();

}
