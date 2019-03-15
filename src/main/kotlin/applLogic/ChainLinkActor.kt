package applLogic

import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChainLinkActor(linkName: String, private val delay: Int) : AbstractChainActor(linkName) {

    var doBlink = false
    var doSendOff = false

    var suspendedBlink: Boolean? = null

    override suspend fun actorBody(msg: ApplMessage) {
        when (msg.msgId()) {
            "start" -> startReceived(msg)
            "stop" -> stopReceived(msg)

            "suspend" -> suspendReceived(msg)
            "resume" -> resumeReceived(msg)

            "on" -> onReceived(msg)
            "off" -> offReceived(msg)

            "click" -> clickReceived(msg)
        }
    }

    private suspend fun suspendReceived(msg: ApplMessage) {
        if (suspendedBlink != null)
            return

        suspendedBlink = doBlink
        doBlink = false

        prev!!.send(MsgUtil.suspendMsg(name, "prev"))
    }

    private suspend fun resumeReceived(msg: ApplMessage) {
        if (suspendedBlink == null)
            return

        doBlink = suspendedBlink!!
        suspendedBlink = null

        prev!!.send(MsgUtil.resumeMsg(name, "prev"))
    }

    override suspend fun clickReceived(msg: ApplMessage) {
        clickCount++

        if (clickCount % 2 == 0) {
            stopReceived(msg)
            //ledModel!!.turnOff()
        } else {
            startReceived(msg)
            //ledModel!!.turnOn()
        }
    }

    override suspend fun startReceived(msg: ApplMessage) {
        if (doBlink) {
            return
        }

        doBlink = true
        //next!!.send(MsgUtil.startMsg(name, "next"))
        prev!!.send(MsgUtil.startMsg(name, "prev"))

        if (head) {
            println("::::::::::$name::    i'm the first one, starting chain    ::")
            applLogic()
        }
    }

    override suspend fun stopReceived(msg: ApplMessage) {
        if (!doBlink) {
            return
        }

        doBlink = false
        doSendOff = false
        //next!!.send(MsgUtil.stopMsg(name, "next"))
        prev!!.send(MsgUtil.stopMsg(name, "prev"))
    }

    override suspend fun onReceived(msg: ApplMessage) {
        if (doBlink) {
            return
        }

        doBlink = true
    }

    override suspend fun offReceived(msg: ApplMessage) {
        head = true

        applLogic()
    }

    override suspend fun applLogic() {
        if (!doBlink)
            return

        turnOnLed()
        next!!.send(MsgUtil.onMsg(name, "next"))

        GlobalScope.launch {
            doSendOff = true
            Utils.delay(delay)

            if (doSendOff) {
                turnOffLed()

                println("::::::::::$name::coroutine::    led ${ledModel?.state}|blink $doBlink|head $head::  notifying forward    ::")
                next!!.send(MsgUtil.offMsg(name, "next"))

                head = false
            } else {
                println("::::::::::$name::coroutine::    led ${ledModel?.state}|blink $doBlink|head $head::  dropping    ::")
            }

        }
    }

    //error in the underlying logic
    private fun turnOnLed() {
        ledModel?.turnOff()
    }

    private fun turnOffLed() {
        ledModel?.turnOn()
    }

}

