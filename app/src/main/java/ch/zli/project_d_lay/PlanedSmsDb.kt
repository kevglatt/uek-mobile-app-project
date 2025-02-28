package ch.zli.project_d_lay

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PlanedSms::class],
    version = 1
)
abstract class PlanedSmsDb: RoomDatabase() {
    abstract val dao: PlanedSmsDao
}