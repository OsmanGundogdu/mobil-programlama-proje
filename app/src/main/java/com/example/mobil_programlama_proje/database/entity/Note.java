package com.example.mobil_programlama_proje.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userOwnerId"),

                @ForeignKey(entity = Category.class,
                        parentColumns = "categoryId",
                        childColumns = "categoryOwnerId")
        }
)
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int noteId;

    public String title;
    public String content;

    public int userOwnerId;
    public int categoryOwnerId;
}
