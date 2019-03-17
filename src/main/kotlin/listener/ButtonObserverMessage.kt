package listener

import interfaces.IObservableMessage
import it.unibo.bls.interfaces.IObserver
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.channels.SendChannel
import java.util.*
import kotlin.collections.ArrayList


class ButtonObserverMessage : IObserver, IObservableMessage {

    private val observers: ArrayList<SendChannel<ApplMessage>> = ArrayList()
    private var clickCount = 0


    override fun addObserver(channel: SendChannel<ApplMessage>) {
        observers.add(channel)
    }

    fun getNumOfClicks(): Int {
        return clickCount
    }

    override fun update(source: Observable, state: Any) {
        clickCount++
        for (channel in observers) {
            MsgUtil.forward(
                ApplMessage("msg(click, dispatch, buttonObserver, controller, $clickCount, ${MsgUtil.count})"), channel
            )
        }
    }
}
