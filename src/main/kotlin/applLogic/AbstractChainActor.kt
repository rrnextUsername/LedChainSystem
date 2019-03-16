package applLogic

import interfaces.IChainActor
import it.unibo.blsFramework.interfaces.ILedModel
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.channels.SendChannel

abstract class AbstractChainActor(actorName: String) : ActorBasic(actorName), IChainActor {

    override var next: SendChannel<ApplMessage>? = null
    override var prev: SendChannel<ApplMessage>? = null
    override var head: Boolean = false
    override var clickCount: Int = 0

    protected abstract suspend fun startReceived(msg: ApplMessage)
    protected abstract suspend fun stopReceived(msg: ApplMessage)
    protected abstract suspend fun onReceived(msg: ApplMessage)
    protected abstract suspend fun offReceived(msg: ApplMessage)
    protected abstract suspend fun clickReceived(msg: ApplMessage)

    protected abstract suspend fun applLogic()

    fun MsgUtil.onMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("on", "dispatch", sender, receiver, "on", count.toString())
    }

    fun MsgUtil.offMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("off", "dispatch", sender, receiver, "off", count.toString())
    }

    fun MsgUtil.suspendMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("suspend", "dispatch", sender, receiver, "suspend", count.toString())
    }

    fun MsgUtil.resumeMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("resume", "dispatch", sender, receiver, "resume", count.toString())
    }

    fun MsgUtil.startMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("start", "dispatch", sender, receiver, "start", count.toString())
    }

    fun MsgUtil.stopMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("stop", "dispatch", sender, receiver, "stop", count.toString())
    }

}