package com.example.uangmasuk.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uangmasuk.data.local.entity.CustomerEntity
import com.example.uangmasuk.di.Injection
import com.example.uangmasuk.navigationDrawer.RootScreen
import com.example.uangmasuk.presentation.cashIn.CashInScreen
import com.example.uangmasuk.presentation.cashIn.CashInViewModel
import com.example.uangmasuk.presentation.cashIn.CashInViewModelFactory
import com.example.uangmasuk.presentation.custList.AddCustomerScreen
import com.example.uangmasuk.presentation.custList.CustomerListScreen
import com.example.uangmasuk.presentation.detailCashIn.DetailCashInScreen
import com.example.uangmasuk.presentation.editCashIn.EditCashInScreen
import com.example.uangmasuk.presentation.home.HomeScreen
import com.example.uangmasuk.presentation.image.FullImageScreen

object Routes {
    const val ROOT = "root"
    const val HOME = "home"
    const val CREATE = "create"
    const val DETAIL = "detail/{id}"
    const val EDIT = "edit/{id}"
    const val CUSTOMER_LIST = "customer_list"
    const val FULL_IMAGE = "full_image"
    const val ADD_CUSTOMER = "add_customer"
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ROOT,
        modifier = modifier
    ) {

        composable(Routes.ROOT) {
            RootScreen(
                onKeuanganClick = {
                    navController.navigate(Routes.HOME)
                }
            )
        }


        composable(Routes.HOME) {
            HomeScreen(
                onAddClick = {
                    navController.navigate(Routes.CREATE)
                },
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        composable(Routes.CREATE) {

            val context = LocalContext.current
            val backStackEntry = remember {
                navController.getBackStackEntry(Routes.CREATE)
            }

            val viewModel: CashInViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = CashInViewModelFactory(
                    Injection.provideCashInRepository(context),
                    Injection.provideCustomerRepository(context)
                )
            )

            CashInScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                onOpenCustomerList = {
                    navController.navigate(Routes.CUSTOMER_LIST)
                }
            )
        }


        composable(Routes.CUSTOMER_LIST) {

            CustomerListScreen(
                onBack = {
                    navController.popBackStack()
                },
                onCustomerSelected = { customer ->

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_customer", customer)

                    navController.popBackStack()
                },
                onAddCustomer = {
                    navController.navigate(Routes.ADD_CUSTOMER)
                }
            )
        }

        composable(Routes.CUSTOMER_LIST) {

            CustomerListScreen(
                onBack = { navController.popBackStack() },
                onAddCustomer = {
                    navController.navigate(Routes.ADD_CUSTOMER)
                },
                onCustomerSelected = { customer ->

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_customer_id", customer.id)

                    navController.popBackStack()
                }
            )
        }


        composable(Routes.ADD_CUSTOMER) {
            AddCustomerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CREATE) {

            val context = LocalContext.current
            val backStackEntry = remember { navController.getBackStackEntry(Routes.CREATE) }

            val viewModel: CashInViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = CashInViewModelFactory(
                    Injection.provideCashInRepository(context),
                    Injection.provideCustomerRepository(context)
                )
            )


            LaunchedEffect(backStackEntry) {
                backStackEntry
                    .savedStateHandle
                    .get<Int>("selected_customer_id")
                    ?.let { customerId ->
                        viewModel.onCustomerSelected(customerId)
                        backStackEntry.savedStateHandle.remove<Int>("selected_customer_id")
                    }
            }

            CashInScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                onOpenCustomerList = {
                    navController.navigate(Routes.CUSTOMER_LIST)
                }
            )
        }




        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            DetailCashInScreen(
                cashInId = id,
                onBack = { navController.popBackStack() },
                onEdit = {
                    navController.navigate("edit/$id")
                },
                onDeleteSuccess = {
                    navController.popBackStack()
                },
                onImageClick = { imagePath ->
                    val encodedPath = Uri.encode(imagePath)
                    navController.navigate(
                        "${Routes.FULL_IMAGE}?path=$encodedPath"
                    )
                }
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            EditCashInScreen(
                cashInId = id,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.FULL_IMAGE}?path={path}",
            arguments = listOf(
                navArgument("path") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path") ?: ""

            FullImageScreen(
                imagePath = path,
                onBack = { navController.popBackStack() }
            )
        }
    }
}