package distributed

import applLogic.AbstractChainActor
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.Protocol
import stateMachine.MsgId


class SingleLedProxyActor(
    name: String,
    private val protocol: Protocol,
    private val hostName: String,
    private val portNum: Int
) : AbstractChainActor(name) {


    private var conn: IConnInteraction? = null
    private var currentMsg: ApplMessage? = null

    init {
        configure()
    }

    private fun configure() {
        when (protocol) {
            Protocol.TCP, Protocol.UDP ->
                conn = MsgUtil.getConnection(protocol, hostName, portNum, name)
            Protocol.SERIAL -> conn = MsgUtil.getConnectionSerial("", 9600)
            else -> println("::ERROR:: protocol unknown ::ERROR::")
        }
    }

    override fun transitionTableSetup() {
        transitionTable.putDefaultAction { println("   ProxyControl $name |  msg= $currentMsg ");conn?.sendALine("$currentMsg") }
    }

    override suspend fun actorBody(msg: ApplMessage) {
        currentMsg = msg
        transitionTable.action(state, MsgId.valueOf(msg.msgId()))
    }

}