package com.rick.assistant.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_memories")
data class MemoryEntity(
    @PrimaryKey val id: String,
    val content: String,
    val category: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isUserAuthorized: Boolean = true
)

@Dao
interface MemoryDao {
    @Query("SELECT * FROM user_memories WHERE isUserAuthorized = 1 ORDER BY createdAt DESC")
    suspend fun getAllMemories(): List<MemoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Delete
    suspend fun deleteMemory(memory: MemoryEntity)

    @Query("DELETE FROM user_memories")
    suspend fun clearAll()
}

class MemoryRepository(private val dao: MemoryDao) {
    suspend fun getAllAuthorizedMemories(): List<String> {
        return dao.getAllMemories().map { it.content }
    }

    suspend fun saveMemory(content: String, category: String = "geral") {
        dao.insertMemory(
            MemoryEntity(
                id = java.util.UUID.randomUUID().toString(),
                content = content,
                category = category
            )
        )
    }
}