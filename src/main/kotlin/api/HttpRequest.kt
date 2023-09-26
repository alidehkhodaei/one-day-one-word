package api

import constant.ACCEPT_HEADER
import constant.CONTENT_TYPE
import constant.JSON_CONTENT_TYPE
import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * A class for making HTTP requests using the HttpClient provided by the Java standard library.
 *
 * @property client The HttpClient instance used to send HTTP requests.
 */
class HttpRequest(private val client: HttpClient) {

    /**
     * Performs a POST HTTP request.
     *
     * @param url The specific API endpoint to call.
     * @param requestBody The data to be sent in the request body.
     * @return The HTTP response containing the API data.
     */
    fun performPostHttpRequest(url: String, requestBody: Map<String, String>): HttpResponse<String> {
        val jsonBody = Gson().toJson(requestBody)
        val request = HttpRequest.newBuilder(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .header(ACCEPT_HEADER, JSON_CONTENT_TYPE)
            .header(CONTENT_TYPE, JSON_CONTENT_TYPE)
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

}