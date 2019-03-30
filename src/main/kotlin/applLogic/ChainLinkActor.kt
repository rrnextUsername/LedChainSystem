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
            MsgId.ACTIVATE -> startReceived(msg)
            MsgId.DEACTIVATE -> stopReceived(msg)

            MsgId.ON -> onReceived(msg)
            MsgId.OFF -> offReceived(msg)

            MsgId.CLICK -> clickReceived(msg)
        }
    }

    suspend fun clickReceived(msg: ApplMessage) {

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

    suspend fun startReceived(msg: ApplMessage) {
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

    suspend fun stopReceived(msg: ApplMessage) {
        if (state == LinkState.SLEEP) {
            return
        }

        state = LinkState.SLEEP
        // doSendOff = false
        forward("${MsgId.DEACTIVATE}", "deactivate", next)
        //next!!.send(MsgUtil.stopMsg(name, "next"))
    }

    suspend fun onReceived(msg: ApplMessage) {
        if (state == LinkState.LIVE) {
            return
        }

        state = LinkState.LIVE
    }

    suspend fun offReceived(msg: ApplMessage) {
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

