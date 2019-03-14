import it.unibo.chain.messages.ApplMessage
import it.unibo.chain.messages.MsgUtil
import kotlinx.coroutines.channels.SendChannel
import java.util.*


class ButtonObserver : Observer {
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
