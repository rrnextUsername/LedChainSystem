package distributed

import applLogic.AbstractChainActor
import applLogic.SingleLedActor
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.Protocol
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import stateMachine.MsgId

class SingleLedProxyServer(name: String, val protocol: Protocol, val portNum: Int, val dest: SingleLedActor) :
    AbstractChainActor(name) {


    //private var hostName: String
    private var factoryProtocol: FactoryProtocol?

    init {
        System.setProperty("inputTimeOut", "60000")
        factoryProtocol = MsgUtil.getFactoryProtocol(protocol)

        GlobalScope.launch(Dispatchers.IO) {
            autoMsg("${MsgId.ACTIVATE}", "start")
        }

    }

    override fun transitionTableSetup() {
        transitionTable.puActionAllStates(MsgId.ACTIVATE) { println("       ServerControl $name receives ${MsgId.ACTIVATE}  "); waitForConnection() }
    }

    override suspend fun actorBody(msg: ApplMessage) {
        transitionTable.action(state, MsgId.valueOf(msg.msgId()))
    }

    private fun waitForConnection() {
        try {
            while (true) {
                println("   LedServer $name | WAIT FOR CONNECTION")
                val conn = factoryProtocol?.createServerProtocolSupport(portNum) //BLOCKS
                handleConnection(conn)
            }
        } catch (e: Exception) {
            println("   LedServer $name | WARNING: ${e.message}")
        }
    }

    private fun handleConnection(conn: IConnInteraction?) {
        try {
            println("   ServerControl | handling new connection:$conn")
            while (true) {
                val msg = conn!!.receiveALine()       //BLOCKING
                println("   ServerControl | receives:$msg")
                val inputMsg = ApplMessage(msg)
                MsgUtil.sendMsg(inputMsg, dest)
            }
        } catch (e: Exception) {
            println("   ServerControl $name | handleConnection WARNING: ${e.message}")
        }
    }


}