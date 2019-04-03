package stateMachine

class TransitionTable {

    private var transitionTable = mutableMapOf<Pair<LinkState, MsgId>, suspend () -> Unit>()

    init {
        putDefaultAction { println("------------------ERROR:::UNHANDLED OPERATION:::ERROR------------------") }
    }

    fun putAction(state: LinkState, input: MsgId, action: suspend () -> Unit) {
        transitionTable[Pair(state, input)] = action
    }

    fun putDefaultAction(action: suspend () -> Unit) {
        for (state in LinkState.values()) {
            for (input in MsgId.values()) {
                transitionTable[Pair(state, input)] = action
            }
        }
    }

    fun puActionAllStates(input: MsgId, action: suspend () -> Unit) {
        for (state in LinkState.values()) {
            transitionTable[Pair(state, input)] = action
        }
    }

    fun action(state: LinkState, input: MsgId): suspend () -> Unit {
        return transitionTable[Pair(state, input)]!!
    }
}



