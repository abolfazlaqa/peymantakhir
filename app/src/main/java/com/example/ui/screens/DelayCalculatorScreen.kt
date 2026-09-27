package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.ContractSummaryCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DelayGreen
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.PeymanViewModel

@Composable
fun DelayCalculatorScreen(
    viewModel: PeymanViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val projectTitle by viewModel.projectTitle.collectAsState()
    val contractorName by viewModel.contractorName.collectAsState()
    val employerName by viewModel.employerName.collectAsState()
    val contractAmountP by viewModel.contractAmountP.collectAsState()
    val initialDurationDays by viewModel.initialDurationDays.collectAsState()
    val advancePayments by viewModel.advancePayments.collectAsState()
    val interimInvoices by viewModel.interimInvoices.collectAsState()
    val overlapDays by viewModel.overlapDays.collectAsState()

    val summary = viewModel.calculateSummary()

    var showAddAdvanceDialog by remember { mutableStateOf(false) }
    var showAddInvoiceDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ۱. کارت خلاصه نتایج بخشنامه ۵۰۹۰
        item {
            ContractSummaryCard(summary = summary)
        }

        // ۲. مشخصات عمومی پیمان
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = NavyPrimary
                        )
                        Text(
                            text = "مشخصات کلی قرارداد و پیمان",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NavyPrimary
                        )
                    }

                    OutlinedTextField(
                        value = projectTitle,
                        onValueChange = { viewModel.updateProjectTitle(it) },
                        label = { Text("عنوان پروژه / موضوع پیمان") },
                        modifier = Modifier.fillMaxWidth().testTag("project_title_field")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = contractorName,
                            onValueChange = { viewModel.updateContractorName(it) },
                            label = { Text("نام پیمانکار") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = employerName,
                            onValueChange = { viewModel.updateEmployerName(it) },
                            label = { Text("دستگاه اجرایی / کارفرما") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = contractAmountP.toString(),
                            onValueChange = {
                                val v = it.filter { c -> c.isDigit() }.toLongOrNull() ?: 0L
                                viewModel.updateContractAmountP(v)
                            },
                            label = { Text("مبلغ اولیه پیمان P (ریال)") },
                            supportingText = { Text(formatToman(contractAmountP)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1.4f).testTag("contract_p_input")
                        )
                        OutlinedTextField(
                            value = initialDurationDays.toString(),
                            onValueChange = {
                                val v = it.filter { c -> c.isDigit() }.toIntOrNull() ?: 365
                                viewModel.updateInitialDurationDays(v)
                            },
                            label = { Text("مدت پیمان (روز)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.9f)
                        )
                    }
                }
            }
        }

        // ۳. بند ۱: اقساط پیش‌پرداخت
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = AmberAccent
                            )
                            Column {
                                Text(
                                    text = "بند ۱: تاخیرات اقساط پیش‌پرداخت",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "فرمول: T = (A × (t2 - t1)) / P",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        FilledTonalButton(
                            onClick = { showAddAdvanceDialog = true },
                            modifier = Modifier.testTag("add_advance_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("افزودن قسط", fontSize = 12.sp)
                        }
                    }

                    if (advancePayments.isEmpty()) {
                        Text(
                            text = "هیچ تاخیری در پرداخت پیش‌پرداخت ثبت نشده است.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        advancePayments.forEach { item ->
                            val extDays = item.calculateExtensionDays(contractAmountP)
                            AdvancePaymentRowItem(
                                item = item,
                                extensionDays = extDays,
                                onDelete = { viewModel.removeAdvancePayment(item.id) }
                            )
                        }
                    }
                }
            }
        }

        // ۴. بند ۲: صورت‌وضعیت‌های موقت
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = NavyPrimary
                            )
                            Column {
                                Text(
                                    text = "بند ۲: صورت‌وضعیت‌های موقت کارکرد",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "فرمول: T = (A × (t2 - t1)) / B",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        FilledTonalButton(
                            onClick = { showAddInvoiceDialog = true },
                            modifier = Modifier.testTag("add_invoice_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("افزودن صورت‌وضعیت", fontSize = 12.sp)
                        }
                    }

                    if (interimInvoices.isEmpty()) {
                        Text(
                            text = "هیچ صورت‌وضعیتی ثبت نشده است.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        interimInvoices.forEach { item ->
                            val extDays = item.calculateExtensionDays()
                            InterimInvoiceRowItem(
                                item = item,
                                extensionDays = extDays,
                                onDelete = { viewModel.removeInterimInvoice(item.id) }
                            )
                        }
                    }
                }
            }
        }

        // ۵. تنظیمات کسر همپوشانی (Overlap Deduction)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.MergeType, contentDescription = null, tint = AmberAccent)
                        Text(
                            text = "تحلیل همپوشانی تاخیرات موازی",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NavyPrimary
                        )
                    }
                    Text(
                        text = "طبق اصول فنی بخشنامه ۵۰۹۰، تاخیرات همزمان چند صورت‌وضعیت نباید دوباره محاسبه شوند. روزهای تداخل را در این بخش کسر نمایید:",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    OutlinedTextField(
                        value = overlapDays.toString(),
                        onValueChange = {
                            val v = it.toDoubleOrNull() ?: 0.0
                            viewModel.updateOverlapDays(v)
                        },
                        label = { Text("روزهای همپوشانی جهت کسر") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // ۶. دکمه‌های عملیاتی: ذخیره پرونده و کپی گزارش لایحه
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.saveCurrentProjectToDb()
                        Toast.makeText(context, "پرونده پیمان در دیتابیس ذخیره گردید", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f).testTag("save_project_btn")
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ذخیره پرونده")
                }

                OutlinedButton(
                    onClick = {
                        val reportText = buildString {
                            appendLine("═══════════════════════════════════════")
                            appendLine("📋 لایحه تاخیرات و تمدید مجاز پیمان (بخشنامه ۵۰۹۰)")
                            appendLine("═══════════════════════════════════════")
                            appendLine("• پروژه: $projectTitle")
                            appendLine("• پیمانکار: $contractorName | کارفرما: $employerName")
                            appendLine("• مبلغ اولیه پیمان (P): ${formatRial(contractAmountP)}")
                            appendLine("• مدت اولیه پیمان: $initialDurationDays روز")
                            appendLine("---------------------------------------")
                            appendLine("۱. تاخیرات پیش‌پرداخت (بند ۱): ${formatNumber(summary.totalAdvanceExtensionDays, 2)} روز")
                            advancePayments.forEach {
                                appendLine("   - ${it.installmentName}: ${it.delayDays} روز تاخیر ➔ تمدید: ${formatNumber(it.calculateExtensionDays(contractAmountP), 2)} روز")
                            }
                            appendLine("۲. تاخیرات صورت‌وضعیت‌ها (بند ۲): ${formatNumber(summary.totalInvoiceExtensionDays, 2)} روز")
                            interimInvoices.forEach {
                                appendLine("   - ${it.invoiceNumber}: کارکرد B=${formatRial(it.grossWorkAmountB)}، پرداخت نشده A=${formatRial(it.unpaidAmountA)}، تاخیر: ${it.delayDays} روز ➔ تمدید: ${formatNumber(it.calculateExtensionDays(), 2)} روز")
                            }
                            appendLine("۳. کسر همپوشانی: -${formatNumber(summary.overlapDeductionDays, 2)} روز")
                            appendLine("═══════════════════════════════════════")
                            appendLine("⭐ مجموع تمدید مجاز پیمان: ${formatNumber(summary.netAllowedExtensionDays, 2)} روز")
                            appendLine("⭐ مدت کل جدید پیمان: ${formatNumber(summary.newTotalDurationDays, 0)} روز (+${formatNumber(summary.extensionPercentageOfContract, 1)}٪)")
                            appendLine("مستند به بخشنامه ۵۰۹۰ و مواد ۳۷ و ۵۰ شرایط عمومی پیمان")
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Peyman 5090 Report", reportText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "گزارش کامل در حافظه کپی شد", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f).testTag("copy_report_btn")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("کپی لایحه")
                }
            }
        }
    }

    if (showAddAdvanceDialog) {
        AddAdvancePaymentDialog(
            onDismiss = { showAddAdvanceDialog = false },
            onConfirm = {
                viewModel.addAdvancePayment(it)
                showAddAdvanceDialog = false
            }
        )
    }

    if (showAddInvoiceDialog) {
        AddInterimInvoiceDialog(
            onDismiss = { showAddInvoiceDialog = false },
            onConfirm = {
                viewModel.addInterimInvoice(it)
                showAddInvoiceDialog = false
            }
        )
    }
}

@Composable
private fun AdvancePaymentRowItem(
    item: AdvancePaymentItem,
    extensionDays: Double,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = item.installmentName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyPrimary
                )
                Text(
                    text = "مبلغ A: ${formatRial(item.requestedAmount)} • تاخیر: ${item.delayDays} روز",
                    fontSize = 11.sp,
                    color = Color(0xFF475569)
                )
                if (item.notes.isNotBlank()) {
                    Text(
                        text = item.notes,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AmberAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${formatNumber(extensionDays, 1)} روز",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "حذف قسط",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InterimInvoiceRowItem(
    item: InterimInvoiceItem,
    extensionDays: Double,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = item.invoiceNumber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyPrimary
                )
                Text(
                    text = "کارکرد B: ${formatRial(item.grossWorkAmountB)}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "طلب A: ${formatRial(item.unpaidAmountA)} • تاخیر: ${item.delayDays} روز",
                    fontSize = 11.sp,
                    color = Color(0xFF334155)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DelayGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${formatNumber(extensionDays, 1)} روز",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DelayGreen
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "حذف صورت‌وضعیت",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
