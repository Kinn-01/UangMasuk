package com.example.uangmasuk.presentation.editCashIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

@Composable
fun CashInForm(
    paddingValues: PaddingValues,
    customerName: String,
    amount: String,
    date: String,
    description: String,
    incomeType: String,
    imagePath: String?,
    onCustomerChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIncomeTypeClick: () -> Unit,
    onPickImage: () -> Unit,
    onSubmit: () -> Unit,
    submitText: String,
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = customerName,
            onValueChange = onCustomerChange,
            label = { Text("Terima Dari") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Keterangan") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text("Jumlah") },
            leadingIcon = { Text("Rp") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = incomeType,
            onValueChange = {},
            label = { Text("Jenis Pendapatan") },
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowRight, null)
            },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onIncomeTypeClick() }
        )

        Text("Upload bukti transfer / nota / kwitansi")

        OutlinedButton(
            onClick = onPickImage,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("Pilih Gambar")
        }

        imagePath?.let {
            AsyncImage(
                model = File(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text(submitText)
        }
    }
}
