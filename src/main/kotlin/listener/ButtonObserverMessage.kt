package listener

import enums.MsgId
import interfaces.IObservableMessage
import it.unibo.bls.interfaces.IObserver
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.ObsoleteCoroutinesApi
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

    @UseExperimental(ObsoleteCoroutinesApi::class)
    override fun update(source: Observable, state: Any) {
        clickCount++
        for (channel in observers) {
            MsgUtil.forward(
                ApplMessage("msg(${MsgId.CLICK}, dispatch, buttonObserver, controller, $clickCount, ${MsgUtil.count})"),
                channel
            )
        }
    }
}
