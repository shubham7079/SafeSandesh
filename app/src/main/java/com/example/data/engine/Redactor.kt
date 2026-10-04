package com.example.data.engine

data class RedactResult(
    val cleaned: String,
    val masked: Boolean
)

object Redactor {

    // Regex to match URLs so we don't modify URL contents
    private val urlPattern = Regex(
        """(https?://[^\s]+|www\.[^\s]+|[a-zA-Z0-9-]+\.(?:com|in|co\.in|org|net|xyz|top|live|app|gov\.in|nic\.in)[^\s]*)""",
        RegexOption.IGNORE_CASE
    )

    // Regex to match money and currency figures like Rs. 5000, Rs 25,000, 1,200 INR, ₹500
    private val currencyPattern = Regex(
        """(?i)(?:rs\.?|inr|₹)\s*[\d,]+(?:\.\d+)?|[\d,]+(?:\.\d+)?\s*(?:rs\.?|inr|₹|rupees|रुपये)"""
    )

    // Regex to match 4 or more digits, including groups of digits separated by single space or hyphen
    private val sensitiveDigitsPattern = Regex(
        """\b\d{1,4}(?:[-\s]\d{2,4}){1,3}\b|\b\d{4,}\b"""
    )

    fun redact(input: String): RedactResult {
        if (input.isBlank()) return RedactResult(input, false)

        var wasMasked = false

        // Step 1: Protect URLs and Currency amounts by tokenizing
        val preservedTokens = mutableListOf<String>()

        var textWithPlaceholders = urlPattern.replace(input) { match ->
            val placeholder = "___URL_TOKEN_${preservedTokens.size}___"
            preservedTokens.add(match.value)
            placeholder
        }

        textWithPlaceholders = currencyPattern.replace(textWithPlaceholders) { match ->
            val placeholder = "___CURR_TOKEN_${preservedTokens.size}___"
            preservedTokens.add(match.value)
            placeholder
        }

        // Step 2: Mask sensitive 4+ digit sequences in remaining text
        val maskedText = sensitiveDigitsPattern.replace(textWithPlaceholders) { match ->
            wasMasked = true
            "****"
        }

        // Step 3: Restore preserved tokens
        var restoredText = maskedText
        preservedTokens.forEachIndexed { index, token ->
            restoredText = restoredText.replace("___URL_TOKEN_${index}___", token)
            restoredText = restoredText.replace("___CURR_TOKEN_${index}___", token)
        }

        return RedactResult(cleaned = restoredText, masked = wasMasked)
    }
}
