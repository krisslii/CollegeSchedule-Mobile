package com.example.collegeschedule.ui.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSearchScreen(
    groups: List<GroupItem>,
    onGroupSelected: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Фильтрация групп по поиску
    val filteredGroups = remember(searchQuery, groups) {
        if (searchQuery.isBlank()) {
            groups
        } else {
            groups.filter {
                it.groupName.contains(searchQuery, ignoreCase = true) ||
                        it.specialtyName?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Поле поиска
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск группы") },
            placeholder = { Text("Например: ИС-12") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        )

        // Список групп
        Text(
            text = "Найдено групп: ${filteredGroups.size}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredGroups) { group ->
                GroupItemCard(
                    group = group,
                    onClick = { onGroupSelected(group.groupName) }
                )
            }
        }
    }
}

@Composable
private fun GroupItemCard(
    group: GroupItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = group.groupName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${group.course} курс",
                style = MaterialTheme.typography.bodyMedium
            )
            group.specialtyName?.let { specialty ->
                Text(
                    text = specialty,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Data class для отображения группы
data class GroupItem(
    val groupId: Int,
    val groupName: String,
    val course: Int,
    val specialtyName: String?
)