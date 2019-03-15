import it.unibo.bls.utils.Utils
import it.unibo.chain.messages.ApplMessage
import it.unibo.chain.messages.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

class ChainLinkActor(linkName: String, private val delay: Int) : AbstractChainActor(linkName) {

    var next: SendChannel<ApplMessage>? = null
    var prev: SendChannel<ApplMessage>? = null

    var doBlink = false

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
        if (msg.msgId().toInt() % 2 == 0) {
            stopReceived(msg)
        } else {
            startReceived(msg)
        }
    }

    override suspend fun startReceived(msg: ApplMessage) {
        if (doBlink) {
            return
        }

        doBlink = true
        next!!.send(MsgUtil.startMsg(name, "next"))

        if (prev != null) {
            prev!!.send(MsgUtil.startMsg(name, "prev"))
            return
        }

        println("::::::::::$name::    i'm the first one, starting chain    ::")
        applLogic()

    }

    override suspend fun stopReceived(msg: ApplMessage) {
        if (!doBlink) {
            return
        }

        doBlink = false
        next!!.send(MsgUtil.stopMsg(name, "next"))
        if (prev != null) {
            prev!!.send(MsgUtil.stopMsg(name, "prev"))
        }

        ledModel!!.turnOn()
    }

    override suspend fun onReceived(msg: ApplMessage) {
        if (doBlink) {
            return
        }

        doBlink = true
    }

    override suspend fun offReceived(msg: ApplMessage) {
        applLogic()
    }

    override suspend fun applLogic() {
        ledModel?.turnOff()
        next!!.send(MsgUtil.onMsg(name, "next"))

        GlobalScope.launch {
            Utils.delay(delay)

            if (doBlink) {
                ledModel?.turnOn()
                println("::::::::::$name::coroutine::    led ${ledModel?.state}, blink $doBlink, notifying forward    ::")
                next!!.send(MsgUtil.offMsg(name, "next"))
            }

        }
    }

}

