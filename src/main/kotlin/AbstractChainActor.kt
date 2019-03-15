import it.unibo.blsFramework.interfaces.ILedModel
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

abstract class AbstractChainActor(actorName: String) : ActorBasic(actorName) {

    protected var ledModel: ILedModel? = null

    protected abstract suspend fun startReceived(msg: ApplMessage)
    protected abstract suspend fun stopReceived(msg: ApplMessage)
    protected abstract suspend fun onReceived(msg: ApplMessage)
    protected abstract suspend fun offReceived(msg: ApplMessage)
    protected abstract suspend fun clickReceived(msg: ApplMessage)

    protected abstract suspend fun applLogic()


    protected fun MsgUtil.customMsg(id: String, sender: String, receiver: String, msg: String): ApplMessage {
        count++
        return ApplMessage("msg( $id, dispatch, $sender, $receiver, $msg, $count )")
    }

    protected fun MsgUtil.onMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("msg( on, dispatch, $sender, $receiver, on, $count )")
    }

    protected fun MsgUtil.offMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("msg( off, dispatch, $sender, $receiver, off, $count )")
    }

    protected fun MsgUtil.startMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("msg( start, dispatch, $sender, $receiver, start, $count )")
    }

    protected fun MsgUtil.stopMsg(sender: String, receiver: String): ApplMessage {
        count++
        return ApplMessage("msg( stop, dispatch, $sender, $receiver, stop, $count )")
    }

}