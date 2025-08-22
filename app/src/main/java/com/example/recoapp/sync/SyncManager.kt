package com.example.recoapp.sync

import android.content.Context
import com.example.recoapp.data.AppDatabase
import com.example.recoapp.data.Waste
import com.example.recoapp.network.PushRequest
import com.example.recoapp.network.SyncRepository
import com.example.recoapp.network.WasteDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncManager(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val wasteDao = db.wasteDao()
    private val syncPrefs = SyncPrefs(context)
    private val repo = SyncRepository(context)

    suspend fun sync(): Unit = withContext(Dispatchers.IO) {
        val since = syncPrefs.getLastSync()
        val now = System.currentTimeMillis()

        
        val localChanges = wasteDao.getModifiedSince(since)
        if (localChanges.isNotEmpty()) {
            val pushDtos = localChanges.map { it.toDto() }
            try {
                repo.api().push(PushRequest(items = pushDtos))
            } catch (e: Exception) {
                
                return@withContext
            }
        }

        
        val pulled = try {
            repo.api().pull(since).items
        } catch (e: Exception) {
            return@withContext
        }

        
        for (remote in pulled) {
            val local = wasteDao.findById(remote.id)
            if (local == null) {
                
                val toInsert = remote.toEntity()
                if (toInsert != null) {
                    wasteDao.insert(toInsert)
                }
            } else {
                if (remote.modifiedAt > local.modifiedAt) {
                    
                    if (remote.deleted == true) {
                        
                        val updated = local.copy(
                            type = remote.type ?: local.type,
                            quantity = remote.quantity ?: local.quantity,
                            location = remote.location ?: local.location,
                            date = java.util.Date(remote.date ?: local.date.time),
                            comment = remote.comment ?: local.comment,
                            deleted = true,
                            modifiedAt = remote.modifiedAt
                        )
                        wasteDao.insert(updated)
                    } else {
                        val updated = local.copy(
                            type = remote.type ?: local.type,
                            quantity = remote.quantity ?: local.quantity,
                            location = remote.location ?: local.location,
                            date = java.util.Date(remote.date ?: local.date.time),
                            comment = remote.comment ?: local.comment,
                            deleted = false,
                            modifiedAt = remote.modifiedAt
                        )
                        wasteDao.insert(updated)
                    }
                }
            }
        }

        
        syncPrefs.setLastSync(now)
    }
}

private fun Waste.toDto(): WasteDto = WasteDto(
    id = id,
    type = type,
    quantity = quantity,
    location = location,
    date = date.time,
    comment = comment,
    deleted = deleted,
    modifiedAt = modifiedAt
)

private fun WasteDto.toEntity(): Waste? {
    
    
    val d = (date ?: System.currentTimeMillis())
    val t = type ?: ""
    val loc = location ?: ""
    val qty = quantity ?: 0.0
    return Waste(
        id = id,
        type = t,
        quantity = qty,
        location = loc,
        date = java.util.Date(d),
        comment = comment,
        modifiedAt = modifiedAt,
        deleted = deleted
    )
}
