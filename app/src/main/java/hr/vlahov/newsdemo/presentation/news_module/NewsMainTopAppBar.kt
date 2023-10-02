package hr.vlahov.newsdemo.presentation.news_module

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewsMainTopAppBar(
    onSearchQueryCommitted: (query: String?) -> Unit,
    onDateRangeConfirmed: (dateFrom: Long?, dateTo: Long?) -> Unit,
    onNewsOrderChanged: (orderBy: NewsFilters.OrderBy) -> Unit,
) {
    TopAppBar(
        title = { },
        actions = {
            SearchAction(
                onSearchQueryCommitted = onSearchQueryCommitted
            )
            FiltersDropdownAction(
                onDateRangeConfirmed = onDateRangeConfirmed,
                onNewsOrderChanged = onNewsOrderChanged
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun RowScope.SearchAction(
    onSearchQueryCommitted: (query: String?) -> Unit,
    searchHint: String = stringResource(id = R.string.search_here),
    isExpanded: Boolean = false,
    debounceMS: Long = 500L,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
) {
    val isExpandedState = rememberSaveable { mutableStateOf(isExpanded) }
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(searchQuery.value) {
        delay(debounceMS)
        onSearchQueryCommitted(searchQuery.value)
    }

    LaunchedEffect(isExpandedState.value) {
        if (isExpandedState.value)
            focusRequester.requestFocus()
    }

    AnimatedVisibility(
        visible = isExpandedState.value,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally()
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text(text = searchHint) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            trailingIcon = {
                AnimatedVisibility(
                    visible = searchQuery.value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { searchQuery.value = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        )
    }

    AnimatedVisibility(visible = isExpandedState.value.not()) {
        IconButton(onClick = { isExpandedState.value = true }, colors = colors) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
    }
}

@Composable
private fun FiltersDropdownAction(
    onDateRangeConfirmed: (dateFrom: Long?, dateTo: Long?) -> Unit,
    onNewsOrderChanged: (orderBy: NewsFilters.OrderBy) -> Unit,
) {
    val dropdownMenuExpanded = rememberSaveable { mutableStateOf(false) }

    val bottomSheetVisible = rememberSaveable { mutableStateOf(false) }


    IconButton(onClick = { dropdownMenuExpanded.value = dropdownMenuExpanded.value.not() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = "Filter news",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }

    DropdownMenu(
        expanded = dropdownMenuExpanded.value,
        onDismissRequest = { dropdownMenuExpanded.value = false },
    ) {
        DateRangePickerAction(
            onDateRangeSelectButtonClicked = {
                dropdownMenuExpanded.value = false
                bottomSheetVisible.value = true
            }
        )
        //NewsOrderBySelector(onNewsOrderChanged = onNewsOrderChanged)
    }

    DateRangeBottomSheet(
        isVisible = bottomSheetVisible.value,
        onDismissRequest = { bottomSheetVisible.value = false },
        onDateRangeConfirmed = onDateRangeConfirmed
    )
}

@Composable
private fun DateRangePickerAction(
    onDateRangeSelectButtonClicked: () -> Unit,
) {
    TextButton(
        onClick = onDateRangeSelectButtonClicked
    ) {
        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date from")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.select_date_range),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun DateRangeBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onDateRangeConfirmed: (dateFrom: Long?, dateTo: Long?) -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState()
    val bottomSheetState =
        rememberModalBottomSheetState(confirmValueChange = { it != SheetValue.PartiallyExpanded })
    val coroutineScope = rememberCoroutineScope()
    if (isVisible)
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    onDismissRequest()
                }
            },
            sheetState = bottomSheetState,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                            onDismissRequest()
                        }
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        onDateRangeConfirmed(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                        onDismissRequest()
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            DateRangePicker(
                state = dateRangePickerState,
                title = null,
                headline = null
            )
        }
}