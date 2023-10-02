package hr.vlahov.newsdemo.presentation.news_module

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
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
                .weight(1f)
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


    Column {
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
                },
                modifier = Modifier.padding(8.dp, 0.dp)
            )
            NewsOrderBySelector(
                onNewsOrderChanged = onNewsOrderChanged,
                modifier = Modifier.padding(8.dp, 0.dp)
            )
        }
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
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onDateRangeSelectButtonClicked,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date selector")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.select_date_range),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
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

@Composable
private fun NewsOrderBySelector(
    onNewsOrderChanged: (orderBy: NewsFilters.OrderBy) -> Unit,
    modifier: Modifier = Modifier,
    currentOrderBy: NewsFilters.OrderBy = NewsFilters.OrderBy.ASCENDING,
) {
    val orderBy = remember { mutableStateOf(currentOrderBy) }
    val scope = rememberCoroutineScope()

    val animatedRotation =
        remember { Animatable(if (orderBy.value == NewsFilters.OrderBy.ASCENDING) 0f else 180f) }

    TextButton(
        onClick = {
            orderBy.value = orderBy.value.toggle()
            onNewsOrderChanged(orderBy.value)

            scope.launch {
                animatedRotation.animateTo(
                    targetValue = if (orderBy.value == NewsFilters.OrderBy.ASCENDING) 0f else 180f,

                    )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Select order by",
            modifier = Modifier.rotate(animatedRotation.value)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.sort_order),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview
private fun FiltersDropdownPreview() {
    NewsDemoTheme {
        Column {
            DateRangePickerAction(
                onDateRangeSelectButtonClicked = { }
            )
            NewsOrderBySelector(
                onNewsOrderChanged = { },
                currentOrderBy = NewsFilters.OrderBy.ASCENDING
            )
        }
    }
}