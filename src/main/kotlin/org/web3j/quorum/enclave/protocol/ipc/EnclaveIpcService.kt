package org.web3j.quorum.enclave.protocol.ipc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.*
import org.slf4j.LoggerFactory
import org.web3j.protocol.ipc.IOFacade
import org.web3j.protocol.ipc.IpcService
import org.web3j.quorum.enclave.protocol.EnclaveService
import org.web3j.quorum.enclave.protocol.utils.RequestBuilder
import org.web3j.quorum.enclave.protocol.utils.ResponseParser

/**
 * IPC service layer
 */
abstract class EnclaveIpcService : EnclaveService {

    private val log = LoggerFactory.getLogger(IpcService::class.java)
    private val objectMapper = jacksonObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    override fun <S, T> send(request: S, path: String, responseType: Class<T>): T {
        val payload = objectMapper.writeValueAsString(request)
        val chunk = sendJsonRequest(payload, path)
        return objectMapper.readValue(chunk, responseType)
    }

    override fun sendRaw(request: String, path: String, from: String, to: List<String>): String {
        return sendRawJsonRequest(request, path, from, to)
    }

    override fun <S> send(request: S, path: String): Boolean {
        val payload = objectMapper.writeValueAsString(request)
        return sendJsonRequest(payload, path).isEmpty()
    }

    override fun sendJsonRequest(payload: String, path: String): String {
        val data = RequestBuilder.encodeJsonRequest(path, payload)
        return performIO(data)
    }

    override fun sendRawJsonRequest(payload: String, path: String, from: String, to: List<String>): String {
        val data = RequestBuilder.encodeRawJsonRequest(path, payload, from, to)
        return performIO(data)
    }

    override fun send(path: String): String {
        val data = RequestBuilder.encodeGetRequest(path)
        return performIO(data)
    }

    override fun performIO(data: String): String {
        val io = getIO()
        io.write(data)
        log.debug(">> " + data)

        val response = io.read()
        log.debug("<< " + response)

        return ResponseParser.parseChunkedResponse(response)
    }

    abstract fun getIO(): IOFacade
}
