package com.example.uangmasuk.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uangmasuk.data.local.entity.CashInEntity
import com.example.uangmasuk.di.Injection
import com.example.uangmasuk.presentation.component.CashInItem
import com.example.uangmasuk.presentation.component.EmptyState
import com.example.uangmasuk.utils.CurrencyUtils
import com.example.uangmasuk.utils.DateUtils
import com.example.uangmasuk.utils.DateUtils.groupByDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    val context = LocalContext.current
    var showPeriodSheet by remember { mutableStateOf(false) }

    val viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    Injection.provideCashInRepository(context)
                ) as T
            }
        }
    )


    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Uang Masuk") }
                )

                ActivePeriodChip(
                    period = uiState.period,
                    onClick = { showPeriodSheet = true }
                )
            }
        }
    ) { padding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.cashInList.isEmpty() -> {
                EmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onAturPeriodeClick = { showPeriodSheet = true },
                    onAddTransactionClick = onAddClick
                )
            }

            else -> {
                CashInListContent(
                    modifier = Modifier.padding(padding),
                    cashInList = uiState.cashInList,
                    onItemClick = onItemClick,
                    onAddClick = onAddClick
                )
            }
        }
    }

    if (showPeriodSheet) {
        PeriodBottomSheet(
            onApply = { period ->
                viewModel.applyPeriod(period)
                showPeriodSheet = false
            },
            onDismiss = { showPeriodSheet = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodBottomSheet(
    onApply: (PeriodFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("today") }
    var showDatePicker by remember { mutableStateOf(false) }
    var customRange by remember { mutableStateOf<Pair<Long, Long>?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text("Pilih Periode", fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(12.dp))

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
                    Text(
                        customRange?.let {
                            "${DateUtils.formatDate(it.first)} - ${DateUtils.formatDate(it.second)}"
                        } ?: "Pilih tanggal"
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val period = when (selectedOption) {
                        "today" -> PeriodFilter.Today
                        "yesterday" -> PeriodFilter.Yesterday
                        "7days" -> PeriodFilter.Last7Days
                        else -> {
                            val range = customRange ?: return@Button
                            PeriodFilter.Custom(range.first, range.second)
                        }
                    }
                    onApply(period)
                }
            ) {
                Text("Terapkan")
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        DateRangePickerDialog(
            onResult = { start, end ->
                customRange = start to end
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun CashInListContent(
    modifier: Modifier = Modifier,
    cashInList: List<CashInEntity>,
    onItemClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val groupedData = cashInList.groupByDate()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        groupedData.forEach { (date, items) ->

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
    onResult: (Long, Long) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val start = dateRangePickerState.selectedStartDateMillis
                    val end = dateRangePickerState.selectedEndDateMillis

                    if (start != null && end != null) {
                        onResult(start, end)
                    }
                }
            ) {
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

@Composable
fun ActivePeriodChip(
    period: PeriodFilter,
    onClick: () -> Unit
) {
    val text = when (period) {
        PeriodFilter.Today -> "Hari ini"
        PeriodFilter.Yesterday -> "Kemarin"
        PeriodFilter.Last7Days -> "7 hari terakhir"
        is PeriodFilter.Custom -> {
            DateUtils.formatDateRange(period.start, period.end)
        }
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF2ECC71)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF2ECC71),
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = text,
                color = Color(0xFF2ECC71),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF2ECC71)
            )
        }
    }
}




