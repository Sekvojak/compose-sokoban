package com.example.composesokobantemplatev2.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composesokobantemplatev2.game.loadLevels
import com.example.composesokobantemplatev2.getBestScore
import com.example.composesokobantemplatev2.hasProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectScreen(onSelect: (Int) -> Unit) {
    val context = LocalContext.current // pristup k assets
    val levels = remember { loadLevels(context) }

    Column(Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Sokoban — Select Level") }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(levels) { index, level ->

                val preview: Bitmap = remember { previewLevelBitmap(context, level) }
                val best = remember { getBestScore(context, index) }
                val bestText = if (best == Int.MAX_VALUE) "-" else best.toString()

                val inProgress = hasProgress(context, index)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(index) }
                        .height(80.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // preview
                    Image(
                        bitmap = preview.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(end = 12.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Level ${index + 1}")

                        when {
                            inProgress -> Text("🟡 In progress", color = Color(0xFFCC9A00))
                            best != Int.MAX_VALUE -> Text("✅ Done", color = Color(0xFF2E7D32))
                        }
                    }

                    // best score
                    Text(
                        text = "Best: $bestText",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Divider(
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.4f)
                )
            }
        }
    }
}
