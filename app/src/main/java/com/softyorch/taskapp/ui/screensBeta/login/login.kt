package com.softyorch.taskapp.ui.screensBeta.login

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.theme.TaskAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Task-App
 * File created by Jorge Agulló on 11/November/2022
 */

@Preview(showBackground = true)
@Composable
fun LoginScreenBeta(navController: NavController = NavController(context = LocalContext.current)) {

    val viewModel = hiltViewModel<LoginViewModelBeta>()

    TaskAppTheme {
        Surface(Modifier.fillMaxSize()) {
            Background()
            Body(viewModel)
        }
    }
}

@Composable
fun Background() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = rememberAsyncImagePainter(
                model = R.drawable.pexels_polina_kovaleva_5717421,
                contentScale = ContentScale.Crop
            ),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Body(viewModel: LoginViewModelBeta) {
    val showLogin by viewModel.showLogin.observeAsState(initial = false)
    val scope = rememberCoroutineScope()

    scope.launch {
        delay(3000L)

        viewModel.showLogin()
    }

    if (showLogin) LoginBody()

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginBody() {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = {
                LinearEasing.transform(0.1f)
            })
        )
    })
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetContent = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Bottom sheet", fontSize = 60.sp)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                scope.launch {
                    sheetState.apply {
                        if (isVisible) hide() else show()
                    }
                }
            }) {
                Text(
                    text = "Bottom sheet fraction -> ${sheetState.progress.fraction}",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

/*    val bottomSheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy//DampingRatioLowBouncy//DampingRatioHighBouncy
        )
    )
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Bottom sheet", fontSize = 60.sp)
            }
        },
        sheetBackgroundColor = Color.Green
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                scope.launch {
                    bottomSheetState.apply {
                        if (isCollapsed) expand() else collapse()
                    }
                }
            }) {
                Text(
                    text = "Bottom sheet fraction -> ${bottomSheetState.progress.fraction}",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }*/


/*BottomSheetScaffold(
        modifier = Modifier.padding(24.dp),
        scaffoldState = scaffoldState,
        sheetContent = {
            Text("Hola desde sheetContent")
        },
        sheetPeekHeight = 100.dp,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {Text("Text Button")},
                onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.apply {
                            if (isCollapsed) expand() else collapse()
                        }
                    }
                }
            )
        }
    ){
        Text("Hola desde lambda")
    }*/

}