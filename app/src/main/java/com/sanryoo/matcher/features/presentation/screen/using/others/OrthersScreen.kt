package com.sanryoo.matcher.features.presentation.screen.using.others

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemInformation
import com.sanryoo.matcher.ui.theme.DeepSkyBlue1
import com.sanryoo.matcher.ui.theme.Primary
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OthersScreen(
    navController: NavHostController,
    viewModel: OthersViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.evenFlow.collectLatest {
            navController.popBackStack()
        }
    }
    OtherInformationContent(viewModel)
}

@Composable
fun OtherInformationContent(viewModel: OthersViewModel) {
    val context = LocalContext.current
    val LIST_STATUS = listOf(
        context.getString(R.string.single),
        context.getString(R.string.single_mom_dad),
        context.getString(R.string.divorced_and_no_children),
        context.getString(R.string.divorced_and_have_children)
    )
    val others = viewModel.others.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .padding(top = 15.dp, start = 10.dp)
                    .size(30.dp)
                    .align(Alignment.TopStart)
                    .clickable(
                        onClick = { viewModel.onUiEvent(OthersViewModel.UiEvent.NavigateBack) },
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ),
                tint = Primary
            )
            Box(modifier = Modifier.padding(20.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(model = others.avatar),
                    contentDescription = "Other's avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.surface)
                        .size(100.dp)
                        .border(
                            width = 3.dp, color = DeepSkyBlue1, shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
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
                        painter = painterResource(id = if (others.sex == 1) R.drawable.male else R.drawable.female),
                        contentDescription = "Sex icon",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
        Text(
            text = others.fullname,
            fontSize = 22.sp,
            style = TextStyle(fontWeight = FontWeight.Medium)
        )
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp),
        ) {
            Text(
                text = "${others.age} ${context.getString(R.string.years_old)}",
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight.Normal)
            )
            Text(
                text = "-",
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight.Normal),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.distance),
                contentDescription = "Icon distance",
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${String.format("%.1f", others.currentdistance)} km",
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight.Normal)
            )
        }
        Row {
            Image(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3 / 5f)
                    .padding(7.dp)
                    .clip(RoundedCornerShape(5.dp))
                ,
                painter = rememberAsyncImagePainter(model = others.image1),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
            Image(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3 / 5f)
                    .padding(7.dp)
                    .clip(RoundedCornerShape(5.dp)),
                painter = rememberAsyncImagePainter(model = others.image2),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(3 / 5f)
                    .padding(7.dp)
                    .clip(RoundedCornerShape(5.dp)),
                painter = rememberAsyncImagePainter(model = others.image3),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.alone,
            text = "${context.getString(R.string.status)}: ${LIST_STATUS[others.status - 1]}",
            showIconNext = false
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.height,
            text = "${context.getString(R.string.height)}: " + if (others.height > 0) "${others.height} cm" else "",
            showIconNext = false
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.weight,
            text = "${context.getString(R.string.weight)}: " + if (others.weight > 0) "${others.weight} kg" else "",
            showIconNext = false
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.income,
            text = "${context.getString(R.string.income)}: " + if (others.income > 0) "${others.income},000,000 đ" else "No income",
            showIconNext = false
        )
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .height(50.dp),
            icon = R.drawable.appearance,
            text = "${context.getString(R.string.appearance)}: " + if (others.appearance > 0) "${others.appearance} / 10" else "",
            showIconNext = false
        )
    }
}