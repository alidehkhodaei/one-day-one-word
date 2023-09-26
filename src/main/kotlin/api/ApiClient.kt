package api

import constant.*

/**
 * A class for interacting with Telegram Bot API.
 *
 * @property httpRequest The instance of `api.HttpRequest` used to perform HTTP requests.
 */
class ApiClient(private val httpRequest: HttpRequest) {

    /**
     * Sends a text message to a channel using the Telegram Bot API.
     *
     * @param data The text message to send.
     * @param token The authorization token for the Telegram Bot.
     * @return The response body from the API call.
     */
    fun sendToChannel(data: String, token: String): String {
        val requestBody = mapOf(
            KEY_CHAT_ID to CHANNEL_ID,
            KEY_TEXT to data,
            KEY_PARSE_MODE to MARKDOWN,
            KEY_DISABLE_WEB_PAGE_PREVIEW to "true"
        )

        val apiUrl = String.format(
            "${TELEGRAM_API_BASE_URL}bot%s/$SEND_MESSAGE_URL",
            token
        )

        val response = httpRequest.performPostHttpRequest(apiUrl, requestBody)
        return response.body()
    }

}