package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DelayProjectEntity
import com.example.model.formatNumber
import com.example.model.formatRial
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.NavyPrimary

@Composable
fun SavedProjectsDialog(
    projects: List<DelayProjectEntity>,
    onDelete: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "پرونده‌های ذخیره شده پیمان",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = NavyPrimary
            )
        },
        text = {
            if (projects.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "هیچ پرونده‌ای ذخیره نشده است.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(projects, key = { it.id }) { project ->
                        SavedProjectItemCard(
                            project = project,
                            onCopy = {
                                val reportText = buildString {
                                    appendLine("📋 گزارش تاخیرات پیمانکاری (بخشنامه ۵۰۹۰)")
                                    appendLine("عنوان پروژه: ${project.title}")
                                    appendLine("پیمانکار: ${project.contractor}")
                                    appendLine("کارفرما: ${project.employer}")
                                    appendLine("مبلغ اولیه پیمان: ${formatRial(project.contractAmountP)}")
                                    appendLine("مدت اولیه پیمان: ${project.initialDurationDays} روز")
                                    appendLine("تمدید مجاز محاسبه شده: ${formatNumber(project.calculatedExtensionDays, 1)} روز")
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Peyman Report", reportText)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "گزارش در حافظه کپی شد", Toast.LENGTH_SHORT).show()
                            },
                            onDelete = { onDelete(project.id) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("بستن")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun SavedProjectItemCard(
    project: DelayProjectEntity,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NavyPrimary,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onCopy) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "کپی گزارش",
                            tint = NavyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "حذف پرونده",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Text(
                text = "کارفرما: ${project.employer} | پیمانکار: ${project.contractor}",
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مبلغ: ${formatRial(project.contractAmountP)}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AmberAccent.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "تمدید: ${formatNumber(project.calculatedExtensionDays, 1)} روز",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                }
            }
        }
    }
}
