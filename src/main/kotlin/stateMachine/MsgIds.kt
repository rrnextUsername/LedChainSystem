package stateMachine

enum class MsgId {
    //StateMachineChainActor
    ACTIVATE,
    DEACTIVATE,
    TOKEN,
    PASS_TOKEN,

    //All
    ON,
    OFF,
    CLICK;

    override fun toString(): String {
        return name
    }
}