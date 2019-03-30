package applLogic

import it.unibo.bls.utils.Utils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import stateMachine.LinkState
import stateMachine.MsgId

open class StateMachineLinkActor(name: String, val delay: Int, isHead: Boolean = false) : AbstractChainActor(name) {

    init {
        if (isHead)
            state = LinkState.SLEEP_TOKEN
    }


    override fun transitionTableSetup() {

        //state transitions
        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.DEACTIVATE) {
            doExpected(
                MsgId.DEACTIVATE,
                "deactivating"
            )
            doSleepToken()
        }
        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.PASS_TOKEN) {
            doExpected(
                MsgId.PASS_TOKEN,
                "passing the token to the next link"
            )
            doPassingToken()
        }

        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.ACTIVATE) {
            doExpected(
                MsgId.ACTIVATE,
                "activating"
            )
            doLiveToken()
        }

        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.DEACTIVATE) {
            doUnexpected(
                MsgId.DEACTIVATE,
                "executing to avoid token loss, possibility of extraneous messages in queue, might create double token"
            )
            doSleep()
        }
        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.DONE) {
            doExpected(
                MsgId.DONE,
                "finished passing token"
            )
            doLive()
        }

        transitionTable.putAction(LinkState.LIVE, MsgId.DEACTIVATE) {
            doExpected(
                MsgId.DEACTIVATE,
                "deactivating"
            )
            doSleep()
        }
        transitionTable.putAction(LinkState.LIVE, MsgId.TOKEN) {
            doExpected(
                MsgId.TOKEN,
                "received token from previous link"
            )
            doLiveToken()
        }

        transitionTable.putAction(LinkState.SLEEP, MsgId.ACTIVATE) {
            doExpected(
                MsgId.ACTIVATE,
                "activating"
            )
            doLive()
        }
        transitionTable.putAction(LinkState.SLEEP, MsgId.TOKEN) {
            doUnexpected(
                MsgId.TOKEN,
                "executing to avoid token loss, possibility of extraneous messages in queue, might create double token"
            )
            doSleepToken()
        }


        //click messages
        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.CLICK) {
            doExpected(
                MsgId.CLICK,
                "received click, deactivating"
            )
            autoMsg(MsgUtil.deactivateMsg(name, name))
        }
        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.CLICK) {
            doExpected(
                MsgId.CLICK,
                "received click, deactivating"
            )
            autoMsg(MsgUtil.deactivateMsg(name, name))
        }
        transitionTable.putAction(LinkState.LIVE, MsgId.CLICK) {
            doExpected(
                MsgId.CLICK,
                "received click, deactivating"
            )
            autoMsg(MsgUtil.deactivateMsg(name, name))
        }

        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.CLICK) {
            doExpected(
                MsgId.CLICK,
                "received click, activating"
            )
            autoMsg(MsgUtil.activateMsg(name, name))
        }
        transitionTable.putAction(LinkState.SLEEP, MsgId.CLICK) {
            doExpected(
                MsgId.CLICK,
                "received click, activating"
            )
            autoMsg(MsgUtil.activateMsg(name, name))
        }

        //expected messages
        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.ACTIVATE) {
            doExpected(
                MsgId.ACTIVATE,
                "chain already activated :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.LIVE, MsgId.ACTIVATE) {
            doExpected(
                MsgId.ACTIVATE,
                "chain already activated :: ignoring message"
            )
        }

        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.DEACTIVATE) {
            doExpected(
                MsgId.DEACTIVATE,
                "chain already deactivated :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.SLEEP, MsgId.DEACTIVATE) {
            doExpected(
                MsgId.DEACTIVATE,
                "chain already deactivated :: ignoring message"
            )
        }

        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.PASS_TOKEN) {
            doExpected(
                MsgId.ACTIVATE,
                "actor state changed while delay coroutine was sleeping :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.LIVE, MsgId.PASS_TOKEN) {
            doExpected(
                MsgId.ACTIVATE,
                "actor state changed while delay coroutine was sleeping :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.SLEEP, MsgId.PASS_TOKEN) {
            doExpected(
                MsgId.ACTIVATE,
                "actor state changed while delay coroutine was sleeping :: ignoring message"
            )
        }


        //unexpected messages
        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.ACTIVATE) {
            doUnexpected(
                MsgId.ACTIVATE,
                "chain already activated, possibility of extraneous messages in queue :: ignoring message"
            )
        }

        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.PASS_TOKEN) {
            doUnexpected(
                MsgId.PASS_TOKEN,
                "actor state changed while delay coroutine was sleeping, possibility of extraneous messages in queue :: ignoring message"
            )
        }

        transitionTable.putAction(LinkState.LIVE, MsgId.DONE) {
            doUnexpected(
                MsgId.DONE,
                "token already passed, possibility of extraneous messages in queue :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.SLEEP, MsgId.DONE) {
            doUnexpected(
                MsgId.DONE,
                "token already passed, possibility of extraneous messages in queue :: ignoring message"
            )
        }


        //error handling
        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.TOKEN) {
            doError(
                MsgId.TOKEN,
                "token already present, double token in the chain :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.TOKEN) {
            doError(
                MsgId.TOKEN,
                "token already present, double token in the chain :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.PASSING_TOKEN, MsgId.TOKEN) {
            doError(
                MsgId.TOKEN,
                "token already present, double token in the chain :: ignoring message"
            )
        }

        transitionTable.putAction(LinkState.LIVE_TOKEN, MsgId.DONE) {
            doError(
                MsgId.DONE,
                "token already present, double token in the chain :: ignoring message"
            )
        }
        transitionTable.putAction(LinkState.SLEEP_TOKEN, MsgId.DONE) {
            doError(
                MsgId.DONE,
                "token already present, double token in the chain :: ignoring message"
            )
        }


    }

    override suspend fun actorBody(msg: ApplMessage) {
        transitionTable.action(state, MsgId.valueOf(msg.msgId())).invoke()
    }

    private suspend fun doLiveToken() {
        state = LinkState.LIVE_TOKEN

        //step 1
        turnOnLed()

        //step 2
        forward("${MsgId.ACTIVATE}", "activate", next)

        //step 3
        GlobalScope.launch {
            Utils.delay(delay)
            println("Actor: $name::coroutine :::: delay over :: state=$state")

            autoMsg(MsgUtil.doOffMsg(name, name))
        }
    }

    private suspend fun doSleepToken() {
        state = LinkState.SLEEP_TOKEN

        //step 1
        forward("${MsgId.DEACTIVATE}", "deactivate", next)
    }

    private suspend fun doPassingToken() {
        state = LinkState.PASSING_TOKEN

        //step 1
        turnOffLed()

        //step 2
        forward("${MsgId.TOKEN}", "deactivate", next)

        //step 3
        autoMsg(MsgUtil.doneMsg(name, name))
    }

    private suspend fun doLive() {
        state = LinkState.LIVE

        //step 1
        forward("${MsgId.ACTIVATE}", "deactivate", next)
    }

    private suspend fun doSleep() {
        state = LinkState.SLEEP

        //step 1
        forward("${MsgId.DEACTIVATE}", "deactivate", next)
    }



}