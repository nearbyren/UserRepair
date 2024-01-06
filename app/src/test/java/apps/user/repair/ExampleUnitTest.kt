package apps.user.repair

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val dateString = "2021-1-11"
        val dateFormat = SimpleDateFormat("yyyy-M-d")
        val date: Date = dateFormat.parse(dateString)
        System.out.println("我来了${date}")


    }
}