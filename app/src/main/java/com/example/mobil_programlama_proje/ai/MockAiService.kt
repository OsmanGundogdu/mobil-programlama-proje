package com.example.mobil_programlama_proje.ai

import kotlinx.coroutines.delay

/**
 * Mock AI Service for testing without real API
 * Simulates AI summarization locally
 */
class MockAiService {

    /**
     * Simulate AI summarization with local logic
     * @param noteContent The full note content to summarize
     * @param callback Callback for async result
     */
    suspend fun summarizeNote(noteContent: String, callback: SummarizeCallback) {
        try {
            // Simulate network delay
            delay(1500)

            val summary = generateMockSummary(noteContent)
            callback.onSuccess(summary)
        } catch (e: Exception) {
            callback.onError(e.message ?: "Özetleme başarısız")
        }
    }

    private fun generateMockSummary(content: String): String {
        // Simple rule-based summarization
        val sentences = content.split(".", "!", "?")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        return when {
            sentences.size <= 2 -> content
            sentences.size <= 5 -> {
                sentences.take(2).joinToString(". ") + "."
            }
            else -> {
                // Take first and last sentences
                "${sentences.first()}. ${sentences.last()}."
            }
        }.let {
            if (it.length > 200) {
                it.take(197) + "..."
            } else {
                it
            }
        }
    }

    interface SummarizeCallback {
        fun onSuccess(summary: String)
        fun onError(error: String)
    }
}