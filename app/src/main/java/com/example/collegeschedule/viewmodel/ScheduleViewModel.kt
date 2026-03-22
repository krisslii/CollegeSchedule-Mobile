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
                // Загружаем расписание на МЕСЯЦ вперед
                val calendar = Calendar.getInstance()
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val start = formatter.format(calendar.time) // Сегодня

                calendar.add(Calendar.MONTH, 1) // + 1 месяц
                val end = formatter.format(calendar.time)

                println("Загрузка расписания для группы: $groupName")
                println("Период: $start - $end")

                val scheduleData = repository.loadSchedule(groupName, start, end)

                println("Получено записей: ${scheduleData.size}")

                _schedule.value = scheduleData
            } catch (e: Exception) {
                println("Ошибка загрузки: ${e.message}")
                e.printStackTrace()
                _error.value = "Ошибка: ${e.message ?: "Не удалось загрузить расписание"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}