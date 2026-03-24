package com.chriscorp.celer

import android.app.Application
import com.chriscorp.celer.nostr.RelayDirectory
import com.chriscorp.celer.ui.theme.ThemePreferenceManager
import com.chriscorp.celer.net.TorManager

/**
 * Main application class for bitchat Android
 */
class BitchatApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Tor first so any early network goes over Tor
        try { TorManager.init(this) } catch (_: Exception) { }

        // Initialize relay directory (loads assets/nostr_relays.csv)
        RelayDirectory.initialize(this)

        // Initialize favorites persistence early so MessageRouter/NostrTransport can use it on startup
        try {
            com.chriscorp.celer.favorites.FavoritesPersistenceService.initialize(this)
        } catch (_: Exception) { }

        // Warm up Nostr identity to ensure npub is available for favorite notifications
        try {
            com.chriscorp.celer.nostr.NostrIdentityBridge.getCurrentNostrIdentity(this)
        } catch (_: Exception) { }

        // Initialize theme preference
        ThemePreferenceManager.init(this)

        // Initialize debug preference manager (persists debug toggles)
        try { com.chriscorp.celer.ui.debug.DebugPreferenceManager.init(this) } catch (_: Exception) { }

        // TorManager already initialized above
    }
}
