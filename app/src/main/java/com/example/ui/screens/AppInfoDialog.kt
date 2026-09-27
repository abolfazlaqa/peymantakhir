package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.NavyPrimary

@Composable
fun AppInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_peyman_1790438311922),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = "درباره سامانه پیمان‌یار",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NavyPrimary
                    )
                    Text(
                        text = "مرجع محاسبات بخشنامه ۵۰۹۰ و فهرست بها",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Banner Hero
                Image(
                    painter = painterResource(id = R.drawable.banner_hero),
                    contentDescription = "بنر پیمان‌یار",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "پاسخ به سوالات فنی شما:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AmberAccent
                )

                Text(
                    text = "• بخشنامه ۵۰۹۰ سازمان برنامه و بودجه چیست؟\nدستورالعمل رسمی مصوب سازمان برنامه جهت محاسبه تمدید مجاز پیمان ناشی از تاخیر کارفرما در پرداخت اقساط پیش‌پرداخت (بند ۱: T = A(t2-t1)/P) و صورت‌وضعیت‌های موقت (بند ۲: T = A(t2-t1)/B). این بخشنامه از تحمیل خسارت تاخیرات ماده ۵۰ به پیمانکار جلوگیری کرده و زمینه تعدیل دوره‌های تمدید شده را فراهم می‌کند.",
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 17.sp
                )

                Text(
                    text = "• فهارس بهای پایه سازمان برنامه چیستند؟\nدفترچه‌های سالیانه نرخ‌های پایه عملیات اجرایی عمرانی در رشته‌های ابنیه، تاسیسات مکانیکی و برقی، راه و ترابری، آب و ... که بر مبنای آنها با اعمال ضرایب بالاسری (۱.۴۱ یا ۱.۳۰)، منطقه، طبقات، ارتفاع، تجهیز کارگاه و ضریب پیشنهادی پیمانکار، مبلغ برآورد و پرداخت قراردادها معین می‌شود.",
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 17.sp
                )

                Text(
                    text = "• ارتباط ۵۰۹۰ و تعدیل آحاد بها:\nبزرگترین مزیت بخشنامه ۵۰۹۰، شمولیت تعدیل آحاد بها با شاخص‌های دوره واقعی انجام کار است؛ در حالی که در تاخیرات غیرمجاز، کمترین شاخص بین زمان مقرر و انجام کار اعمال شده و به پیمانکار خسارت سنگین وارد می‌شود.",
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 17.sp
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("متوجه شدم")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
