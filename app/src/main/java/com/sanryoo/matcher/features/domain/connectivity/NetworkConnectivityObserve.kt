package com.sanryoo.matcher.features.domain.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserve(context: Context) {

    companion object {
        enum class Status {
            Available, Unavailable, Losing, Lost
        }
    }

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    fun observe(): Flow<Status> {
        return callbackFlow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val callback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        trySend(Status.Available)
                    }

                    override fun onLosing(network: Network, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                        trySend(Status.Losing)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(Status.Lost)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        trySend(Status.Unavailable)
                    }
                }
                connectivityManager.registerDefaultNetworkCallback(callback)
                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }
        }.distinctUntilChanged()
    }

}