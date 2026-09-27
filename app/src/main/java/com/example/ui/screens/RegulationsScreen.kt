package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClaimChecklistItem
import com.example.model.LegalArticle
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DelayGreen
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.PeymanViewModel

@Composable
fun RegulationsScreen(
    viewModel: PeymanViewModel,
    modifier: Modifier = Modifier
) {
    val legalArticles = viewModel.legalArticles
    val checklist = viewModel.claimChecklist

    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = NavyPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("قوانین و نشریه ۴۳۱۱", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("چک‌لیست لایحه تاخیرات", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(legalArticles, key = { it.id }) { article ->
                    LegalArticleExpandableCard(article = article)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "راهنمای تدوین لایحه تاخیرات پیمانکاری",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                            Text(
                                text = "برای اخذ تاییدیه مهندس مشاور و کارفرما و تصویب تاخیرات مجاز، مستندات زیر باید ضمیمه جدول محاسباتی ۵۰۹۰ شوند:",
                                fontSize = 12.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }

                items(checklist) { checkItem ->
                    ClaimChecklistCard(item = checkItem)
                }
            }
        }
    }
}

@Composable
private fun LegalArticleExpandableCard(article: LegalArticle) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NavyPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = article.articleNumber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    }
                    Text(
                        text = article.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Text(
                text = article.summary,
                fontSize = 12.sp,
                color = Color(0xFF475569),
                lineHeight = 17.sp
            )

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    Text(
                        text = "متن کامل و تحلیل حقوقی:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NavyPrimary
                    )

                    Text(
                        text = article.fullText,
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 18.sp
                    )

                    if (article.practicalEngineeringTips.isNotEmpty()) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = Color(0xFFB45309),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "نکات طلایی و اجرایی برای مهندسان:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color(0xFF92400E)
                                    )
                                }

                                article.practicalEngineeringTips.forEach { tip ->
                                    Text(
                                        text = "• $tip",
                                        fontSize = 11.sp,
                                        color = Color(0xFF78350F),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClaimChecklistCard(item: ClaimChecklistItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyPrimary
            )

            Text(
                text = item.description,
                fontSize = 12.sp,
                color = Color(0xFF475569),
                lineHeight = 18.sp
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "مدارک و ضمائم پیوست:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF0F766E)
                )
                item.requiredDocuments.forEach { doc ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = DelayGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = doc,
                            fontSize = 11.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }
        }
    }
}
