/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun ContentLoginPin() {

    val height = 55.dp
    val width = 90.dp

    val modifierBox = Modifier
        .background(color = MaterialTheme.colorScheme.background)
        .width(width)
        .height(height)
        .border(border = BorderStroke(1.dp, color = Color.Black))
    val listItems = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Rounded.RadioButtonChecked, contentDescription = null, modifier = Modifier.padding(4.dp), tint = MaterialTheme.colorScheme.primary)
            Icon(imageVector = Icons.Rounded.RadioButtonChecked, contentDescription = null, modifier = Modifier.padding(4.dp), tint = MaterialTheme.colorScheme.primary)
            Icon(imageVector = Icons.Rounded.RadioButtonChecked, contentDescription = null, modifier = Modifier.padding(4.dp), tint = MaterialTheme.colorScheme.primary)
            Icon(imageVector = Icons.Rounded.RadioButtonUnchecked, contentDescription = null, modifier = Modifier.padding(4.dp), tint = MaterialTheme.colorScheme.primary)
            Icon(imageVector = Icons.Rounded.RadioButtonUnchecked, contentDescription = null, modifier = Modifier.padding(4.dp), tint = MaterialTheme.colorScheme.primary)
            Icon(imageVector = Icons.Rounded.RadioButtonUnchecked, contentDescription = null, modifier = Modifier.padding(4.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Box(
            modifier = Modifier.width(width * 3).height(height * 4),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()

            ) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.width(width * 3).height(height * 3),
                    content = {
                        items(listItems) { number ->
                            Box(
                                modifier = modifierBox,
                                contentAlignment = Alignment.Center,
                                content = {
                                    Text(
                                        text = number.toString(),
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            )
                        }
                    }
                )


/*            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = modifierBox,
                    contentAlignment = Alignment.Center,
                    content = { Text(text = "1", fontSize = 24.sp) }
                )
                Box(
                    modifier = modifierBox,
                    contentAlignment = Alignment.Center,
                    content = { Text(text = "2", fontSize = 24.sp) }
                )
                Box(
                    modifier = modifierBox,
                    contentAlignment = Alignment.Center,
                    content = { Text(text = "3", fontSize = 24.sp) }
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = modifierBox,
                    contentAlignment = Alignment.Center,
                    content = { Text(text = "4", fontSize = 24.sp) }
                )
                Box(
                    modifier = modifierBox,
                    contentAlignment = Alignment.Center,
                    content = { Text(text = "5", fontSize = 24.sp) }
                )
                Box(
                    modifier = modifierBox,
                    contentAlignment = Alignment.Center,
                    content = { Text(text = "6", fontSize = 24.sp) }
                )
            }*/
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = modifierBox,
                        contentAlignment = Alignment.Center,
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Fingerprint,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    )
                    Box(
                        modifier = modifierBox,
                        contentAlignment = Alignment.Center,
                        content = {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    )
                    Box(
                        modifier = modifierBox,
                        contentAlignment = Alignment.Center,
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowLeft,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    )
                }

            }
        }
    }


}