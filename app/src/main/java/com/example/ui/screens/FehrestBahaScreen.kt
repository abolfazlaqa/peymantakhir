package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
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
import com.example.model.*
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.PeymanViewModel

@Composable
fun FehrestBahaScreen(
    viewModel: PeymanViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDiscipline by viewModel.selectedDiscipline.collectAsState()
    val searchQuery by viewModel.fehrestSearchQuery.collectAsState()
    val filteredItems = viewModel.getFilteredFehrestItems()

    var activeCalcItem by remember { mutableStateOf<FehrestItem?>(null) }
    var quantityInput by remember { mutableStateOf("10") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ۱. راهنمای ساختار فهرست بها
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "راهنمای فهارس بهای سازمان برنامه و بودجه",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NavyPrimary
                        )
                        Text(
                            text = "فهرست بها سالانه توسط سازمان برنامه جهت برآورد هزینه طرح‌های عمرانی منتشر می‌شود. کلیه قیمت‌ها پایه بوده و در قرارداد با اعمال ضرایب بالاسری، منطقه، طبقات و پیشنهادی به قیمت نهایی تبدیل می‌گردند.",
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // ۲. چیپ‌های انتخاب رشته
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FehrestDiscipline.values().forEach { disc ->
                    FilterChip(
                        selected = selectedDiscipline == disc,
                        onClick = { viewModel.setFehrestDiscipline(disc) },
                        label = { Text(disc.persianName, fontSize = 12.sp) },
                        modifier = Modifier.testTag("discipline_chip_${disc.name}")
                    )
                }
            }
        }

        // ۳. کادر جستجو
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setFehrestSearchQuery(it) },
                placeholder = { Text("جستجو بر اساس شماره ردیف، فصل یا شرح آیتم...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setFehrestSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "پاک کردن")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("fehrest_search_input")
            )
        }

        // ۴. نتایج و آیتم‌ها
        if (filteredItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "هیچ آیتمی مطابق جستجو یافت نشد.",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(filteredItems, key = { it.code }) { item ->
                FehrestItemCard(
                    item = item,
                    onOpenCalc = {
                        activeCalcItem = item
                        quantityInput = "10"
                    }
                )
            }
        }
    }

    // دیالوگ محاسبه سریع برآورد آیتم
    activeCalcItem?.let { item ->
        val qty = quantityInput.toDoubleOrNull() ?: 0.0
        val totalRial = (qty * item.basePriceRial).toLong()

        AlertDialog(
            onDismissRequest = { activeCalcItem = null },
            title = {
                Text(
                    text = "محاسبه برآورد ردیف ${item.code}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NavyPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "بهای واحد پایه: ${formatRial(item.basePriceRial)} به ازای هر ${item.unit}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                    OutlinedTextField(
                        value = quantityInput,
                        onValueChange = { quantityInput = it },
                        label = { Text("مقدار کار (${item.unit})") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("quick_calc_qty")
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "مبلغ کل برآورد پایه:", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = formatRial(totalRial),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                            Text(
                                text = formatToman(totalRial),
                                fontSize = 12.sp,
                                color = AmberAccent
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { activeCalcItem = null }) {
                    Text("بستن")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun FehrestItemCard(
    item: FehrestItem,
    onOpenCalc: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NavyPrimary.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ردیف ${item.code}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NavyPrimary
                    )
                }

                Text(
                    text = item.chapter,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Text(
                text = item.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B),
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "واحد: ${item.unit}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = formatRial(item.basePriceRial),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                }

                IconButton(
                    onClick = onOpenCalc,
                    modifier = Modifier.testTag("calc_btn_${item.code}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "محاسبه سریع مقدار",
                        tint = NavyPrimary
                    )
                }
            }

            if (item.technicalNote.isNotBlank()) {
                Text(
                    text = "نکته فنی: ${item.technicalNote}",
                    fontSize = 11.sp,
                    color = Color(0xFF0F766E),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
