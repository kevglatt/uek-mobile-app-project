package ch.zli.project_d_lay

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity
data class PlanedSms(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val isEnabled: Boolean,
    val time: LocalTime,
    val phoneNumber: String,
    val message: String
)
