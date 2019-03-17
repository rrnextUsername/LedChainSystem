package interfaces

import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.channels.SendChannel

interface IObservableMessage {

    fun addObserver(channel: SendChannel<ApplMessage>)
}