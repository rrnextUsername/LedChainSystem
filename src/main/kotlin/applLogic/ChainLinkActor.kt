package applLogic

import interfaces.ILedActorModel
import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChainLinkActor(linkName: String, private val delay: Int) : AbstractChainActor(linkName) {

    var doBlink = false
    var doSendOff = false
    override var ledModel: ILedActorModel? = null

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

    //error in the underlying logic, turnOn->off and turnOff->on :P
    private suspend fun turnOnLed() {
        ledModel?.getChannel()?.send(MsgUtil.offMsg(name,"led"))
    }

    private suspend fun turnOffLed() {
        ledModel?.getChannel()?.send(MsgUtil.onMsg(name,"led"))
    }

}

