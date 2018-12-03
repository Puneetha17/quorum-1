package org.web3j.quorum.core

import org.assertj.core.api.Assertions.*
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.web3j.quorum.*

/**
 * Useful integration tests for verifying Enclave transactions.
 *
 * <p>
 *     To use, start up 2 Tessera instances with valid config and
 * hardcode the IPC path to connect to in constellationIpcPath1 and constellationIpcPath2
 * variables below.
 *
 * <p>
 */
@Ignore
class TesseraServiceTest : Helper() {
    @Test
    fun testUpCheck() {
        val upCheckResponse = upCheckTessera.upCheck()
        assertTrue(upCheckResponse)
    }

    @Test
    fun testStoreRawRequest() {
        val payload = PAYLOAD
        val from = nodesT[0].publicKeys[0]

        val storeResponse = storeRawTessera.storeRawRequest(payload, from)
        println(storeResponse.key)
        val key = storeResponse.key
        assertThat(key).hasSize(88)
    }

    @Test
    @Throws(Exception::class)
    fun testNodes() {
        for (count in 0..0) {
            for (i in 0..0) {
                val sourceNode = nodesT[i]
                val destNode = nodesT[(i + 1) % nodesT.size]

                val keyFile = "keyfiles/key" + (i + 1).toString()
                testRawTransactionsWithGreeterContract(sourceNode, destNode, keyFile, storeRawTessera)
                runPrivateHumanStandardTokenTest(sourceNode, destNode, keyFile, storeRawTessera)

            }
        }
    }


}
