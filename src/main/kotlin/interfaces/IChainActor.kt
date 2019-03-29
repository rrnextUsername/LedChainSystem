package interfaces

import enums.LinkState
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.channels.SendChannel

interface IChainActor : IActorBasic {

    var next: SendChannel<ApplMessage>?
    var prev: SendChannel<ApplMessage>?

    var state: LinkState

    var ledModel: SendChannel<ApplMessage>?

}