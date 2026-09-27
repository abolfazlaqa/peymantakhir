package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeymanTopBar(
    onSavedProjectsClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AmberAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_peyman_1790438311922),
                        contentDescription = "آیکون پیمان‌یار",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Text(
                        text = "پیمان‌یار • PeymanPro",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "دستیار بخشنامه ۵۰۹۰ و فهرست بها",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onSavedProjectsClick,
                modifier = Modifier.testTag("saved_projects_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "پرونده‌های ذخیره شده",
                    tint = AmberAccent
                )
            }
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier.testTag("app_info_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "راهنمای نرم‌افزار",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NavyPrimary,
            titleContentColor = Color.White
        ),
        modifier = modifier
    )
}
