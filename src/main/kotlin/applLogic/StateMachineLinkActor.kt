package applLogic

import enums.LinkState
import enums.MsgId
import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

class StateMachineLinkActor(name: String, val delay: Int, isHead: Boolean = false) : AbstractChainActor(name) {

    override var ledModel: SendChannel<ApplMessage>? = null
    override var state: LinkState = LinkState.SLEEP

    private val transitionTable = TransitionTable()

    init {
        if (isHead)
            state = LinkState.SLEEP_TOKEN

        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.DEACTIVATE) { doSleepToken() }
        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.DO_OFF) { doPassingToken() }

        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.ACTIVATE) { doLiveToken() }

        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.DEACTIVATE) { doSleep() }
        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.DONE) { doLive() }

        transitionTable.putAction(LinkState.LIVE, MsgId.DEACTIVATE) { doSleep() }
        transitionTable.putAction(LinkState.LIVE, MsgId.DO_ON) { doLiveToken() }

        transitionTable.putAction(LinkState.SLEEP, MsgId.ACTIVATE) { doLive() }


        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.CLICK) { autoMsg(MsgUtil.deactivateMsg(name, name)) }
        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.CLICK) { autoMsg(MsgUtil.deactivateMsg(name, name)) }
        transitionTable.putAction(LinkState.LIVE, MsgId.CLICK) { autoMsg(MsgUtil.deactivateMsg(name, name)) }

        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.CLICK) { autoMsg(MsgUtil.activateMsg(name, name)) }
        transitionTable.putAction(LinkState.SLEEP, MsgId.CLICK) { autoMsg(MsgUtil.activateMsg(name, name)) }


        transitionTable.putAction(
            LinkState.LIVE_TOKEN,
            MsgId.DO_ON
        ) { println("Actor: $name :::: unexpected DO_ON message received :: state=$state") }
        transitionTable.putAction(
            LinkState.LIVE_TOKEN,
            MsgId.DONE
        ) { println("Actor: $name :::: unexpected DONE message received :: state=$state") }
        transitionTable.putAction(
            LinkState.SLEEP_TOKEN,
            MsgId.DO_ON
        ) { println("Actor: $name :::: unexpected DO_ON message received :: state=$state") }
        transitionTable.putAction(
            LinkState.SLEEP_TOKEN,
            MsgId.DONE
        ) { println("Actor: $name :::: unexpected DONE message received :: state=$state") }
        transitionTable.putAction(
            LinkState.PASSING_TOKEN,
            MsgId.DO_ON
        ) { println("Actor: $name :::: unexpected DO_ON message received :: state=$state") }
        transitionTable.putAction(
            LinkState.LIVE,
            MsgId.DONE
        ) { println("Actor: $name :::: unexpected DONE message received :: state=$state") }
        transitionTable.putAction(
            LinkState.SLEEP,
            MsgId.DO_ON
        ) { println("Actor: $name :::: unexpected DO_ON message received :: state=$state") }
    }

    override suspend fun actorBody(msg: ApplMessage) {
        transitionTable.action(state, MsgId.valueOf(msg.msgId())).invoke()
    }

    private suspend fun doLiveToken() {
        state = LinkState.LIVE_TOKEN

        println("Actor: $name :::: started doLiveToken :: state=$state")

        //step 1
        turnOnLed()

        //step 2
        next!!.send(MsgUtil.activateMsg(name, next.toString()))

        //step 3
        GlobalScope.launch {
            Utils.delay(delay)
            println("Actor: $name::coroutine :::: delay over :: state=$state")

            autoMsg(MsgUtil.doOffMsg(name, name))
        }
    }

    private suspend fun doSleepToken() {
        state = LinkState.SLEEP_TOKEN

        println("Actor: $name :::: started doSleepToken :: state=$state")

        //step 1
        next!!.send(MsgUtil.deactivateMsg(name, next.toString()))
    }

    private suspend fun doPassingToken() {
        state = LinkState.PASSING_TOKEN

        println("Actor: $name :::: started doPassingToken :: state=$state")

        //step 1
        turnOffLed()

        //step 2
        next!!.send(MsgUtil.doOnMsg(name, next.toString()))

        //step 3
        autoMsg(MsgUtil.doneMsg(name, name))
    }

    private suspend fun doLive() {
        state = LinkState.LIVE

        println("Actor: $name :::: started doLive :: state=$state")

        //step 1
        next!!.send(MsgUtil.activateMsg(name, next.toString()))
    }

    private suspend fun doSleep() {
        state = LinkState.SLEEP

        println("Actor: $name :::: started doSleep :: state=$state")

        //step 1
        next!!.send(MsgUtil.deactivateMsg(name, next.toString()))
    }


    //error in the underlying logic, turnOn->off and turnOff->on :P
    private suspend fun turnOnLed() {
        ledModel!!.send(MsgUtil.offMsg(name, "led"))
    }

    private suspend fun turnOffLed() {
        ledModel!!.send(MsgUtil.onMsg(name, "led"))

    }

    override fun toString(): String {
        return name
    }


}