package com.example.model

/**
 * رشته‌های اصلی فهرست بها
 */
enum class FehrestDiscipline(val persianName: String, val code: String) {
    ABNIYEH("ابنیه و ساختمان", "01"),
    MECHANICAL("تاسیسات مکانیکی", "02"),
    ELECTRICAL("تاسیسات برقی", "03"),
    ROAD("راه، راه‌آهن و باند", "04"),
    WATER("توزیع و انتقال آب", "05")
}

/**
 * آیتم فهرست بها
 */
data class FehrestItem(
    val code: String, // شماره ردیف مثلا ۰۷۰۱۰۱
    val chapter: String, // عنوان فصل مثلا فصل هفتم - بتن درجا
    val discipline: FehrestDiscipline,
    val description: String, // شرح عملیات
    val unit: String, // واحد مثلا متر مکعب، کیلوگرم، متر مربع
    val basePriceRial: Long, // بهای واحد به ریال در فهرست بهای پایه
    val technicalNote: String = "" // توضیحات فنی یا ضوابط اضافه بها
)

/**
 * اطلاعات فصول فهرست بها
 */
data class FehrestChapterInfo(
    val chapterNumber: Int,
    val title: String,
    val discipline: FehrestDiscipline,
    val description: String,
    val keyItemsCount: Int
)

/**
 * ضرایب پیمان
 */
data class ContractCoefficients(
    val isGovernmentalProject: Boolean = true, // طرح عمرانی (1.41) یا غیرعمرانی (1.30)
    val customOverhead: Double = 1.41, // ضریب بالاسری
    val regionalFactor: Double = 1.05, // ضریب منطقه
    val contractorTenderFactor: Double = 1.00, // ضریب پیشنهادی پیمانکار (پلوس یا مینوس)
    val floorFactor: Double = 1.00, // ضریب طبقات
    val heightFactor: Double = 1.00, // ضریب ارتفاع
    val siteMobilizationPercentage: Double = 4.0 // درصد تجهیز و برچیدن کارگاه
) {
    /**
     * محاسبه ضریب کل پیمان (حاصلضرب ضرایب)
     */
    fun totalContractMultiplier(): Double {
        val overhead = if (isGovernmentalProject) 1.41 else 1.30
        return overhead * regionalFactor * contractorTenderFactor * floorFactor * heightFactor
    }

    /**
     * محاسبه مبلغ نهایی پیمان بر اساس برآورد پایه
     */
    fun calculateTotalContractPrice(baseEstimateAmount: Long): Long {
        val multiplied = (baseEstimateAmount * totalContractMultiplier()).toLong()
        val mobilizationAmount = (baseEstimateAmount * (siteMobilizationPercentage / 100.0)).toLong()
        return multiplied + mobilizationAmount
    }
}
