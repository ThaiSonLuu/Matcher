package com.sanryoo.matcher.features.presentation.screen.using.profile.tag_profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using._components.ChangeName
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemImage
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemInformation
import com.sanryoo.matcher.features.presentation.screen.using.profile.ProfileViewModel

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun TabInformation(viewModel: ProfileViewModel) {

    val context = LocalContext.current
    val listSex = listOf(context.getString(R.string.male), context.getString(R.string.female))
    val listStatus = listOf(
        context.getString(R.string.single),
        context.getString(R.string.single_mom_dad),
        context.getString(R.string.divorced_and_no_children),
        context.getString(R.string.divorced_and_have_children)
    )
    val user = viewModel.user.collectAsState().value
    val changeName = viewModel.changeName.collectAsState().value

    val launcher1 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            viewModel.selectedImage1(it)
        }
    }
    val launcher2 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            viewModel.selectedImage2(it)
        }
    }
    val launcher3 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            viewModel.selectedImage3(it)
        }
    }
    if (changeName) {
        ChangeName(
            currentName = user.fullname,
            onCancel = viewModel::onCanCel,
            onChange = viewModel::onFullnameChanged
        )
    }
    var chooseImage = 1
    val permission = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE,
        onPermissionResult = {
            if (it) {
                when (chooseImage) {
                    1 -> launcher1.launch("image/*")
                    2 -> launcher2.launch("image/*")
                    else -> launcher3.launch("image/*")
                }
            } else {
                viewModel.onUiEvent(ProfileViewModel.UiEvent.ShowSnackBar(context.getString(R.string.you_need_to_allow_permission_on_settings)))
            }
        }
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
        ) {
            ItemImage(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3 / 5f)
                    .padding(7.dp),
                url = user.image1,
                onClick = {
                    if (!permission.status.isGranted) {
                        chooseImage = 1
                        permission.launchPermissionRequest()
                    } else {
                        launcher1.launch("image/*")
                    }
                },
            )
            ItemImage(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3 / 5f)
                    .padding(7.dp),
                url = user.image2,
                onClick = {
                    if (!permission.status.isGranted) {
                        chooseImage = 2
                        permission.launchPermissionRequest()
                    } else {
                        launcher2.launch("image/*")
                    }
                }
            )
            ItemImage(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3 / 5f)
                    .padding(7.dp),
                url = user.image3,
                onClick = {
                    if (!permission.status.isGranted) {
                        chooseImage = 3
                        permission.launchPermissionRequest()
                    } else {
                        launcher3.launch("image/*")
                    }
                }
            )
        }
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.name,
            text = "${context.getString(R.string.fullname)}: ${user.fullname}",
            onClick = viewModel::changeName
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.sex,
            text = "${context.getString(R.string.sex)}: ${if (user.sex != 0) listSex[user.sex - 1] else ""}",
            onClick = viewModel::showChooseSex
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.alone,
            text = "${context.getString(R.string.you_are)}: ${listStatus[user.status - 1]}",
            onClick = { viewModel.showChooseStatus(0) }
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.age,
            text = "${context.getString(R.string.age)}: " + if (user.age > 0) "${user.age} ${context.getString(R.string.years_old)}" else "",
            onClick = { viewModel.showChooseAge(0) }
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.height,
            text = "${context.getString(R.string.height)}: " + if (user.height > 0) "${user.height} cm" else "",
            onClick = { viewModel.showChooseHeight(0) }
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.weight,
            text = "${context.getString(R.string.weight)}: " + if (user.weight > 0) "${user.weight} kg" else "",
            onClick = { viewModel.showChooseWeight(0) }
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.income,
            text = "${context.getString(R.string.income)}: " + if (user.income > 0) "${user.income},000,000 đ" else context.getString(
                R.string.no_income
            ),
            onClick = { viewModel.showChooseIncome(0) }
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.appearance,
            text = "${context.getString(R.string.appearance)}: " + if (user.appearance > 0) "${user.appearance} / 10" else "",
            onClick = { viewModel.showChooseAppearance(0) }
        )
    }
}

