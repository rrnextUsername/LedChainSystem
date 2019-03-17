package applLogic

import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

class ChainLinkActor(linkName: String, private val delay: Int) : AbstractChainActor(linkName) {

    override var ledModel: SendChannel<ApplMessage>? = null
    //var doSendOff = false


    override suspend fun actorBody(msg: ApplMessage) {
        when (msg.msgId()) {
            "start" -> startReceived(msg)
            "stop" -> stopReceived(msg)

            "on" -> onReceived(msg)
            "off" -> offReceived(msg)

            "click" -> clickReceived(msg)
        }
    }

    override suspend fun clickReceived(msg: ApplMessage) {

        if (state == LinkState.STARTED) {
            autoMsg(MsgUtil.stoptMsg())

            //stopReceived(msg)
            //ledModel!!.turnOff()
        } else {
            autoMsg(MsgUtil.startMsg())

            //startReceived(msg)
            //ledModel!!.turnOn()
        }
    }

    override suspend fun startReceived(msg: ApplMessage) {
        if (state == LinkState.STARTED) {
            return
        }

        state = LinkState.STARTED
        prev!!.send(MsgUtil.startMsg(name, "prev"))

        if (head) {
            println("::::::::::$name::    i'm the first one, starting chain    ::")
            applLogic()
        }
    }

    override suspend fun stopReceived(msg: ApplMessage) {
        if (state == LinkState.STOPPED) {
            return
        }

        state = LinkState.STOPPED
        // doSendOff = false
        prev!!.send(MsgUtil.stopMsg(name, "prev"))
    }

    override suspend fun onReceived(msg: ApplMessage) {
        if (state == LinkState.STARTED) {
            return
        }

        state = LinkState.STARTED
    }

    override suspend fun offReceived(msg: ApplMessage) {
        head = true

        applLogic()
    }

    override suspend fun applLogic() {
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

                head = false
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

