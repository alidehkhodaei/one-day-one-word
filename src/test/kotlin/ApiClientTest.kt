import api.ApiClient
import constant.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Flow
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach

@ExtendWith(MockitoExtension::class)
class ApiClientTest {

    /**
     * Tests for the 'sendToChannel'.
     * @see ApiClient.sendToChannel
     */

    @Mock
    private lateinit var mockResponse: HttpResponse<String>

    @Captor
    private lateinit var captor: ArgumentCaptor<HttpRequest>

    @Mock
    private lateinit var mockClient: HttpClient

    private lateinit var apiClient: ApiClient

    @BeforeEach
    fun setUp() {
        val httpRequest = api.HttpRequest(mockClient)
        apiClient = ApiClient(httpRequest)

        `when`(mockClient.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
            .thenReturn(mockResponse)

        `when`(mockResponse.body()).thenReturn("{\"ok\":true")

        apiClient.sendToChannel("Test data", token)
    }

    @Test
    fun `test Request Body`() {
        val expectedRequestBody = mapOf(
            KEY_CHAT_ID to CHANNEL_ID,
            KEY_TEXT to "Test data",
            KEY_PARSE_MODE to MARKDOWN,
            KEY_DISABLE_WEB_PAGE_PREVIEW to "true"
        )

        val capturedRequest = verifyAndCaptureHttpResponse()

        assertEquals(expectedRequestBody, capturedRequest.extractActualRequestBody())
    }

    @Test
    fun `test Request Url`() {
        val capturedRequest = verifyAndCaptureHttpResponse()
        val expectedUrl = "${TELEGRAM_API_BASE_URL}bot$token/$SEND_MESSAGE_URL"
        assertEquals(expectedUrl, capturedRequest.uri().toString())
    }

    @Test
    fun `test Request Method`() {
        val capturedRequest = verifyAndCaptureHttpResponse()
        assertEquals(POST_REQUEST, capturedRequest.method())
    }

    @Test
    fun `test Request Headers`() {
        val capturedRequest = verifyAndCaptureHttpResponse()
        assertEquals(JSON_CONTENT_TYPE, capturedRequest.headers().firstValue(ACCEPT_HEADER).orElse(null))
        assertEquals(JSON_CONTENT_TYPE, capturedRequest.headers().firstValue(CONTENT_TYPE).orElse(null))
    }

    @Test
    fun `test Response Body`() =
        assertEquals("{\"ok\":true", mockResponse.body())

    private fun verifyAndCaptureHttpResponse(): HttpRequest {
        verify(mockClient).send(captor.capture(), eq(HttpResponse.BodyHandlers.ofString()))
        return captor.value
    }

    private fun HttpRequest.extractActualRequestBody(): Map<String, Any> {
        val body = this.bodyPublisher().get()
        val flowSubscriber: FlowSubscriber<ByteBuffer> = FlowSubscriber()
        body.subscribe(flowSubscriber)
        val actual = String(flowSubscriber.getBodyItems()[0].array())
        return JSONObject(actual).toMap()
    }

    companion object {
        private const val token = "1234"
    }

}

/*
    A comment for me!
    For checking request body every request, I created this class. Visit this link:
    https://stackoverflow.com/questions/59342963/how-to-test-java-net-http-java-11-requests-bodypublisher
 */
private class FlowSubscriber<T> : Flow.Subscriber<T> {
    private val latch = CountDownLatch(1)
    private val bodyItems: MutableList<T> = ArrayList()

    fun getBodyItems(): List<T> {
        try {
            latch.await()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        return bodyItems
    }

    override fun onSubscribe(subscription: Flow.Subscription) {
        subscription.request(Long.MAX_VALUE)
    }

    override fun onNext(item: T) {
        bodyItems.add(item)
    }

    override fun onError(throwable: Throwable) {
        latch.countDown()
    }

    override fun onComplete() {
        latch.countDown()
    }
}