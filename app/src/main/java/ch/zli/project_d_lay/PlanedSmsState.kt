package ch.zli.project_d_lay

import java.time.LocalTime

data class PlanedSmsState(
    val allPlanedSms: List<PlanedSms> = emptyList(),
    val isEnabled: Boolean = false,
    val time: LocalTime = LocalTime.of(0, 0),
    val phoneNumber: String = "",
    val message: String = ""
)
