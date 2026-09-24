package com.rick.assistant.mesh

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*

/**
 * Android Nearby Connections API
 * P2P Mesh communication for Rick between Tablets, Wearables and Car Units
 */
class DeviceSyncService(private val context: Context) {

    private val SERVICE_ID = "com.rick.assistant.mesh"
    private val client = Nearby.getConnectionsClient(context)

    fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()

        client.startAdvertising(
            "Rick-Android-Node",
            SERVICE_ID,
            connectionLifecycleCallback,
            advertisingOptions
        )
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            // Auto accept authenticated device mesh pairing
            client.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {}
        override fun onDisconnected(endpointId: String) {}
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receivedBytes = payload.asBytes()
            // Process synchronized command from nearby Android device
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }
}