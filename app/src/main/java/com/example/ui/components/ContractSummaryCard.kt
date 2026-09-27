package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DelayClaimSummary
import com.example.model.formatNumber
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DelayGreen
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary

@Composable
fun ContractSummaryCard(
    summary: DelayClaimSummary,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("contract_summary_card")
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(NavyPrimary, NavyLight)
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "تمدید مجاز پیمان (بخشنامه ۵۰۹۰)",
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = formatNumber(summary.netAllowedExtensionDays, 1),
                                color = AmberAccent,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "روز کاری",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }

                    // Badge درصد تمدید
                    Box(
                        modifier = Modifier
                            .background(
                                color = AmberAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "+${formatNumber(summary.extensionPercentageOfContract, 1)}٪ مدت اولیه",
                            color = AmberAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                // ریز اقلام تمدید
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricMiniItem(
                        label = "تاخیر پیش‌پرداخت",
                        value = "${formatNumber(summary.totalAdvanceExtensionDays, 1)} روز",
                        color = Color.White
                    )
                    MetricMiniItem(
                        label = "تاخیر صورت‌وضعیت‌ها",
                        value = "${formatNumber(summary.totalInvoiceExtensionDays, 1)} روز",
                        color = Color.White
                    )
                    MetricMiniItem(
                        label = "کسر همپوشانی",
                        value = "-${formatNumber(summary.overlapDeductionDays, 1)} روز",
                        color = Color(0xFFF87171)
                    )
                    MetricMiniItem(
                        label = "مدت کل نهایی",
                        value = "${formatNumber(summary.newTotalDurationDays, 0)} روز",
                        color = DelayGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricMiniItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF94A3B8)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
