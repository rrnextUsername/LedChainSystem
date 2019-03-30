package applLogic

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import model.LedActorModel
import stateMachine.LinkState
import stateMachine.MsgId

abstract class AbstractChainActor(actorName: String) : ActorBasic(actorName) {

    lateinit var next: ActorBasic
    var ledModel: LedActorModel? = null

    var state: LinkState = LinkState.SLEEP


    override fun toString(): String {
        return name
    }


    //error in the underlying logic, turnOn->off and turnOff->on :P
    suspend fun turnOnLed() {
        forward("${MsgId.OFF}", "turn off led", ledModel!!)
    }

    suspend fun turnOffLed() {
        forward("${MsgId.ON}", "turn on led", ledModel!!)

    }

    //override var state: Boolean = false

    fun MsgUtil.onMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.ON.name, "dispatch", sender, receiver, MsgId.ON.name, count.toString())
    }

    fun MsgUtil.offMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.OFF.name, "dispatch", sender, receiver, MsgId.OFF.name, count.toString())
    }

    fun MsgUtil.activateMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.ACTIVATE.name, "dispatch", sender, receiver, MsgId.ACTIVATE.name, count.toString())
    }

    fun MsgUtil.deactivateMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.DEACTIVATE.name, "dispatch", sender, receiver, MsgId.DEACTIVATE.name, count.toString())
    }

    fun MsgUtil.doneMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.DONE.name, "dispatch", sender, receiver, MsgId.DONE.name, count.toString())
    }

    fun MsgUtil.doOnMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.TOKEN.name, "dispatch", sender, receiver, MsgId.TOKEN.name, count.toString())
    }

    fun MsgUtil.doOffMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.PASS_TOKEN.name, "dispatch", sender, receiver, MsgId.PASS_TOKEN.name, count.toString())
    }



}