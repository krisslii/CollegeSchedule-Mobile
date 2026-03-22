package com.example.collegeschedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.ui.group.GroupItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupViewModel(private val repository: ScheduleRepository) : ViewModel() {

    private val _groups = MutableStateFlow<List<GroupItem>>(emptyList())
    val groups: StateFlow<List<GroupItem>> = _groups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadGroups() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val groupDtos = repository.getAllGroups()
                _groups.value = groupDtos.map { dto ->
                    GroupItem(
                        groupId = dto.groupId,
                        groupName = dto.groupName,
                        course = dto.course,
                        specialtyName = dto.specialtyName
                    )
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки групп"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}