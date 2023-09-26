package db

import constant.LINK_MORE_DETAILS
import java.sql.DriverManager

/**
 * Object responsible for fetching random words from a database.
 */
object RandomWordFetcher {

    // JDBC URL for the H2 database
    private const val JDBC_URL = "jdbc:h2:./src/main/resources/words"

    // Lazy initialization of the database connection
    private val connection by lazy {
        DriverManager.getConnection(JDBC_URL, "root", "")
    }

    /**
     * Fetches a random word and its meaning from the database.
     *
     * @return A formatted string containing the random word and its meaning, or null if no word is found.
     */
    fun fetchRandomWordFromDatabase(): String? {
        connection.use { connection ->
            val sql = "SELECT * FROM Dictionary ORDER BY RAND() LIMIT 1"
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(sql)
            return if (resultSet.next()) {
                val word = resultSet.getString("word")
                val meaning = resultSet.getString("meaning")
                "[$word]($LINK_MORE_DETAILS${word}_1) \n\n $meaning"
            } else {
                null
            }
        }
    }
}


