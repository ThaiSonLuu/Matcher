package com.sanryoo.matcher.features.presentation.screen.using.message

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemMenu
import com.sanryoo.matcher.features.util.MessageType
import com.sanryoo.matcher.features.util.Screen
import com.sanryoo.matcher.ui.theme.Primary
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MessageScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.evenFlow.collectLatest { event ->
            when (event) {
                is MessageViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is MessageViewModel.UiEvent.NavigateOtherInformation -> {
                    navController.navigate(Screen.OtherInformation.route)
                }
                is MessageViewModel.UiEvent.ScrollToBottom -> {
                    lazyListState.scrollToItem(0)
                }
                is MessageViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    MessageContent(viewModel, lazyListState)
}

@SuppressLint("SimpleDateFormat")
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MessageContent(viewModel: MessageViewModel, lazyListState: LazyListState) {
    val context = LocalContext.current
    val listReport = listOf(
        context.getString(R.string.information_is_invalid),
        context.getString(R.string.image_sentitive),
        context.getString(R.string.vulgar_language),
        context.getString(R.string.not_seriously)
    )
    val user = viewModel.user.collectAsState().value
    val others = viewModel.others.collectAsState().value
    val messageState = viewModel.messageState.collectAsState().value
    val sending = viewModel.sending.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    val currentDateTime = Date(System.currentTimeMillis())
    val currentCalendar = Calendar.getInstance().apply {
        time = currentDateTime
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = viewModel::loadMoreMessage,
        refreshingOffset = 45.dp,
        refreshThreshold = 40.dp
    )
    val focusManger = LocalFocusManager.current

    var showDropDownMenu by remember { mutableStateOf(false) }
    var showConfirmCancelMatchDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.sendMessageImage(uri)
        }
    }

    if (showConfirmCancelMatchDialog) {
        AlertDialog(onDismissRequest = { showConfirmCancelMatchDialog = false },
            backgroundColor = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(15.dp),
            buttons = {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = context.getString(R.string.do_you_want_to_cancel_this_match),
                        fontSize = 20.sp,
                        style = TextStyle(textAlign = TextAlign.Center)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showConfirmCancelMatchDialog = false },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = context.getString(R.string.no))
                        }
                        Button(
                            onClick = {
                                viewModel.cancelMatch(0)
                                showConfirmCancelMatchDialog = false
                            }, shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = context.getString(R.string.yes))
                        }
                    }
                }
            })
    }

    if (showReportDialog) {
        AlertDialog(onDismissRequest = { showReportDialog = false },
            backgroundColor = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(15.dp),
            buttons = {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = context.getString(R.string.report),
                        fontSize = 20.sp,
                        style = TextStyle(textAlign = TextAlign.Center)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    var report0 by remember { mutableStateOf(false) }
                    var report1 by remember { mutableStateOf(false) }
                    var report2 by remember { mutableStateOf(false) }
                    var report3 by remember { mutableStateOf(false) }
                    LazyColumn {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = report0,
                                    onClick = { report0 = !report0 },
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(text = listReport[0], modifier = Modifier.padding(5.dp))
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = report1,
                                    onClick = { report1 = !report1 },
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(text = listReport[1], modifier = Modifier.padding(5.dp))
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = report2,
                                    onClick = { report2 = !report2 },
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(text = listReport[2], modifier = Modifier.padding(5.dp))
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = report3,
                                    onClick = { report3 = !report3 },
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(text = listReport[3], modifier = Modifier.padding(5.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showReportDialog = false },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = context.getString(R.string.cancel))
                        }
                        Button(
                            onClick = {
                                viewModel.cancelMatch(1)
                                showReportDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            enabled = report0 || report1 || report2 || report3
                        ) {
                            Text(text = context.getString(R.string.report))
                        }
                    }
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp)
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            onClick = { focusManger.clearFocus() },
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ), reverseLayout = true, state = lazyListState
                ) {
                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            when (sending) {
                                1 -> {
                                    Text(
                                        text = context.getString(R.string.sending),
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 20.dp),
                                        fontSize = 12.sp
                                    )
                                }
                                2 -> {
                                    Text(
                                        text = context.getString(R.string.sent),
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 20.dp),
                                        fontSize = 12.sp
                                    )
                                }
                                3 -> {
                                    Text(
                                        text = context.getString(R.string.error),
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 20.dp),
                                        fontSize = 12.sp,
                                        style = TextStyle(color = Color.Red)
                                    )
                                }
                            }
                        }
                    }
                    items(messageState.listMessage.size) { index ->
                        val message = messageState.listMessage[index]
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (message.date != null) {
                                    if (
                                        index == messageState.listMessage.size - 1 ||
                                        TimeUnit.MILLISECONDS.toMinutes(message.date!!.time - messageState.listMessage[index + 1].date!!.time) >= 5
                                    ) {
                                        val calendar = Calendar.getInstance().apply {
                                            time = message.date!!
                                        }
                                        val dateFormat = when {
                                            calendar.get(Calendar.YEAR) != currentCalendar.get(
                                                Calendar.YEAR
                                            ) ->
                                                SimpleDateFormat("HH:mm EEEE dd/MM/yyyy")
                                            calendar.get(Calendar.MONTH) != currentCalendar.get(
                                                Calendar.MONTH
                                            ) ->
                                                SimpleDateFormat("HH:mm EEEE dd MMMM")
                                            TimeUnit.MILLISECONDS.toDays(currentDateTime.time - message.date!!.time) > 6 ->
                                                SimpleDateFormat("HH:mm EEEE dd MMMM")
                                            TimeUnit.MILLISECONDS.toDays(currentDateTime.time - message.date!!.time) in 2..6 ->
                                                SimpleDateFormat("HH:mm EEEE")
                                            calendar.get(Calendar.DATE) + 1 == currentCalendar.get(
                                                Calendar.DATE
                                            ) ->
                                                SimpleDateFormat("HH:mm '${context.getString(R.string.yesterday)}'")
                                            else -> SimpleDateFormat("HH:mm '${context.getString(R.string.today)}'")
                                        }
                                        Text(
                                            text = dateFormat.format(message.date!!),
                                            fontSize = 12.sp,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(horizontal = 5.dp)
                                        )
                                    }
                                }
                            }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (message.type == MessageType.TEXT) {
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                top = 0.5.dp,
                                                bottom = 0.5.dp,
                                                start = if (message.usersend.id == user.id) 100.dp else 5.dp,
                                                end = if (message.usersend.id == user.id) 5.dp else 100.dp,
                                            )
                                            .align(
                                                if (message.usersend.id == user.id) Alignment.CenterEnd
                                                else Alignment.CenterStart
                                            )
                                            .background(
                                                if (message.usersend.id == user.id) Primary
                                                else MaterialTheme.colors.surface,
                                                shape = if (message.usersend.id == user.id) RoundedCornerShape(
                                                    topStart = 15.dp,
                                                    bottomStart = 15.dp,
                                                    topEnd = if (index < messageState.listMessage.size - 1 && message.usersend.id == messageState.listMessage[index + 1].usersend.id) {
                                                        5.dp
                                                    } else {
                                                        15.dp
                                                    },
                                                    bottomEnd = if (index > 0 && message.usersend.id == messageState.listMessage[index - 1].usersend.id) {
                                                        5.dp
                                                    } else {
                                                        15.dp
                                                    },
                                                ) else RoundedCornerShape(
                                                    topEnd = 15.dp,
                                                    bottomEnd = 15.dp,
                                                    topStart = if (index < messageState.listMessage.size - 1 && message.usersend.id == messageState.listMessage[index + 1].usersend.id) {
                                                        5.dp
                                                    } else {
                                                        15.dp
                                                    },
                                                    bottomStart = if (index > 0 && message.usersend.id == messageState.listMessage[index - 1].usersend.id) {
                                                        5.dp
                                                    } else {
                                                        15.dp
                                                    },
                                                )
                                            )
                                    ) {
                                        Text(
                                            text = messageState.listMessage[index].data,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                                vertical = 7.dp
                                            )
                                        )
                                    }
                                } else if (message.type == MessageType.IMAGE) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = message.data),
                                        contentDescription = "Image message",
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth(0.55f)
                                            .aspectRatio(message.width.toFloat() / message.height.toFloat())
                                            .background(
                                                color = MaterialTheme.colors.surface,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clip(RoundedCornerShape(8.dp))
                                            .align(
                                                if (message.usersend.id == user.id)
                                                    Alignment.CenterEnd
                                                else
                                                    Alignment.CenterStart
                                            )
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                PullRefreshIndicator(
                    refreshing = isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    contentColor = Primary,
                    backgroundColor = MaterialTheme.colors.surface
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(MaterialTheme.colors.background)
                        .clickable(
                            onClick = { focusManger.clearFocus() },
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ), verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(30.dp)
                            .clickable(
                                onClick = { viewModel.onUiEvent(MessageViewModel.UiEvent.NavigateBack) },
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ),
                        tint = Primary
                    )
                    Image(
                        painter = rememberAsyncImagePainter(model = others.avatar),
                        contentDescription = "Other's avatar",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface)
                            .size(50.dp)
                            .clickable(
                                onClick = {
                                    viewModel.onUiEvent(MessageViewModel.UiEvent.NavigateOtherInformation)
                                },
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = others.fullname,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    viewModel.onUiEvent(MessageViewModel.UiEvent.NavigateOtherInformation)
                                },
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            )
                            .weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.three_dots),
                        contentDescription = "Three Dots",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(25.dp)
                            .clickable(
                                onClick = { showDropDownMenu = !showDropDownMenu },
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            )
                    )
                }
                if (showDropDownMenu) {
                    Column(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .width(200.dp)
                            .background(
                                MaterialTheme.colors.surface, RoundedCornerShape(
                                    topStart = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp
                                )
                            )
                            .align(Alignment.End)
                    ) {
                        ItemMenu(
                            icon = R.drawable.report,
                            text = context.getString(R.string.report),
                            iconSize = 25.dp,
                            onClick = { showReportDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        Divider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        ItemMenu(
                            icon = R.drawable.close,
                            text = context.getString(R.string.cancel_match),
                            iconSize = 25.dp,
                            contentColor = Color.Red,
                            onClick = { showConfirmCancelMatchDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.image_gallery),
                contentDescription = "Image",
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 15.dp)
                    .size(25.dp)
                    .align(Alignment.Bottom)
                    .clickable(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ),
                tint = Primary
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .background(
                        MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = messageState.inputMessage,
                    onValueChange = viewModel::onInputMessageChange,
                    textStyle = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colors.onBackground
                    ),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManger.clearFocus()
                    }),
                    maxLines = 5,
                    cursorBrush = SolidColor(Primary),
                )
                if (messageState.inputMessage.isEmpty()) {
                    Text(
                        text = "Aa",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.send_message),
                contentDescription = "Send",
                tint = Primary,
                modifier = Modifier
                    .padding(bottom = 17.dp, end = 10.dp)
                    .size(25.dp)
                    .align(Alignment.Bottom)
                    .clickable(
                        onClick = viewModel::sendMessage,
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    )
            )
        }
    }
}