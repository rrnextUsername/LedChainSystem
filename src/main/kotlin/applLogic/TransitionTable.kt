package applLogic

import enums.LinkState
import enums.MsgId


class TransitionTable {

    private var transitionTable = mutableMapOf<Pair<LinkState, MsgId>, suspend () -> Unit>()

    init {
        for (state in LinkState.values()) {
            for (input in MsgId.values()) {
                transitionTable[Pair(state, input)] = {}
            }
        }
    }

    fun putAction(state: LinkState, input: MsgId, action: suspend () -> Unit) {
        transitionTable[Pair(state, input)] = action
    }

    fun action(state: LinkState, input: MsgId): suspend () -> Unit {
        return transitionTable[Pair(state, input)]!!
    }
}



