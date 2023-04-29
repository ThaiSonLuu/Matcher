package com.sanryoo.matcher.features.presentation.screen.using._bottomsheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanryoo.matcher.features.presentation.screen.using._bottomsheet.component.*
import com.sanryoo.matcher.features.presentation.screen.using.profile.ProfileViewModel
import com.sanryoo.matcher.features.util.BottomSheet

@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    sheetState: ModalBottomSheetState,
    profileViewModel: ProfileViewModel,
    content: @Composable () -> Unit,
) {
    val chooseBottomSheet = profileViewModel.chooseBottomSheet.collectAsState().value
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        scrimColor = Color.Black.copy(alpha = 0.32f),
        sheetContent = {
            when (chooseBottomSheet) {
                BottomSheet.DEFAULT -> Spacer(modifier = Modifier.fillMaxSize())
                BottomSheet.SEX -> ChooseSex(profileViewModel)
                BottomSheet.STATUS -> ChooseStatus(profileViewModel)
                BottomSheet.AGE -> ChooseAge(profileViewModel)
                BottomSheet.HEIGHT -> ChooseHeight(profileViewModel)
                BottomSheet.WEIGHT -> ChooseWeight(profileViewModel)
                BottomSheet.INCOME -> ChooseIncome(profileViewModel)
                BottomSheet.DISTANCE -> ChooseDistance(profileViewModel)
                BottomSheet.APPEARANCE -> ChooseAppearance(profileViewModel)
            }
        },
        content = { content() }
    )
}