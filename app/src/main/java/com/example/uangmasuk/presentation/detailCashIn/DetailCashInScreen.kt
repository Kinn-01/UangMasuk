package com.example.uangmasuk.presentation.detailCashIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.uangmasuk.bottomSheet.DeleteConfirmationDialog
import com.example.uangmasuk.di.Injection
import com.example.uangmasuk.utils.CurrencyUtils
import com.example.uangmasuk.utils.DateUtils
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCashInScreen(
    cashInId: Int,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    onDeleteSuccess: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember {
        DetailCashInViewModel(
            Injection.provideCashInRepository(context)
        )
    }

    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(cashInId) {
        viewModel.loadCashIn(cashInId)
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                viewModel.deleteCashIn(context)
                showDeleteDialog = false
                onDeleteSuccess()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Uang Masuk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { paddingValues ->

        uiState.cashIn?.let { data ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DetailItem("Tanggal Dibuat", DateUtils.formatDate(data.transactionDate))
                DetailItem("Masuk Ke", "Kasir perangkat ke-49")
                DetailItem("Terima Dari", data.customerName)
                DetailItem("Keterangan", data.description.ifBlank { "-" })
                DetailItem("Jumlah", CurrencyUtils.formatRupiah(data.amount))
                DetailItem("Jenis Pendapatan", data.incomeType)

                Text(
                    "Bukti transfer / nota / kwitansi",
                    style = MaterialTheme.typography.labelMedium
                )

                if (!data.imagePath.isNullOrEmpty()) {
                    AsyncImage(
                        model = File(data.imagePath),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onImageClick(data.imagePath) }
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { onEdit(data.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Ubah transaksi")
                }

                TextButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Hapus", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
