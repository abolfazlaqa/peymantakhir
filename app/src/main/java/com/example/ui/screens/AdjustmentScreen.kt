package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.formatNumber
import com.example.model.formatRial
import com.example.model.formatToman
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DelayGreen
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.PeymanViewModel

@Composable
fun AdjustmentScreen(
    viewModel: PeymanViewModel,
    modifier: Modifier = Modifier
) {
    val baseIndexI0 by viewModel.baseIndexI0.collectAsState()
    val periodIndexI by viewModel.periodIndexI.collectAsState()
    val invoiceGrossWork by viewModel.invoiceGrossWork.collectAsState()
    val disciplineFactor by viewModel.disciplineFactor.collectAsState()

    val adjustmentAmount = viewModel.calculateAdjustmentAmount()
    val adjustmentFactor = if (baseIndexI0 > 0) (periodIndexI / baseIndexI0) - 1.0 else 0.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ۱. کارت نتیجه تعدیل صورت‌وضعیت
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "مبلغ ناخالص تعدیل آحاد بها (این دوره)",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp
                    )

                    Text(
                        text = formatRial(adjustmentAmount),
                        color = AmberAccent,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = formatToman(adjustmentAmount),
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "ضریب تعدیل (I/I0 - 1)", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = "+${formatNumber(adjustmentFactor * 100, 2)}٪",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DelayGreen
                            )
                        }

                        Column {
                            Text(text = "شاخص دوره / مبنا", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = "${formatNumber(periodIndexI, 0)} / ${formatNumber(baseIndexI0, 0)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // ۲. رابطه مستقیم ۵۰۹۰ و تعدیل آحاد بها (نکته کلیدی مهندسی)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(24.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "پیوند حیاتی بخشنامه ۵۰۹۰ با تعدیل آحاد بها",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF92400E)
                        )
                        Text(
                            text = "اگر تاخیرات پروژه با بخشنامه ۵۰۹۰ به عنوان «تاخیر مجاز» اثبات شود، کارکرد پیمانکار در ماه‌های تمدید شده با شاخص‌های تورمی همان دوره (شاخص بالاتر) تعدیل می‌گردد. اما در صورت تاخیر غیرمجاز، طبق بخشنامه تعدیل سازمان برنامه، کمترین شاخص بین زمان مقرر و واقعی ملاک خواهد بود و پیمانکار متضرر می‌شود!",
                            fontSize = 12.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // ۳. ورودی‌های فرمول تعدیل
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "محاسبه‌گر فرمول رسمی تعدیل سازمان برنامه",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NavyPrimary
                    )

                    Text(
                        text = "فرمول: E = B × [ ( I / I0 ) - 1 ] × ضریب رشته",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    OutlinedTextField(
                        value = invoiceGrossWork.toString(),
                        onValueChange = {
                            val v = it.filter { c -> c.isDigit() }.toLongOrNull() ?: 0L
                            viewModel.updateInvoiceGrossWork(v)
                        },
                        label = { Text("مبلغ ناخالص صورت‌وضعیت این دوره B (ریال)") },
                        supportingText = { Text(formatToman(invoiceGrossWork)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("adjustment_gross_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = baseIndexI0.toString(),
                            onValueChange = {
                                val v = it.toDoubleOrNull() ?: 1500.0
                                viewModel.updateBaseIndexI0(v)
                            },
                            label = { Text("شاخص مبنا I0") },
                            supportingText = { Text("شاخص دوره تسلیم پیشنهاد") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f).testTag("base_index_input")
                        )

                        OutlinedTextField(
                            value = periodIndexI.toString(),
                            onValueChange = {
                                val v = it.toDoubleOrNull() ?: 2000.0
                                viewModel.updatePeriodIndexI(v)
                            },
                            label = { Text("شاخص دوره کارکرد I") },
                            supportingText = { Text("شاخص فصلی سازمان برنامه") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f).testTag("period_index_input")
                        )
                    }

                    OutlinedTextField(
                        value = disciplineFactor.toString(),
                        onValueChange = {
                            val v = it.toDoubleOrNull() ?: 0.95
                            viewModel.updateDisciplineFactor(v)
                        },
                        label = { Text("ضریب شاخص رشته مربوطه") },
                        supportingText = { Text("معمولا ۰.۹۵ در پیمان‌های بدون حد نصاب") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
