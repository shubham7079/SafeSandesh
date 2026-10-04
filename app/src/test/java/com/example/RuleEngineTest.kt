package com.example

import com.example.data.engine.Redactor
import com.example.data.engine.RuleEngine
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import com.example.data.model.Verdict
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleEngineTest {

    @Test
    fun testKycFraudScamMessage() {
        val message = "Dear customer your SBI account will be blocked today. Update PAN now at http://sbi-kyc-update.in/login"
        val result = RuleEngine.evaluate(message, CheckKind.TEXT, AppLanguage.EN)

        assertEquals(Verdict.SCAM, result.verdict)
        assertEquals("KYC fraud", result.scamType)
        assertTrue(result.impersonates?.contains("SBI") == true)
        assertTrue(result.reasons.isNotEmpty())
        assertTrue(result.whatToDo.isNotEmpty())
    }

    @Test
    fun testLotteryScamMessage() {
        val message = "Congratulations! You have won Rs 25 lakh in KBC lucky draw. Pay Rs 5000 processing fee to claim"
        val result = RuleEngine.evaluate(message, CheckKind.TEXT, AppLanguage.EN)

        assertEquals(Verdict.SCAM, result.verdict)
        assertTrue(result.scamType.contains("Lottery") || result.scamType.contains("Fee"))
        assertTrue(result.reasons.isNotEmpty())
    }

    @Test
    fun testGenuineOtpMessage() {
        val message = "Your OTP is 482913. Do not share it with anyone."
        val result = RuleEngine.evaluate(message, CheckKind.TEXT, AppLanguage.EN)

        assertEquals(Verdict.SAFE, result.verdict)
    }

    @Test
    fun testShortGreetingMessage() {
        val message = "Hi"
        val result = RuleEngine.evaluate(message, CheckKind.TEXT, AppLanguage.EN)

        assertEquals(Verdict.UNSURE, result.verdict)
    }

    @Test
    fun testFakeJobOfferScamMessage() {
        val message = "Work from home, earn Rs 5000 daily, pay Rs 999 registration, join Telegram"
        val result = RuleEngine.evaluate(message, CheckKind.TEXT, AppLanguage.EN)

        assertEquals(Verdict.SCAM, result.verdict)
        assertTrue(result.scamType.contains("job") || result.scamType.contains("Fee"))
    }

    @Test
    fun testGenuineDebitAlert() {
        val message = "Rs 1,200 debited from A/c XX1234 at ABC Store. Not you? Call your bank."
        val result = RuleEngine.evaluate(message, CheckKind.TEXT, AppLanguage.EN)

        assertEquals(Verdict.SAFE, result.verdict)
    }

    @Test
    fun testRedactorMasksOtpAndCardNumbers() {
        val input = "Your OTP is 482913 and account is 12345678. Pay Rs 5000 fee at https://secure.bank.com/pay"
        val redacted = Redactor.redact(input)

        assertTrue(redacted.masked)
        assertTrue(redacted.cleaned.contains("****"))
        assertTrue(redacted.cleaned.contains("Rs 5000"))
        assertTrue(redacted.cleaned.contains("https://secure.bank.com/pay"))
    }
}
