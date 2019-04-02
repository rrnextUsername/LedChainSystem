package applLogic

import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import stateMachine.LinkState
import stateMachine.MsgId

class ChainLinkActor(linkName: String, private val delay: Int, var hasToken: Boolean = false) :
    AbstractChainActor(linkName) {


    override fun transitionTableSetup() {
    }

    override suspend fun actorBody(msg: ApplMessage) {
        when (MsgId.valueOf(msg.msgId())) {
            MsgId.ACTIVATE -> startReceived()
            MsgId.DEACTIVATE -> stopReceived()

            MsgId.ON -> onReceived()
            MsgId.OFF -> offReceived()

            MsgId.CLICK -> clickReceived()
        }
    }

    private suspend fun clickReceived() {

        if (state == LinkState.LIVE) {
            autoMsg(MsgUtil.deactivateMsg(name, name))

            //stopReceived(msg)
            //ledModel!!.turnOff()
        } else {
            autoMsg(MsgUtil.activateMsg(name, name))

            //startReceived(msg)
            //ledModel!!.turnOn()
        }
    }

    private suspend fun startReceived() {
        if (state == LinkState.LIVE) {
            return
        }

        state = LinkState.LIVE
        forward("${MsgId.ACTIVATE}", "activate", next)
        //next!!.send(MsgUtil.startMsg(name, "next"))

        if (hasToken) {
            println("::::::::::$name::    i'm the first one, starting chain    ::")
            applLogic()
        }
    }

    private suspend fun stopReceived() {
        if (state == LinkState.SLEEP) {
            return
        }

        state = LinkState.SLEEP
        // doSendOff = false
        forward("${MsgId.DEACTIVATE}", "deactivate", next)
        //next!!.send(MsgUtil.stopMsg(name, "next"))
    }

    private suspend fun onReceived() {
        if (state == LinkState.LIVE) {
            return
        }

        state = LinkState.LIVE
    }

    private suspend fun offReceived() {
        hasToken = true

        applLogic()
    }

    suspend fun applLogic() {
        if (state == LinkState.SLEEP)
            return

        turnOnLed()
        forward("${MsgId.ON}", "led on", next)

        GlobalScope.launch {
            //doSendOff = true
            Utils.delay(delay)

            if (state == LinkState.LIVE) {
                turnOffLed()

                forward("${MsgId.OFF}", "led on", next)

                hasToken = false
            } else {
                println("dropping")
            }

        }
    }


}

