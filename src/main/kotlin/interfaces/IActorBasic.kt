package interfaces

import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.channels.SendChannel

interface IActorBasic {
    val name: String
    fun getChannel(): SendChannel<ApplMessage>
    suspend fun autoMsg(msg: ApplMessage)
}