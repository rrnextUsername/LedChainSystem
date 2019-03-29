package enums

enum class LinkState {
    //StateMachineChainActor
    SLEEP_TOKEN,
    LIVE_TOKEN,
    PASSING_TOKEN,
    SLEEP,
    LIVE;

    override fun toString(): String {
        return name
    }
}