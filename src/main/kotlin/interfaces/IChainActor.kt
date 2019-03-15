package interfaces

import it.unibo.blsFramework.interfaces.ILedModel
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.channels.SendChannel

interface IChainActor {

    var next: SendChannel<ApplMessage>?
    var prev: SendChannel<ApplMessage>?
    var clickCount: Int

    var head: Boolean

    var ledModel: ILedModel?

    fun getChannel(): SendChannel<ApplMessage>

}