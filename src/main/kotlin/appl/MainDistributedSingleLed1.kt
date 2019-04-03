package appl

import distributed.SingleLedProxyActor
import it.unibo.bls.devices.gui.ButtonAsGui
import it.unibo.kactor.Protocol
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

class BlsDistrNode1 {

    init {
        configure()
    }

    fun configure() {
        val chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM - NODE 1")

        chainSystem.addChainLink(SingleLedProxyActor("SingleLedProxyActor", Protocol.TCP, "localhost", 8012))

        println("-----------------------ADDING BUTTON----------------------------")
        chainSystem.addConcreteButton(ButtonAsGui.createButton("click me"))
    }

}

fun main() = runBlocking {
    println("BlsDistrNode1 | START ${sysUtil.curThread()}")
    BlsDistrNode1()
    println("BlsDistrNode1 | ENDS")
}