package com.example.uangmasuk.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Uang Masuk") },
        text = { Text("Apakah Anda ingin menghapus detail uang masuk ini?") },
        confirmButton = {
            OutlinedButton(
                onClick = onConfirm,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Red
                ),
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Batalkan")
            }
        }

    )
}