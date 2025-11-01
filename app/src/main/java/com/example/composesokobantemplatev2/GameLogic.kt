package com.example.composesokobantemplatev2.game

fun moveDir(
    level: MutableList<Int>,
    playerPos: Pair<Int, Int>,
    dir: Pair<Int, Int>,
    width: Int,
    height: Int,
    update: (MutableList<Int>, Pair<Int, Int>) -> Unit
) {
    val (px, py) = playerPos
    val (mx, my) = dir

    val nx = px + mx
    val ny = py + my

    if (nx !in 0 until width || ny !in 0 until height) return

    val nextIdx = ny * width + nx
    val newLevel = level.toMutableList()

    when (newLevel[nextIdx]) {
        0, 3 -> {
            movePlayer(newLevel, playerPos, nx, ny, width)
            update(newLevel, nx to ny)
        }
        2, 5 -> {
            val bx = nx + mx
            val by = ny + my
            if (bx in 0 until width && by in 0 until height) {
                val boxNext = newLevel[by * width + bx]
                if (boxNext == 0 || boxNext == 3) {
                    moveBox(newLevel, nx, ny, bx, by, width)
                    movePlayer(newLevel, playerPos, nx, ny, width)
                    update(newLevel, nx to ny)
                }
            }
        }
    }
}

fun movePlayer(level: MutableList<Int>, oldPos: Pair<Int, Int>, nx: Int, ny: Int, width: Int) {
    val (px, py) = oldPos
    val oldIdx = py * width + px
    val newIdx = ny * width + nx

    level[oldIdx] = if (level[oldIdx] == 6) 3 else 0
    level[newIdx] = if (level[newIdx] == 3) 6 else 4
}



fun moveBox(level: MutableList<Int>, px: Int, py: Int, nx: Int, ny: Int, width: Int) {
    val oldIdx = py * width + px
    val newIdx = ny * width + nx

    val oldTile = level[oldIdx]
    val newTile = level[newIdx]

    level[oldIdx] = if (oldTile == 5) 3 else 0
    level[newIdx] = if (newTile == 3) 5 else 2
}

fun findPlayer(level: List<Int>, width: Int): Pair<Int, Int> {
    val idx = level.indexOfFirst { it == 4 || it == 6 }
    return if (idx == -1) 0 to 0 else idx % width to idx / width
}