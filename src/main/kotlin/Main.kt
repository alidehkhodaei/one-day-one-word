import api.ApiClient
import api.HttpRequest
import db.RandomWordFetcher.fetchRandomWordFromDatabase
import java.net.http.HttpClient

fun main(args: Array<String>) {

    // Get bot token
    val token = args[0]

    // Init request api
    val httpClient = HttpClient.newHttpClient()
    val httpRequest = HttpRequest(httpClient)
    val api = ApiClient(httpRequest)

    // Fetch data from database
    val data = fetchRandomWordFromDatabase()

    // Call api
    api.sendToChannel(data!!, token)

}