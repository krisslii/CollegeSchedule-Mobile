package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    currentGroup: String?,
    onGroupSelect: () -> Unit,
    scheduleViewModel: ScheduleViewModel
) {
    val schedule by scheduleViewModel.schedule.collectAsState()
    val isLoading by scheduleViewModel.isLoading.collectAsState()
    val error by scheduleViewModel.error.collectAsState()

    LaunchedEffect(currentGroup) {
        currentGroup?.let {
            scheduleViewModel.loadSchedule(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Текущая группа",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentGroup ?: "Не выбрана",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(onClick = onGroupSelect) {
                    Text("Выбрать")
                }
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Загрузка расписания...")
                    }
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error ?: "Ошибка",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { currentGroup?.let { scheduleViewModel.loadSchedule(it) } }) {
                            Text("Повторить")
                        }
                    }
                }
            }

            currentGroup == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Выберите группу для просмотра расписания",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            schedule.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Расписание не найдено",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Проверьте название группы или подключение к серверу",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(schedule) { day ->
                        ScheduleDayCard(day = day)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleDayCard(day: ScheduleByDateDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${day.weekday}, ${day.lessonDate}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (day.lessons.isEmpty()) {
                Text(
                    text = "Нет занятий",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                day.lessons.forEachIndexed { index, lesson ->
                    if (index > 0) Spacer(modifier = Modifier.height(8.dp))

                    LessonItem(lesson = lesson)
                }
            }
        }
    }
}

@Composable
private fun LessonItem(lesson: com.example.collegeschedule.data.dto.LessonDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Пара ${lesson.lessonNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = lesson.time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            lesson.subject?.let { subject ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subject,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            lesson.teacher?.let { teacher ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = teacher,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            lesson.classroom?.let { classroom ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Аудитория: $classroom",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}