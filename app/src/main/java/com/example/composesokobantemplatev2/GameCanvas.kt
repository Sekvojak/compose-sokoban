package com.example.composesokobantemplatev2.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.composesokobantemplatev2.R
import kotlin.math.roundToInt

@Composable
fun GameCanvas(
    levelData: List<Int>,
    levelWidth: Int,
    levelHeight: Int,
    modifier: Modifier = Modifier
)
{
    val context = LocalContext.current

    val tiles = remember {
        arrayOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.empty).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.wall).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.box).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.goal).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.hero).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.boxok).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.hero).asImageBitmap(),
        )
    }

    Canvas(modifier = modifier) {
        val tileW = size.width / levelWidth
        val tileH = size.height / levelHeight
        val tileSize = minOf(tileW, tileH)

        for (y in 0 until levelHeight) {
            for (x in 0 until levelWidth) {
                val tileIndex = levelData[y * levelWidth + x]
                val img: ImageBitmap = tiles[tileIndex.coerceIn(0, tiles.lastIndex)]

                drawImage(
                    image = img,
                    dstOffset = IntOffset((x * tileSize).roundToInt(), (y * tileSize).roundToInt()),
                    dstSize = IntSize(tileSize.roundToInt(), tileSize.roundToInt())
                )
            }
        }
    }
}
