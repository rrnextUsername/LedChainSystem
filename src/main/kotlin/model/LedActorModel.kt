package model

import interfaces.ILedActorModel
import it.unibo.bls.interfaces.IObserver
import it.unibo.blsFramework.interfaces.ILedModel
import it.unibo.blsFramework.models.LedModel
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage

class LedActorModel(actorName: String) : ActorBasic(actorName), ILedActorModel {

    lateinit var ledModel: ILedModel

    override fun getState(): Boolean {
        return ledModel.state
    }

    override fun turnOff() {
        println("off received")
        ledModel.turnOff()
    }

    override fun addObserver(p0: IObserver?) {
        ledModel.addObserver(p0)
    }

    override fun turnOn() {
        println("on received")
        ledModel.turnOn()
    }

    override suspend fun actorBody(msg: ApplMessage) {
        when (msg.msgId()) {
            "on" -> turnOn()
            "off" -> turnOff()
        }
    }

    companion object {

        //Factory method
        fun createLed(name: String): ILedActorModel {
            val led = LedActorModel(name)
            led.ledModel = LedModel.createLed()

            return led
        }

        fun createLed(name: String, observer: IObserver): ILedActorModel {
            val led = LedActorModel(name)
            led.ledModel = LedModel.createLed(observer)

            return led
        }
    }

}