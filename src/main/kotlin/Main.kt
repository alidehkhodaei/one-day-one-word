import api.ApiClient
import api.HttpRequest
import constant.JDBC_URL
import db.DatabaseManager
import java.net.http.HttpClient
import java.sql.DriverManager

fun main(args: Array<String>) {

    // Get bot token
    val token = args[0]

    // Init request api
    val httpClient = HttpClient.newHttpClient()
    val httpRequest = HttpRequest(httpClient)
    val api = ApiClient(httpRequest)

    // Fetch data from database
    val connection = DriverManager.getConnection(JDBC_URL, "root", "")
    val databaseManager = DatabaseManager(connection)
    val data = databaseManager.fetchRandomWordFromDatabase()

    // Call api
    api.sendToChannel(data!!, token)

}