package com.example.myandroidtemplate.utils

object FeatureFlags {

    /**
     * Toggle mock API responses.
     *
     * true  -> mock network layer (template / development)
     * false -> real backend
     */
    const val USE_MOCK_API = true

    /**
     * Artificial delay for mock responses (ms)
     */
    const val MOCK_API_DELAY_MS = 800L

    /**
     * Enable verbose network logging
     */
    const val ENABLE_NETWORK_LOGS = true
}