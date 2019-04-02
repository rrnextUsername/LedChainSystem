package stateMachine

enum class LinkState {
    //StateMachineChainActor
    SLEEP_TOKEN,
    LIVE_TOKEN,
    PASSING_TOKEN,

    //StateMachineLinkActor && AbstractChainActor
    SLEEP,
    LIVE,

    //SingleLedActor
    INIT,
    LED_ON,
    LED_OFF,
    END;

    override fun toString(): String {
        return name
    }
}