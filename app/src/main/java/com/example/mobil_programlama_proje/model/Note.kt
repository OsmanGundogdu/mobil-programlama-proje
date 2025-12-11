package com.example.mobil_programlama_proje.model

import java.util.UUID

/**
 * Data class representing a Note entity.
 * 
 * @property id Unique identifier for the note (auto-generated UUID)
 * @property title The title of the note
 * @property content The content/body of the note
 * @property createdAt Timestamp when the note was created (milliseconds since epoch)
 * @property updatedAt Timestamp when the note was last updated (milliseconds since epoch)
 */
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
