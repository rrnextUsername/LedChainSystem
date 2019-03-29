package enums

enum class MsgId {
    //ChainActor
    START,
    STOP,

    //StateMachineChainActor
    ACTIVATE,
    DEACTIVATE,
    DO_ON,
    DO_OFF,
    DONE,

    //None
    RESUME,
    SUSPEND,

    //All
    ON,
    OFF,
    CLICK;
}