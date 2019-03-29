package applLogic

import enums.LinkState
import enums.MsgId
import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

class ChainLinkActor(linkName: String, private val delay: Int) : AbstractChainActor(linkName) {

    override var state: LinkState = LinkState.STOPPED
    var hasToken: Boolean = false

    override var ledModel: SendChannel<ApplMessage>? = null
    //var doSendOff = false


    override suspend fun actorBody(msg: ApplMessage) {
        when (MsgId.valueOf(msg.msgId())) {
            MsgId.START -> startReceived(msg)
            MsgId.STOP -> stopReceived(msg)

            MsgId.ON -> onReceived(msg)
            MsgId.OFF -> offReceived(msg)

            MsgId.CLICK -> clickReceived(msg)
        }
    }

    suspend fun clickReceived(msg: ApplMessage) {

        if (state == LinkState.STARTED) {
            autoMsg(MsgUtil.stopMsg(name, name))

            //stopReceived(msg)
            //ledModel!!.turnOff()
        } else {
            autoMsg(MsgUtil.startMsg(name, name))

            //startReceived(msg)
            //ledModel!!.turnOn()
        }
    }

    suspend fun startReceived(msg: ApplMessage) {
        if (state == LinkState.STARTED) {
            return
        }

        state = LinkState.STARTED
        prev!!.send(MsgUtil.startMsg(name, "prev"))
        //next!!.send(MsgUtil.startMsg(name, "next"))

        if (hasToken) {
            println("::::::::::$name::    i'm the first one, starting chain    ::")
            applLogic()
        }
    }

    suspend fun stopReceived(msg: ApplMessage) {
        if (state == LinkState.STOPPED) {
            return
        }

        state = LinkState.STOPPED
        // doSendOff = false
        prev!!.send(MsgUtil.stopMsg(name, "prev"))
        //next!!.send(MsgUtil.stopMsg(name, "next"))
    }

    suspend fun onReceived(msg: ApplMessage) {
        if (state == LinkState.STARTED) {
            return
        }

        state = LinkState.STARTED
    }

    suspend fun offReceived(msg: ApplMessage) {
        hasToken = true

        applLogic()
    }

    suspend fun applLogic() {
        if (state == LinkState.STOPPED)
            return

        turnOnLed()
        next!!.send(MsgUtil.onMsg(name, "next"))

        GlobalScope.launch {
            //doSendOff = true
            Utils.delay(delay)

            if (state == LinkState.STARTED) {
                turnOffLed()

                next!!.send(MsgUtil.offMsg(name, "next"))

                hasToken = false
            } else {
                println("dropping")
            }

        }
    }

    //error in the underlying logic, turnOn->off and turnOff->on :P
    private suspend fun turnOnLed() {
        ledModel?.send(MsgUtil.offMsg(name, "led"))
    }

    private suspend fun turnOffLed() {
        ledModel?.send(MsgUtil.onMsg(name, "led"))

    }

}

