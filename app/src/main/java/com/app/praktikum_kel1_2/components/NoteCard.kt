package com.app.praktikum_kel1_2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.praktikum_kel1_2.model.response.NoteItem

/**
 * NoteCard menampilkan satu catatan dalam tampilan elegan yang senada dengan screen lainnya.
 *
 * @param note Objek NoteItem yang akan ditampilkan.
 */
@Composable
fun NoteCard(note: NoteItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFe0eafc), Color(0xFFf9f9f9))
                    )
                )
                .padding(20.dp)
        ) {
            Text(
                text = note.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F51B5)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "✍️ Oleh: ${note.nm_lengkap}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = note.content,
                fontSize = 15.sp,
                color = Color(0xFF333333),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )
        }
    }
}
