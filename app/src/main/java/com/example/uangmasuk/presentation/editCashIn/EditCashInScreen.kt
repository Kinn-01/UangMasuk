package com.example.uangmasuk.presentation.editCashIn

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.uangmasuk.di.Injection
import com.example.uangmasuk.utils.copyImageToInternalStorage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCashInScreen(
    cashInId: Int,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember {
        EditCashInViewModel(
            Injection.provideCashInRepository(context)
        )
    }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val path = context.copyImageToInternalStorage(it)
                viewModel.onImageChange(path)
            }
        }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(cashInId) {
        viewModel.loadCashIn(cashInId)
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Uang Masuk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        CashInForm(
            paddingValues = padding,
            customerName = uiState.customerName,
            amount = uiState.amount,
            date = uiState.date,
            description = uiState.description,
            incomeType = uiState.incomeType,
            imagePath = uiState.imagePath,
            onCustomerChange = viewModel::onCustomerChange,
            onAmountChange = viewModel::onAmountChange,
            onDateChange = viewModel::onDateChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onIncomeTypeClick = { /* bottom sheet */ },
            onPickImage = {
                imagePicker.launch("image/*")
            },
            onSubmit = {
                viewModel.updateCashIn()
                onSuccess()
            },
            submitText = "Perbarui"
        )
    }
}
