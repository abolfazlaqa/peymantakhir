package com.example.model

import java.text.NumberFormat
import java.util.Locale

/**
 * مدل قسط پیش‌پرداخت جهت محاسبه تاخیر طبق بند ۱ بخشنامه ۵۰۹۰
 * فرمول: T = (A * (t2 - t1)) / P
 */
data class AdvancePaymentItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val installmentName: String = "قسط اول پیش‌پرداخت",
    val requestedAmount: Long = 0L, // مبلغ قسط پیش‌پرداخت (A)
    val submissionDaysOffset: Int = 10, // مهلت رسیدگی و پرداخت مقرر (مثلا ۱۰ روز پس از تحویل ضمانتنامه)
    val delayDays: Int = 0, // تعداد روزهای تاخیر در پرداخت (t2 - t1)
    val notes: String = ""
) {
    /**
     * محاسبه روزهای تمدید پیمان ناشی از این پیش‌پرداخت
     */
    fun calculateExtensionDays(contractAmountP: Long): Double {
        if (contractAmountP <= 0 || requestedAmount <= 0 || delayDays <= 0) return 0.0
        return (requestedAmount.toDouble() * delayDays) / contractAmountP.toDouble()
    }
}

/**
 * مدل صورت‌وضعیت موقت جهت محاسبه تاخیر طبق بند ۲ بخشنامه ۵۰۹۰
 * فرمول: T = (A * (t2 - t1)) / B
 */
data class InterimInvoiceItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val invoiceNumber: String = "صورت‌وضعیت موقت شماره ۱",
    val grossWorkAmountB: Long = 0L, // مبلغ کارکرد صورت‌وضعیت در این دوره (B)
    val unpaidAmountA: Long = 0L, // مبلغ پرداخت نشده صورت‌وضعیت در موعد مقرر (A)
    val delayDays: Int = 0, // تعداد روزهای تاخیر در پرداخت (t2 - t1)
    val isDeductedOverlap: Boolean = false, // آیا همپوشانی دارد
    val notes: String = ""
) {
    /**
     * محاسبه روزهای تمدید ناشی از این صورت‌وضعیت
     */
    fun calculateExtensionDays(): Double {
        if (grossWorkAmountB <= 0 || unpaidAmountA <= 0 || delayDays <= 0) return 0.0
        // اگر مبلغ پرداخت نشده از کل کارکرد بیشتر ثبت شده باشد، سقف آن B است
        val effectiveA = unpaidAmountA.coerceAtMost(grossWorkAmountB)
        return (effectiveA.toDouble() * delayDays) / grossWorkAmountB.toDouble()
    }
}

/**
 * نتیجه تحلیل و خلاصه محاسبه تاخیرات پیمان
 */
data class DelayClaimSummary(
    val totalAdvanceExtensionDays: Double = 0.0,
    val totalInvoiceExtensionDays: Double = 0.0,
    val totalRawExtensionDays: Double = 0.0,
    val overlapDeductionDays: Double = 0.0,
    val netAllowedExtensionDays: Double = 0.0,
    val extensionPercentageOfContract: Double = 0.0,
    val initialDurationDays: Int = 365,
    val newTotalDurationDays: Double = 365.0
)

fun formatRial(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "${formatter.format(amount)} ریال"
}

fun formatToman(rialAmount: Long): String {
    val toman = rialAmount / 10
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "${formatter.format(toman)} تومان"
}

fun formatNumber(number: Double, decimals: Int = 2): String {
    return String.format(Locale.US, "%.${decimals}f", number)
}
