package appl

import applLogic.SingleLedActor
import distributed.SingleLedProxyServer
import it.unibo.kactor.Protocol
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking
import segments.LedSegmentAdapter

class BlsDistrNode2 {

    init {
        configure()
    }

    fun configure() {
        val chainSystem = SegChainFramework.createTheSystem("CHAIN_SYSTEM - NODE 2")

        var actor = SingleLedActor("seg1", 500)
        chainSystem.addChainLink(actor)
        chainSystem.addConcreteLed(chainSystem.lastLink, LedSegmentAdapter("seg2", 20, 10, 320, 0))

        val servercontrol = SingleLedProxyServer("serverControl", Protocol.TCP, 8012, actor)
    }

}

fun main() = runBlocking {
    println("BlsDistrNode2 | START ${sysUtil.curThread()}")
    BlsDistrNode2()
    println("BlsDistrNode2 | ENDS")
}


