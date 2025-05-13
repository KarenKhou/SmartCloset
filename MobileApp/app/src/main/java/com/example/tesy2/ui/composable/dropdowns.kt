package com.example.tesy2.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tesy2.data.models.Closet


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonDropdown(
    season: String,
    onSeasonSelected: (String) -> Unit,
    seasonError: Boolean
) {
    val options = listOf("Winter", "Spring", "Summer", "Fall")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = season,
            onValueChange = {},
            readOnly = true,
            label = { Text("Season") },
            isError = seasonError,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onSeasonSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleDropdown(
    style: String,
    onStyleSelected: (String) -> Unit,
    styleError: Boolean
) {
    val options = listOf("Formal", "Casual", "Both")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = style,
            onValueChange = {},
            readOnly = true,
            label = { Text("Style") },
            isError = styleError,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onStyleSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClosetDropdown(
    closetList: List<Closet>,
    selectedCloset: Closet?,
    onClosetSelected: (Closet) -> Unit,
    closetError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCloset?.closet_name ?: "Select a Dressing",
            onValueChange = {},
            readOnly = true,
            label = { Text("Votre dressing") },
            isError = closetError,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu( // ✅ Corrected here
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            closetList.forEach { closet ->
                DropdownMenuItem(
                    text = { Text(closet.closet_name) },
                    onClick = {
                        onClosetSelected(closet)
                        expanded = false
                    }
                )
            }
        }
    }
}