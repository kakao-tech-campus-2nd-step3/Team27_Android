package com.jnu.togetherpet.extensions

import com.jnu.togetherpet.extensions.formatDateTime
import com.jnu.togetherpet.extensions.formattingLocalDateTimeToString
import org.junit.Test
import org.junit.Assert.assertEquals
import java.time.LocalDateTime

/*UI 직접 테스트 대신 extension 테스트*/

class DateTimeExtensionsTest {
    @Test
    fun `시간 포맷 변환 테스트`() {
        val inputDateTime = LocalDateTime.of(2024, 11, 15, 14, 30, 0)
        val formattedTime = inputDateTime.formattingLocalDateTimeToString()
        assertEquals("14:30:45", formattedTime)
    }

    @Test
    fun `날짜,시간 포맷 변환 테스트`() {
        val input = "2024-11-15 14:30:45"
        val formattedDateTime = formatDateTime(input)
        assertEquals("2024.11.15 (금) 14:30", formattedDateTime)
    }
}
