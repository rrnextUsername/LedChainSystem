package listener

import it.unibo.bls.interfaces.IObserver
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.channels.SendChannel
import java.util.*


class ButtonObserverMessage : IObserver {
    private var controller: SendChannel<ApplMessage>? = null

    private var clickCount = 0


    fun setControl(ctrl: SendChannel<ApplMessage>) {
        this.controller = ctrl
    }


    fun getNumOfClicks(): Int {
        return clickCount
    }


    override fun update(source: Observable, state: Any) {
        clickCount++
        MsgUtil.forward(
            ApplMessage("msg(click, dispatch, buttonObserver, controller, $clickCount, ${MsgUtil.count})"),
            controller!!
        )
    }
}
