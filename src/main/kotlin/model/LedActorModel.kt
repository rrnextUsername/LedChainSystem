package model

import it.unibo.bls.interfaces.IObserver
import it.unibo.blsFramework.interfaces.ILedModel
import it.unibo.blsFramework.models.LedModel
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import stateMachine.MsgId

class LedActorModel(actorName: String) : ActorBasic(actorName), ILedModel {


    override suspend fun actorBody(msg: ApplMessage) {
        when (MsgId.valueOf(msg.msgId())) {
            MsgId.ON -> turnOn()
            MsgId.OFF -> turnOff()
        }
    }

    lateinit var ledModel: ILedModel

    override fun getState(): Boolean {
        return ledModel.state
    }

    override fun turnOff() {
        println("$name::off received")
        ledModel.turnOff()
    }

    override fun addObserver(p0: IObserver?) {
        ledModel.addObserver(p0)
    }

    override fun turnOn() {
        println("$name::on received")
        ledModel.turnOn()
    }


    companion object {

        //Factory method
        fun createLed(name: String): ILedModel {
            val led = LedActorModel(name)
            led.ledModel = LedModel.createLed()

            return led
        }

        fun createLed(name: String, observer: IObserver): ILedModel {
            val led = LedActorModel(name)
            led.ledModel = LedModel.createLed(observer)

            return led
        }
    }

}