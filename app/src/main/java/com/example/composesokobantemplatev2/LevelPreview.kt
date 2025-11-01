package com.example.composesokobantemplatev2.ui

import android.content.Context
import android.graphics.*
import androidx.annotation.DrawableRes
import com.example.composesokobantemplatev2.R
import com.example.composesokobantemplatev2.game.Level
import androidx.core.graphics.createBitmap

fun previewLevelBitmap(context: Context, level: Level): Bitmap {
    val tile = 32 // velkost policka v preview
    val bmp = createBitmap(level.width * tile, level.height * tile)
    val canvas = Canvas(bmp)

    fun load(@DrawableRes id: Int) =
        BitmapFactory.decodeResource(context.resources, id) // id obrazka nacita ako BitMap

    val img = mapOf(
        0 to load(R.drawable.empty),
        1 to load(R.drawable.wall),
        2 to load(R.drawable.box),
        3 to load(R.drawable.goal),
        4 to load(R.drawable.hero),
        5 to load(R.drawable.boxok),
        6 to load(R.drawable.hero)
    )

    level.tiles.forEachIndexed { index, value ->
        val x = (index % level.width) * tile
        val y = (index / level.width) * tile

        val bmpTile = img[value] ?: img[0]!!
        val dst = Rect(x, y, x + tile, y + tile)

        canvas.drawBitmap(bmpTile, null, dst, null)
    }

    return bmp
}
