package com.example.uangmasuk.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangmasuk.di.Injection
import com.example.uangmasuk.presentation.component.CashInItem
import com.example.uangmasuk.presentation.component.EmptyState
import com.example.uangmasuk.utils.CurrencyUtils
import com.example.uangmasuk.utils.DateUtils.groupByDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    val context = LocalContext.current
    var showPeriodSheet by remember { mutableStateOf(false) }


    val viewModel = remember {
        HomeViewModel(
            Injection.provideCashInRepository(context)
        )
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Uang Masuk") }
            )
        }
    ) { paddingValues ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.cashInList.isEmpty() -> {
                EmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onAturPeriodeClick = {
                        showPeriodSheet = true
                    },
                    onAddTransactionClick = {
                        onAddClick()
                    }
                )
            }

            else -> {
                val groupedData = uiState.cashInList.groupByDate()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    groupedData.forEach { (date, items) ->

                        // HEADER TANGGAL
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(date, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    CurrencyUtils.formatRupiah(items.sumOf { it.amount }),
                                    color = Color(0xFF2ECC71),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // ITEMS
                        items(items) { item ->
                            CashInItem(
                                cashIn = item,
                                onClick = { onItemClick(item.id) }
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = onAddClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Buat transaksi uang masuk")
                        }
                    }
                }
            }

        }
    }
    if (showPeriodSheet) {
        PeriodBottomSheet(
            onDismiss = { showPeriodSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var selectedOption by remember { mutableStateOf("custom") }
    var showDatePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Pilih Periode",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(16.dp))

            PeriodRadio("Hari ini", selectedOption) { selectedOption = "today" }
            PeriodRadio("Kemarin", selectedOption) { selectedOption = "yesterday" }
            PeriodRadio("7 hari terakhir", selectedOption) { selectedOption = "7days" }
            PeriodRadio("Pilih tanggal sendiri", selectedOption) { selectedOption = "custom" }

            if (selectedOption == "custom") {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("13 Apr 2024 - 23 Apr 2024")
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Terapkan")
            }
        }
    }

    if (showDatePicker) {
        DateRangePickerDialog(
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun PeriodRadio(
    text: String,
    selected: String,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected == when (text) {
                "Hari ini" -> "today"
                "Kemarin" -> "yesterday"
                "7 hari terakhir" -> "7days"
                else -> "custom"
            },
            onClick = onSelect
        )
        Text(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Terapkan", color = Color(0xFF2ECC71))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color(0xFF2ECC71))
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState
        )
    }
}


