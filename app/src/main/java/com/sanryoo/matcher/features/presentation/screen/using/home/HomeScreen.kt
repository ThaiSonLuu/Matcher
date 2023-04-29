package com.sanryoo.matcher.features.presentation.screen.using.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using._components.ConfirmMatchDialog
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemSurface
import com.sanryoo.matcher.features.presentation.screen.using._components.Searching
import com.sanryoo.matcher.features.util.Screen
import com.sanryoo.matcher.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    navController: NavController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is HomeViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    HomeContent(viewModel)
}

@Composable
fun HomeContent(
    viewModel: HomeViewModel,
) {
    val context = LocalContext.current
    val user = viewModel.user.collectAsState().value
    val others = viewModel.others.collectAsState().value
    val tempOthers = viewModel.tempOthers.collectAsState().value
    if (tempOthers.id > 0) {
        ConfirmMatchDialog(others = tempOthers,
            confirm = { viewModel.confirm(1) },
            cancel = { viewModel.confirm(-1) })
    }
    if (user.searching == 1) {
        AlertDialog(
            onDismissRequest = viewModel::cancelFind, buttons = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Searching()
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = viewModel::cancelFind, shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = context.getString(R.string.cancel))
                    }
                }
            }, backgroundColor = Color.Transparent
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(60.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (user.avatar != "") {
                    rememberAsyncImagePainter(user.avatar)
                } else {
                    painterResource(id = R.drawable.logo_profile_default)
                },
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.surface)
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = user.fullname,
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 24.sp
            )
        }
        ItemSurface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(140.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Purple600,
                            Purple500,
                            Purple400,
                            Purple300,
                            Purple200,
                            Purple100,
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(15.dp),
                ),
            shape = RoundedCornerShape(15.dp),
            onClick = { viewModel.onUiEvent(HomeViewModel.UiEvent.Navigate(Screen.Profile.route)) },
            color = Color.Transparent,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = context.getString(R.string.profile), fontSize = 30.sp, color = Color.White)
            }
        }
        ItemSurface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(140.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Brush1, Brush2),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(15.dp),
                ),
            color = Color.Transparent,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (others.id > 0) {
                    viewModel.onUiEvent(HomeViewModel.UiEvent.Navigate(Screen.Message.route))
                } else {
                    viewModel.onUiEvent(HomeViewModel.UiEvent.ShowSnackBar(context.getString(R.string.you_have_not_been_matched)))
                }
            }
        ) {
            if (others.id > 0) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = others.avatar),
                            contentDescription = "Others avatar",
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface)
                                .size(75.dp)
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
                    Column {
                        Text(
                            text = others.fullname,
                            fontSize = 18.sp,
                        )
                        Text(
                            text = "${others.age} ${context.getString(R.string.years_old)}",
                            fontSize = 12.sp,
                        )
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.distance),
                                contentDescription = "Icon distance",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${
                                    String.format(
                                        "%.1f", others.currentdistance
                                    )
                                } km",
                                fontSize = 16.sp,
                                style = TextStyle(fontWeight = FontWeight.Normal)
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = context.getString(R.string.message), fontSize = 30.sp, color = Color.White)
                }
            }
        }
        if (others.id <= 0) {
            ItemSurface(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .height(90.dp)
                    .aspectRatio(1f),
                color = Color.Transparent,
                shape = CircleShape,
                onClick = {
                    if (user.banned < 5) {
                        viewModel.find()
                    } else {
                        viewModel.onUiEvent(HomeViewModel.UiEvent.ShowSnackBar(context.getString(R.string.you_were_banned)))
                    }
                }
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(Maroon300, Maroon300, Maroon300, Maroon200, Maroon100)
                            )
                        ), contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.6f)
                            .background(
                                color = Maroon300, shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Icon Search",
                            tint = Color.White,
                            modifier = Modifier.fillMaxSize(0.5f)
                        )
                    }
                }
            }
        }
    }
}