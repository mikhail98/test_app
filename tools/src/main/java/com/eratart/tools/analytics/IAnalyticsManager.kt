package com.eratart.tools.analytics

interface IAnalyticsManager {

    /**
     * Send event to firebase analytics
     *
     * @param eventName is tracking event name
     */
    fun logEvent(eventName: String)

}