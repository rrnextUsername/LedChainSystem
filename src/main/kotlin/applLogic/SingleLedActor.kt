package applLogic

import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import stateMachine.LinkState
import stateMachine.MsgId

open class SingleLedActor(name: String, private val delay: Int) :
    AbstractChainActor(name, 5, true) {


    init {
        state = LinkState.INIT
    }

    override fun transitionTableSetup() {

        //state transitions
        transitionTable.putAction(LinkState.INIT, MsgId.CLICK) { doLedOn() }

        transitionTable.putAction(LinkState.LED_ON, MsgId.CLICK) { doEnd() }
        transitionTable.putAction(LinkState.LED_ON, MsgId.OFF) { doLedOff() }

        transitionTable.putAction(LinkState.LED_OFF, MsgId.CLICK) { doEnd() }
        transitionTable.putAction(LinkState.LED_OFF, MsgId.ON) { doLedOn() }
    }


    override suspend fun actorBody(msg: ApplMessage) {
        transitionTable.action(state, MsgId.valueOf(msg.msgId()))()
    }


    private suspend fun doInit() {
        state = LinkState.INIT
    }

    private suspend fun doLedOn() {
        state = LinkState.LED_ON

        turnOnLed()

        GlobalScope.launch {
            Utils.delay(delay)
            //println("Actor: $name::coroutine :::: delay over :: state=$state")

            autoMsg(MsgUtil.offMsg(name, name))
        }
    }

    private suspend fun doLedOff() {
        state = LinkState.LED_OFF

        turnOffLed()

        GlobalScope.launch {
            Utils.delay(delay)
            //println("Actor: $name::coroutine :::: delay over :: state=$state")

            autoMsg(MsgUtil.onMsg(name, name))
        }
    }

    private suspend fun doEnd() {
        state = LinkState.END

        turnOffLed()

        doInit()
    }


}