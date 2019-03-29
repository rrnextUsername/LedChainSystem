package listener

import enums.MsgId
import interfaces.IObservableMessage
import it.unibo.bls.interfaces.IObserver
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ButtonObserverMessage(name: String) : ActorBasic(name), IObserver, IObservableMessage {

    override suspend fun actorBody(msg: it.unibo.kactor.ApplMessage) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val observers: ArrayList<ActorBasic> = ArrayList()
    private var clickCount = 0


    override fun addObserver(actor: ActorBasic) {
        observers.add(actor)
    }

    fun getNumOfClicks(): Int {
        return clickCount
    }

    @UseExperimental(ObsoleteCoroutinesApi::class)
    override fun update(source: Observable, state: Any) {
        clickCount++
        for (actorBasic in observers) {
            GlobalScope.launch { forward("${MsgId.CLICK}", "$clickCount", actorBasic) }
        }
    }
}
