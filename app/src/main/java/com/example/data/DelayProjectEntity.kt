package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delay_projects")
data class DelayProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val contractor: String,
    val employer: String,
    val contractAmountP: Long,
    val initialDurationDays: Int,
    val advancePaymentsData: String, // CSV or format encoded
    val interimInvoicesData: String, // CSV or format encoded
    val calculatedExtensionDays: Double,
    val dateCreated: Long = System.currentTimeMillis()
)
