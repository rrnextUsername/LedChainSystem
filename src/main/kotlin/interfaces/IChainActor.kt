package interfaces

import applLogic.LinkState
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.channels.SendChannel

interface IChainActor : IActorBasic {

    var next: SendChannel<ApplMessage>?
    var prev: SendChannel<ApplMessage>?

    var head: Boolean
    var state: LinkState

    var ledModel: SendChannel<ApplMessage>?

}