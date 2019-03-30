package applLogic

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import model.LedActorModel
import stateMachine.LinkState
import stateMachine.MsgId
import stateMachine.TransitionTable

abstract class AbstractChainActor(actorName: String) : ActorBasic(actorName) {

    lateinit var next: ActorBasic
    var ledModel: LedActorModel? = null

    var state: LinkState = LinkState.SLEEP
    var transitionTable = TransitionTable()

    init {
        transitionTableSetup()
    }

    abstract fun transitionTableSetup()

    override fun toString(): String {
        return name
    }


    //error in the underlying logic, turnOn->off and turnOff->on
    suspend fun turnOnLed() {
        forward("${MsgId.OFF}", "turn off led", ledModel!!)
    }

    suspend fun turnOffLed() {
        forward("${MsgId.ON}", "turn on led", ledModel!!)

    }

    protected fun doError(msgId: MsgId, comment: String) {
        println(":::ERROR::: Actor: $name :::: $msgId message received :: state=$state :: $comment :::ERROR::: ")
    }

    protected fun doUnexpected(msgId: MsgId, comment: String) {
        println(":::WARNING::: Actor: $name :::: $msgId message received :: state=$state :: $comment :::WARNING:::")
    }

    protected fun doExpected(msgId: MsgId, comment: String) {
        println("Actor: $name :::: $msgId message received :: state=$state :: $comment")
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

    fun MsgUtil.tokenMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.TOKEN.name, "dispatch", sender, receiver, MsgId.TOKEN.name, count.toString())
    }

    fun MsgUtil.passTokenMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage(MsgId.PASS_TOKEN.name, "dispatch", sender, receiver, MsgId.PASS_TOKEN.name, count.toString())
    }



}