package com.example.uangmasuk.presentation.cashIn

import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.uangmasuk.di.Injection
import java.io.File
import kotlin.let
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.uangmasuk.data.local.entity.CustomerEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashInScreen(
    viewModel: CashInViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onOpenCustomerList: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
//    val viewModel = remember {
//        CashInViewModel(
//            Injection.provideCashInRepository(context),
//            Injection.provideCustomerRepository(context)
//        )
//    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onImageSelected(context, it)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    var showIncomeTypeSheet by remember { mutableStateOf(false) }
    var showImagePickerSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Transaksi Uang Masuk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Masuk ke (Readonly)
            OutlinedTextField(
                value = "Kasir Perangkat ke-49",
                onValueChange = {},
                label = { Text("Masuk ke") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            // Pelanggan
            OutlinedTextField(
                value = uiState.customerName,
                onValueChange = viewModel::onCustomerNameChange,
                label = { Text("Terima Dari") },
                placeholder = { Text("Masukkan nama atau pilih pelanggan") },
                trailingIcon = {
                    IconButton(onClick = onOpenCustomerList) {
                        Icon(
                            Icons.Default.Contacts,
                            contentDescription = "Pilih Pelanggan"
                        )
                    }
                }
                ,
                modifier = Modifier.fillMaxWidth()
            )

            // Keterangan
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Keterangan") },
                placeholder = { Text("Contoh: Menjual kemasan kardus") },
                modifier = Modifier.fillMaxWidth()
            )

            // Jumlah
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Jumlah") },
                leadingIcon = { Text("Rp") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Jenis Pendapatan
            OutlinedTextField(
                value = uiState.incomeType,
                onValueChange = {},
                label = { Text("Jenis Pendapatan") },
                trailingIcon = {
                    Icon(Icons.Default.KeyboardArrowRight, null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showIncomeTypeSheet = true },
                enabled = false
            )

            // Upload Bukti
            Text(
                text = "Upload bukti transfer / nota / kwitansi",
                style = MaterialTheme.typography.labelMedium
            )

            ImageUploadBox(
                imagePath = uiState.imagePath,
                onClick = { showImagePickerSheet = true }
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = viewModel::saveCashIn,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Simpan")
            }
        }
    }

    // Bottom Sheet
    if (showIncomeTypeSheet) {
        IncomeTypeBottomSheet(
            onSelect = {
                viewModel.onIncomeTypeChange(it)
                showIncomeTypeSheet = false
            },
            onDismiss = { showIncomeTypeSheet = false }
        )
    }

    if (showImagePickerSheet) {
        ImagePickerBottomSheet(
            onImageSelected = { uri ->
                viewModel.onImageSelected(context, uri)
                showImagePickerSheet = false
            },
            onDismiss = { showImagePickerSheet = false }
        )
    }
}

@Composable
fun ImageUploadBox(
    imagePath: String?,
    onClick: () -> Unit
) {
    val borderColor = Color(0xFF4CAF50)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                BorderStroke(
                    2.dp,
                    borderColor
                ),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imagePath == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(borderColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Upload bukti transfer / nota / kwitansi",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    text = "hanya menggunakan format .JPG .PNG",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )
            }
        } else {
            AsyncImage(
                model = File(imagePath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeTypeBottomSheet(
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("Pilih Jenis Pendapatan", fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(12.dp))

            IncomeTypeItem(
                title = "Pendapatan Lain",
                description = "Uang yang masuk ke kasir atau rekening dan menambah profit"
            ) {
                onSelect("Pendapatan Lain")
            }

            IncomeTypeItem(
                title = "Non Pendapatan",
                description = "Uang yang masuk tetapi tidak menambah profit"
            ) {
                onSelect("Non Pendapatan")
            }
        }
    }
}

@Composable
fun IncomeTypeItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    onImageSelected: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraImageUri?.let { onImageSelected(it) }
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                val file = File(
                    context.cacheDir,
                    "camera_${System.currentTimeMillis()}.jpg"
                )

                cameraImageUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

                cameraLauncher.launch(cameraImageUri!!)
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let { onImageSelected(it) }
        }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // TITLE (CENTER)
            Text(
                text = "Ambil dari",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            // KAMERA
            ListItem(
                headlineContent = { Text("Kamera") },
                leadingContent = {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable {

                    val hasPermission =
                        ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        val file = File(
                            context.cacheDir,
                            "camera_${System.currentTimeMillis()}.jpg"
                        )

                        cameraImageUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )

                        cameraLauncher.launch(cameraImageUri!!)
                    } else {
                        cameraPermissionLauncher.launch(
                            android.Manifest.permission.CAMERA
                        )
                    }
                }
            )

            // GALERI
            ListItem(
                headlineContent = { Text("Galeri") },
                leadingContent = {
                    Icon(
                        Icons.Default.PhotoLibrary,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable {
                    galleryLauncher.launch("image/*")
                }
            )
        }
    }

}





