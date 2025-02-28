package ch.zli.project_d_lay

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanedSmsDao {
    @Upsert
    suspend fun upsertPlanedSms(planedSms: PlanedSms)
    @Delete
    suspend fun deletePlanedSms(planedSms: PlanedSms)
    @Query("SELECT * FROM planedsms")
    fun getAllPlanedSms(): Flow<List<PlanedSms>>
}