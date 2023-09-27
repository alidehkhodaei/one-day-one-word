package db

import constant.LINK_MORE_DETAILS
import java.sql.Connection

/**
 * DatabaseManager is responsible for managing interactions with a database connection.
 * It provides methods for querying and retrieving data from the connected database.
 *
 * @property connection The database connection to be used for executing queries.
 */
class DatabaseManager(private val connection:Connection) {

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

