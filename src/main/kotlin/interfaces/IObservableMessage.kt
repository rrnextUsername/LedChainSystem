package interfaces

import it.unibo.kactor.ActorBasic

interface IObservableMessage {

    fun addObserver(observer: ActorBasic)
}