package com.example.uangmasuk.navigationDrawer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    onKeuanganClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedIndex by remember { mutableIntStateOf(3) } // Keuangan aktif

    val drawerMenus = listOf(
        DrawerMenuItem("Kasir", Icons.Outlined.PointOfSale),
        DrawerMenuItem("Riwayat Penjualan", Icons.Outlined.ReceiptLong),
        DrawerMenuItem("Absensi", Icons.Outlined.FactCheck),
        DrawerMenuItem("Keuangan", Icons.Outlined.AccountBalanceWallet),
        DrawerMenuItem("Stok", Icons.Outlined.Inventory2),
        DrawerMenuItem("Laporan", Icons.Outlined.Assessment),
        DrawerMenuItem("Pengaturan", Icons.Outlined.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {

                    // ===== HEADER (FIXED) =====
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = Color(0xFF2ECC71),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "J",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text("John Doe", fontWeight = FontWeight.Bold)
                                Text(
                                    "Admin",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    // ===== MENU (SCROLLABLE) =====
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        itemsIndexed(drawerMenus) { index, item ->
                            DrawerMenuRow(
                                title = item.title,
                                icon = item.icon,
                                selected = index == selectedIndex,
                                onClick = {
                                    selectedIndex = index
                                    scope.launch { drawerState.close() }

                                    if (item.title == "Keuangan") {
                                        onKeuanganClick()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Pilih menu dari drawer")
            }
        }
    }
}

