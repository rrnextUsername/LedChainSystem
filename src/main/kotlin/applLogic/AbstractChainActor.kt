package applLogic

import enums.MsgId
import interfaces.IChainActor
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.channels.SendChannel

abstract class AbstractChainActor(actorName: String) : ActorBasic(actorName), IChainActor {

    override var next: SendChannel<ApplMessage>? = null
    override var prev: SendChannel<ApplMessage>? = null

    //override var state: Boolean = false

    fun MsgUtil.onMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.ON.name, "dispatch", sender, receiver, MsgId.ON.name, count.toString())
    }

    fun MsgUtil.offMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.OFF.name, "dispatch", sender, receiver, MsgId.OFF.name, count.toString())
    }

    fun MsgUtil.suspendMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.SUSPEND.name, "dispatch", sender, receiver, MsgId.SUSPEND.name, count.toString())
    }

    fun MsgUtil.resumeMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.RESUME.name, "dispatch", sender, receiver, MsgId.RESUME.name, count.toString())
    }

    fun MsgUtil.startMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.START.name, "dispatch", sender, receiver, MsgId.START.name, count.toString())
    }

    fun MsgUtil.activateMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.ACTIVATE.name, "dispatch", sender, receiver, MsgId.ACTIVATE.name, count.toString())
    }

    fun MsgUtil.deactivateMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.DEACTIVATE.name, "dispatch", sender, receiver, MsgId.DEACTIVATE.name, count.toString())
    }

    fun MsgUtil.stopMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.STOP.name, "dispatch", sender, receiver, MsgId.STOP.name, count.toString())
    }

    fun MsgUtil.doneMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.DONE.name, "dispatch", sender, receiver, MsgId.DONE.name, count.toString())
    }

    fun MsgUtil.doOnMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.DO_ON.name, "dispatch", sender, receiver, MsgId.DO_ON.name, count.toString())
    }

    fun MsgUtil.doOffMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.DO_OFF.name, "dispatch", sender, receiver, MsgId.DO_OFF.name, count.toString())
    }



}