package ch.zli.project_d_lay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlanedSmsViewModel(
    private val dao: PlanedSmsDao,
) : ViewModel() {

    private val _state = MutableStateFlow(PlanedSmsState())
    private val _allPlanedSms = dao.getAllPlanedSms()

    val state = combine(_state, _allPlanedSms) { state, allPlanedSms ->
        state.copy(
            allPlanedSms = allPlanedSms
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlanedSmsState())

    suspend fun onEvent(event: PlanedSmsEvent) {
        when (event) {
            is PlanedSmsEvent.DeletePlanedSms -> {
                dao.deletePlanedSms(event.planedSms)
            }

            is PlanedSmsEvent.SavePlanedSms -> {
                val time = state.value.time
                val phoneNumber = state.value.phoneNumber
                val isEnabled = state.value.isEnabled
                val message = state.value.message

                if (phoneNumber.isBlank() || message.isBlank() ) {
                    return
                }

                val planedSms = PlanedSms(
                    time = time,
                    isEnabled = isEnabled,
                    phoneNumber = phoneNumber,
                    message = message,
                )

                viewModelScope.launch {
                    dao.upsertPlanedSms(planedSms)
                }
            }

            is PlanedSmsEvent.EnablePlanedSms -> {
                _state.update {
                    it.copy(
                        isEnabled = true
                    )
                }
            }

            is PlanedSmsEvent.DisablePlanedSms -> {
                _state.update {
                    it.copy(
                        isEnabled = false
                    )
                }
            }

            is PlanedSmsEvent.SetPlanedSmsPhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }

            is PlanedSmsEvent.SetPlanedSmsTime -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }

            is PlanedSmsEvent.SetPlanedSmsMessage -> {
                _state.update {
                    it.copy(
                        message = event.message
                    )
                }
            }
        }
    }
}