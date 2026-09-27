package com.example.model

/**
 * مدل بندهای قانونی و بخشنامه‌ها
 */
data class LegalArticle(
    val id: String,
    val title: String,
    val source: String, // نشریه ۴۳۱۱ یا بخشنامه ۵۰۹۰
    val articleNumber: String,
    val summary: String,
    val fullText: String,
    val practicalEngineeringTips: List<String>
)

/**
 * مدل چک‌لیست لایحه تاخیرات
 */
data class ClaimChecklistItem(
    val title: String,
    val description: String,
    val requiredDocuments: List<String>
)
