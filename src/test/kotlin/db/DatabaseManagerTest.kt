package db

import constant.LINK_MORE_DETAILS
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

@ExtendWith(MockitoExtension::class)
class DatabaseManagerTest {

    /**
     * Tests for the 'fetchRandomWordFromDatabase'.
     * @see DatabaseManager.fetchRandomWordFromDatabase
     */

    @Mock
    private lateinit var connection: Connection

    @Mock
    private lateinit var statement: Statement

    @Mock
    private lateinit var resultSet: ResultSet

    @BeforeEach
    fun setUp() {
        `when`(connection.createStatement()).thenReturn(statement)
        `when`(statement.executeQuery(SQL_QUERY)).thenReturn(resultSet)
    }

    @Test
    fun `fetchRandomWordFromDatabase should return a string with a word and its meaning in markdown format`() {
        val expectedWord = "TestWord"
        val expectedMeaning = "TestMeaning"

        `when`(resultSet.next()).thenReturn(true)
        `when`(resultSet.getString("word")).thenReturn(expectedWord)
        `when`(resultSet.getString("meaning")).thenReturn(expectedMeaning)

        val data = getDataFromDatabse()

        assertTrue(data == "[$expectedWord]($LINK_MORE_DETAILS${expectedWord}_1) \n\n $expectedMeaning")
    }

    @Test
    fun `fetchRandomWordFromDatabase should return null when no word is found`() {
        `when`(resultSet.next()).thenReturn(false)

        val data = getDataFromDatabse()

        assertNull(data)
    }

    private fun getDataFromDatabse(): String? {
        val databaseManager = DatabaseManager(connection)
        return databaseManager.fetchRandomWordFromDatabase()
    }

    private companion object {
        const val SQL_QUERY = "SELECT * FROM Dictionary ORDER BY RAND() LIMIT 1"
    }
}
