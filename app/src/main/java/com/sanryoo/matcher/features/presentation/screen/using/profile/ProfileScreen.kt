package com.sanryoo.matcher.features.presentation.screen.using.profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using.profile.tag_profile.TabInformation
import com.sanryoo.matcher.features.presentation.screen.using.profile.tag_profile.TabRequire
import com.sanryoo.matcher.ui.theme.DeepSkyBlue1
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ProfileScreen(
    sheetState: ModalBottomSheetState,
    scaffoldState: ScaffoldState,
    viewModel: ProfileViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProfileViewModel.UiEvent.BottomSheet -> {
                    sheetState.animateTo(if (event.state) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden)
                }
                is ProfileViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    ProfileContent(viewModel)
}

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ProfileContent(viewModel: ProfileViewModel) {

    val context = LocalContext.current
    val user = viewModel.user.collectAsState().value
    val tagIndex = viewModel.tagIndex.collectAsState().value

    val launcherAvatar = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.selectedAvatar(uri)
            }
        })
    val permission =
        rememberPermissionState(
            permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE,
            onPermissionResult = {
                if (it) {
                    launcherAvatar.launch("image/*")
                } else {
                    viewModel.onUiEvent(ProfileViewModel.UiEvent.ShowSnackBar(context.getString(R.string.you_need_to_allow_permission_on_settings)))
                }
            })
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(bottom = 5.dp)) {
                    Image(
                        painter = if (user.avatar != "") {
                            rememberAsyncImagePainter(user.avatar)
                        } else {
                            painterResource(id = R.drawable.logo_profile_default)
                        },
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface)
                            .size(90.dp)
                            .clickable(
                                onClick = {
                                    if (!permission.status.shouldShowRationale) {
                                        permission.launchPermissionRequest()
                                    } else {
                                        launcherAvatar.launch("image/*")
                                    }
                                }, interactionSource = MutableInteractionSource(), indication = null
                            )
                            .border(
                                width = 3.dp, color = DeepSkyBlue1, shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )
                    if (user.sex != 0) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.BottomEnd)
                                .background(MaterialTheme.colors.background, CircleShape)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp, color = DeepSkyBlue1, shape = CircleShape
                                ), contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = if (user.sex == 1) R.drawable.male else R.drawable.female),
                                contentDescription = "",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
                Text(
                    text = if (user.idgoogle != "" || user.idfacebook != "") user.fullname else "@${user.username}",
                    fontSize = 22.sp
                )
            }
            TabView(viewModel)
            if (tagIndex == 0) {
                TabInformation(viewModel)
            } else {
                TabRequire(viewModel)
            }
        }
        Spacer(modifier = Modifier.height(55.dp))
    }
}

@Composable
fun TabView(viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val inactiveColor = Color(0xFF777777)
    val tagIndex = viewModel.tagIndex.collectAsState().value
    TabRow(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        selectedTabIndex = tagIndex,
        backgroundColor = Color.Transparent,
    ) {
        Tab(selected = tagIndex == 0,
            unselectedContentColor = inactiveColor,
            onClick = { viewModel.tagMyInformation() }) {
            Text(
                text = context.getString(R.string.information),
                fontSize = 15.sp,
                fontWeight = FontWeight.W500
            )
        }
        Tab(selected = tagIndex == 1,
            unselectedContentColor = inactiveColor,
            onClick = { viewModel.tagMyRequire() }) {
            Text(
                text = context.getString(R.string.require),
                fontSize = 15.sp,
                fontWeight = FontWeight.W500
            )
        }
    }
}