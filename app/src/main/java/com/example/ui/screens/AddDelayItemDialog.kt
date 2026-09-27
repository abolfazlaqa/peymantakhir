package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdvancePaymentItem
import com.example.model.InterimInvoiceItem
import com.example.ui.theme.NavyPrimary

@Composable
fun AddAdvancePaymentDialog(
    initialItem: AdvancePaymentItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (AdvancePaymentItem) -> Unit
) {
    var title by remember { mutableStateOf(initialItem?.installmentName ?: "قسط پیش‌پرداخت") }
    var amountStr by remember { mutableStateOf(initialItem?.requestedAmount?.toString() ?: "3000000000") }
    var delayDaysStr by remember { mutableStateOf(initialItem?.delayDays?.toString() ?: "30") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialItem == null) "افزودن قسط پیش‌پرداخت (بند ۱)" else "ویرایش قسط پیش‌پرداخت",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = NavyPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان قسط پیش‌پرداخت") },
                    modifier = Modifier.fillMaxWidth().testTag("advance_title_input")
                )
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it.filter { char -> char.isDigit() } },
                    label = { Text("مبلغ قسط پرداخت نشده A (ریال)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("advance_amount_input")
                )
                OutlinedTextField(
                    value = delayDaysStr,
                    onValueChange = { delayDaysStr = it.filter { char -> char.isDigit() } },
                    label = { Text("تعداد روزهای تاخیر کارفرما (t2 - t1)") },
                    supportingText = { Text("از تاریخ سررسید تعهد تا پرداخت واقعی") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("advance_days_input")
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("توضیحات و مستندات") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountStr.toLongOrNull() ?: 0L
                    val days = delayDaysStr.toIntOrNull() ?: 0
                    val newItem = AdvancePaymentItem(
                        id = initialItem?.id ?: java.util.UUID.randomUUID().toString(),
                        installmentName = title.ifBlank { "قسط پیش‌پرداخت" },
                        requestedAmount = amount,
                        delayDays = days,
                        notes = notes
                    )
                    onConfirm(newItem)
                },
                modifier = Modifier.testTag("save_advance_btn")
            ) {
                Text("ثبت و محاسبه")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun AddInterimInvoiceDialog(
    initialItem: InterimInvoiceItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (InterimInvoiceItem) -> Unit
) {
    var title by remember { mutableStateOf(initialItem?.invoiceNumber ?: "صورت‌وضعیت موقت") }
    var grossAmountStr by remember { mutableStateOf(initialItem?.grossWorkAmountB?.toString() ?: "4000000000") }
    var unpaidAmountStr by remember { mutableStateOf(initialItem?.unpaidAmountA?.toString() ?: "3500000000") }
    var delayDaysStr by remember { mutableStateOf(initialItem?.delayDays?.toString() ?: "30") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialItem == null) "افزودن صورت‌وضعیت موقت (بند ۲)" else "ویرایش صورت‌وضعیت موقت",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = NavyPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("شماره / عنوان صورت‌وضعیت") },
                    modifier = Modifier.fillMaxWidth().testTag("invoice_title_input")
                )
                OutlinedTextField(
                    value = grossAmountStr,
                    onValueChange = { grossAmountStr = it.filter { char -> char.isDigit() } },
                    label = { Text("مبلغ کارکرد این دوره B (ریال)") },
                    supportingText = { Text("مبلغ خالص کارکرد دوره صورت‌وضعیت") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("invoice_gross_input")
                )
                OutlinedTextField(
                    value = unpaidAmountStr,
                    onValueChange = { unpaidAmountStr = it.filter { char -> char.isDigit() } },
                    label = { Text("مبلغ پرداخت‌نشده A (ریال)") },
                    supportingText = { Text("طلب پیمانکار پس از کسر کسورات قانونی") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("invoice_unpaid_input")
                )
                OutlinedTextField(
                    value = delayDaysStr,
                    onValueChange = { delayDaysStr = it.filter { char -> char.isDigit() } },
                    label = { Text("روزهای تاخیر (t2 - t1)") },
                    supportingText = { Text("فاصله تاریخ پرداخت تا روز بیستم پس از تسلیم (ماده ۳۷)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("invoice_days_input")
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("توضیحات (دلیل تاخیر، شماره چک، ...)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val grossB = grossAmountStr.toLongOrNull() ?: 0L
                    val unpaidA = unpaidAmountStr.toLongOrNull() ?: 0L
                    val days = delayDaysStr.toIntOrNull() ?: 0
                    val newItem = InterimInvoiceItem(
                        id = initialItem?.id ?: java.util.UUID.randomUUID().toString(),
                        invoiceNumber = title.ifBlank { "صورت‌وضعیت موقت" },
                        grossWorkAmountB = grossB,
                        unpaidAmountA = unpaidA,
                        delayDays = days,
                        notes = notes
                    )
                    onConfirm(newItem)
                },
                modifier = Modifier.testTag("save_invoice_btn")
            ) {
                Text("ثبت و محاسبه")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
