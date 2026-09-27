package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Calculate
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
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.PeymanViewModel

@Composable
fun CoefficientsScreen(
    viewModel: PeymanViewModel,
    modifier: Modifier = Modifier
) {
    val coefficients by viewModel.coefficients.collectAsState()
    val baseEstimate by viewModel.baseEstimateAmount.collectAsState()
    val selectedRegion by viewModel.selectedRegionName.collectAsState()
    val regionalFactors = viewModel.regionalFactors

    val totalMultiplier = coefficients.totalContractMultiplier()
    val finalContractAmount = coefficients.calculateTotalContractPrice(baseEstimate)
    val mobilizationAmount = (baseEstimate * (coefficients.siteMobilizationPercentage / 100.0)).toLong()

    var regionMenuExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ۱. کارت نتیجه مبلغ کل پیمان با ضرایب
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "مبلغ نهایی پیمان با اعمال کلیه ضرایب",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp
                    )

                    Text(
                        text = formatRial(finalContractAmount),
                        color = AmberAccent,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = formatToman(finalContractAmount),
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "ضریب کل پیمان", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = formatNumber(totalMultiplier, 4),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Column {
                            Text(text = "هزینه تجهیز کارگاه", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = formatRial(mobilizationAmount),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DelayGreen
                            )
                        }
                    }
                }
            }
        }

        // ۲. مبلغ برآورد پایه
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "مبلغ کل برآورد پایه (ناخالص اقلام فهرست بها)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyPrimary
                    )
                    OutlinedTextField(
                        value = baseEstimate.toString(),
                        onValueChange = {
                            val v = it.filter { c -> c.isDigit() }.toLongOrNull() ?: 0L
                            viewModel.updateBaseEstimate(v)
                        },
                        label = { Text("برآورد پایه ریال") },
                        supportingText = { Text(formatToman(baseEstimate)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("base_estimate_input")
                    )
                }
            }
        }

        // ۳. ضریب بالاسری (Overhead Factor)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "۱. ضریب بالاسری (Overhead)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NavyPrimary
                        )

                        Text(
                            text = if (coefficients.isGovernmentalProject) "۱.۴۱ (طرح عمرانی)" else "۱.۳۰ (طرح غیرعمرانی)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AmberAccent
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = coefficients.isGovernmentalProject,
                            onClick = { viewModel.updateIsGovernmental(true) },
                            label = { Text("طرح‌های عمرانی ملی/استانی (۱.۴۱)") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = !coefficients.isGovernmentalProject,
                            onClick = { viewModel.updateIsGovernmental(false) },
                            label = { Text("طرح‌های غیرعمرانی (۱.۳۰)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = "آنالیز بالاسری شامل: سهم بیمه کارفرما و پیمانکار (۱.۶٪ و ۶.۶٪)، مالیات عملکرد، سود پیمانکار، هزینه‌های دفتر مرکزی، ضمانت‌نامه‌ها، حوادث، ایاب و ذهاب، و آزمایشگاه کنترل کیفیت کارگاه است.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // ۴. ضریب منطقه (Regional Factor)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "۲. ضریب منطقه (Regional Factor)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyPrimary
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            .clickable { regionMenuExpanded = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = selectedRegion, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text(
                                    text = "ضریب منطقه: ${formatNumber(coefficients.regionalFactor, 2)}",
                                    fontSize = 11.sp,
                                    color = AmberAccent
                                )
                            }
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = regionMenuExpanded,
                            onDismissRequest = { regionMenuExpanded = false }
                        ) {
                            regionalFactors.forEach { (region, factor) ->
                                DropdownMenuItem(
                                    text = { Text("$region (ضریب $factor)") },
                                    onClick = {
                                        viewModel.updateSelectedRegion(region)
                                        regionMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ۵. سایر ضرایب (پیشنهادی پیمانکار، طبقات، ارتفاع و تجهیز کارگاه)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "۳. ضریب پیشنهادی پیمانکار (ضریب پیمان)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyPrimary
                    )
                    OutlinedTextField(
                        value = coefficients.contractorTenderFactor.toString(),
                        onValueChange = {
                            val v = it.toDoubleOrNull() ?: 1.0
                            viewModel.updateTenderFactor(v)
                        },
                        label = { Text("ضریب پیمانکار (پلوس/مینوس)") },
                        supportingText = {
                            val diff = ((coefficients.contractorTenderFactor - 1.0) * 100.0)
                            val desc = if (diff < 0) "${formatNumber(-diff, 1)}٪ تخفیف (مینوس)" else if (diff > 0) "${formatNumber(diff, 1)}٪ افزایش (پلوس)" else "بدون تخفیف (نت)"
                            Text(desc)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("tender_factor_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = coefficients.floorFactor.toString(),
                            onValueChange = {
                                val v = it.toDoubleOrNull() ?: 1.0
                                viewModel.updateFloorFactor(v)
                            },
                            label = { Text("ضریب طبقات") },
                            supportingText = { Text("طبق پیوست ۲") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = coefficients.heightFactor.toString(),
                            onValueChange = {
                                val v = it.toDoubleOrNull() ?: 1.0
                                viewModel.updateHeightFactor(v)
                            },
                            label = { Text("ضریب ارتفاع") },
                            supportingText = { Text("سقف بالای ۳.۵متر") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = coefficients.siteMobilizationPercentage.toString(),
                        onValueChange = {
                            val v = it.toDoubleOrNull() ?: 4.0
                            viewModel.updateMobilizationPercent(v)
                        },
                        label = { Text("درصد تجهیز و برچیدن کارگاه (٪)") },
                        supportingText = { Text("سقف قانونی معمولا ۴٪ برای ابنیه و راه و ۲.۵٪ برای تاسیسات") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
