package com.example.uangmasuk.presentation.custList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uangmasuk.data.local.entity.CustomerEntity
import com.example.uangmasuk.di.Injection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    onCustomerSelected: (CustomerEntity) -> Unit,
    onBack: () -> Unit,
    onAddCustomer: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember {
        CustomerViewModel(
            Injection.provideCustomerRepository(context)
        )
    }

    val customers by viewModel.customers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredCustomers = remember(customers, searchQuery) {
        if (searchQuery.isBlank()) {
            customers
        } else {
            customers.filter { customer ->
                customer.name.contains(searchQuery, ignoreCase = true) ||
                        (customer.phone?.contains(searchQuery, ignoreCase = true) == true) ||
                        (customer.email?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pelanggan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCustomer) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari pelanggan disini") },
                leadingIcon = {
                    Icon(Icons.Default.Search, null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            LazyColumn {
                items(filteredCustomers) { customer ->
                    CustomerItem(
                        customer = customer,
                        onClick = { onCustomerSelected(customer) }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerItem(
    customer: CustomerEntity,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = {
            CircleAvatar(customer.name)
        },
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(customer.name)
                if (customer.isMember) {
                    Spacer(Modifier.width(8.dp))
                    MemberBadge()
                }
            }
        },
        supportingContent = {
            Column {
                customer.phone?.let {
                    Text(it)
                }
                customer.email?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        },
        trailingContent = {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF2ECC71)
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun CircleAvatar(name: String) {
    val letter = name.firstOrNull()?.uppercase() ?: "?"

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color(0xFF2ECC71),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MemberBadge() {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "Member",
            color = Color(0xFF2ECC71),
            style = MaterialTheme.typography.labelSmall
        )
    }
}




