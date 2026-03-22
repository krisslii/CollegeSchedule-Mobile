package com.example.collegeschedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {

    private val _schedule = MutableStateFlow<List<ScheduleByDateDto>>(emptyList())
    val schedule: StateFlow<List<ScheduleByDateDto>> = _schedule.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadSchedule(groupName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val calendar = Calendar.getInstance()
                val startFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val start = startFormatter.format(calendar.time)

                calendar.add(Calendar.WEEK_OF_YEAR, 4)
                val end = startFormatter.format(calendar.time)

                val scheduleData = repository.loadSchedule(groupName, start, end)
                _schedule.value = scheduleData
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки расписания"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}