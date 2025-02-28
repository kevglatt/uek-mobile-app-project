package ch.zli.project_d_lay

import java.time.LocalTime

sealed interface PlanedSmsEvent {
    data object SavePlanedSms: PlanedSmsEvent
    data class EnablePlanedSms(val isEnabled: Boolean): PlanedSmsEvent
    data class DisablePlanedSms(val isEnabled: Boolean): PlanedSmsEvent
    data class SetPlanedSmsTime(val time: LocalTime): PlanedSmsEvent
    data class SetPlanedSmsPhoneNumber(val phoneNumber: String): PlanedSmsEvent
    data class SetPlanedSmsMessage(val message: String): PlanedSmsEvent
    data class DeletePlanedSms(val planedSms: PlanedSms): PlanedSmsEvent
}