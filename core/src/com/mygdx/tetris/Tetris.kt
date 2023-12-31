package com.mygdx.tetris

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils

class Tetris : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var gameMatrix: MutableList<MutableList<Int>> = MutableList(21) {
        MutableList(10) {
            0
        }
    }
    val leftOffset: Float = 200f
    val facings: List<List<List<List<Int>>>> = listOf(
        listOf(
            listOf(
                listOf(0, 1, 1),
                listOf(0, 1, 1),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 1, 1),
                listOf(0, 1, 1),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 1, 1),
                listOf(0, 1, 1),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 1, 1),
                listOf(0, 1, 1),
                listOf(0, 0, 0)
            )
        ),
        listOf(
            listOf(
                listOf(0, 0, 0, 0),
                listOf(2, 2, 2, 2),
                listOf(0, 0, 0, 0),
                listOf(0, 0, 0, 0)
            ),
            listOf(
                listOf(0, 2, 0, 0),
                listOf(0, 2, 0, 0),
                listOf(0, 2, 0, 0),
                listOf(0, 2, 0, 0)
            ),
            listOf(
                listOf(0, 0, 0, 0),
                listOf(0, 0, 0, 0),
                listOf(2, 2, 2, 2),
                listOf(0, 0, 0, 0)
            ),
            listOf(
                listOf(0, 0, 2, 0),
                listOf(0, 0, 2, 0),
                listOf(0, 0, 2, 0),
                listOf(0, 0, 2, 0)
            )
        ),
        listOf(
            listOf(
                listOf(0, 3, 0),
                listOf(3, 3, 3),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 3, 0),
                listOf(0, 3, 3),
                listOf(0, 3, 0)
            ),
            listOf(
                listOf(0, 0, 0),
                listOf(3, 3, 3),
                listOf(0, 3, 0)
            ),
            listOf(
                listOf(0, 3, 0),
                listOf(3, 3, 0),
                listOf(0, 3, 0)
            )
        ),
        listOf(
            listOf(
                listOf(0, 0, 4),
                listOf(4, 4, 4),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 4, 0),
                listOf(0, 4, 0),
                listOf(0, 4, 4)
            ),
            listOf(
                listOf(0, 0, 0),
                listOf(4, 4, 4),
                listOf(4, 0, 0)
            ),
            listOf(
                listOf(4, 4, 0),
                listOf(0, 4, 0),
                listOf(0, 4, 0)
            )
        ),
        listOf(
            listOf(
                listOf(5, 0, 0),
                listOf(5, 5, 5),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 5, 5),
                listOf(0, 5, 0),
                listOf(0, 5, 0)
            ),
            listOf(
                listOf(0, 0, 0),
                listOf(5, 5, 5),
                listOf(0, 0, 5)
            ),
            listOf(
                listOf(0, 5, 0),
                listOf(0, 5, 0),
                listOf(5, 5, 0)
            )
        ),
        listOf(
            listOf(
                listOf(0, 6, 6),
                listOf(6, 6, 0),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 6, 0),
                listOf(0, 6, 6),
                listOf(0, 0, 6)
            ),
            listOf(
                listOf(0, 0, 0),
                listOf(0, 6, 6),
                listOf(6, 6, 0)
            ),
            listOf(
                listOf(6, 0, 0),
                listOf(6, 6, 0),
                listOf(0, 6, 0)
            )
        ),
        listOf(
            listOf(
                listOf(7, 7, 0),
                listOf(0, 7, 7),
                listOf(0, 0, 0)
            ),
            listOf(
                listOf(0, 0, 7),
                listOf(0, 7, 7),
                listOf(0, 7, 0)
            ),
            listOf(
                listOf(0, 0, 0),
                listOf(7, 7, 0),
                listOf(0, 7, 7)
            ),
            listOf(
                listOf(0, 7, 0),
                listOf(7, 7, 0),
                listOf(7, 0, 0)
            )
        )
    )

    private lateinit var shape: ShapeRenderer
    private var bag: List<Int> = (0..6).toList().shuffled()
    private var bagIndex: Int = 0
    private var tetrominoX: Int = 0
    private var tetrominoY: Int = 17
    private var tetrominoType: Int = bag[bagIndex]
    private var tetrominoRot: Int = 0
    private var level: Int = 0
    private val dropSpeeds: List<Int> = listOf(60, 48, 37, 28, 21, 16)
    private var dropTimer: Int = 0
    private var score: Int = 0
    private lateinit var font: BitmapFont
    override fun create() {
        batch = SpriteBatch()
        shape = ShapeRenderer()
        font = BitmapFont()
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        dropTimer++
        if(Gdx.input.isKeyJustPressed(Keys.Z)) {
            if (tetrominoRot == 0) {
                tetrominoRot = 3
            } else {
                tetrominoRot--
            }
            if(!isValidMove(facings[tetrominoType][tetrominoRot], tetrominoY, tetrominoX)) {
                if(tetrominoRot == 3) {
                    tetrominoRot = 0
                } else {
                    tetrominoRot++
                }
            }
        }
        if(Gdx.input.isKeyJustPressed(Keys.X)) {
            if(tetrominoRot == 3) {
                tetrominoRot = 0
            } else {
                tetrominoRot++
            }
            if(!isValidMove(facings[tetrominoType][tetrominoRot], tetrominoY, tetrominoX)) {
                if (tetrominoRot == 0) {
                    tetrominoRot = 3
                } else {
                    tetrominoRot--
                }
            }
        }
        if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            tetrominoX--
            if(!isValidMove(facings[tetrominoType][tetrominoRot], tetrominoY, tetrominoX))
                tetrominoX++
        }
        if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
             tetrominoX++
            if(!isValidMove(facings[tetrominoType][tetrominoRot], tetrominoY, tetrominoX))
                tetrominoX--
        }
        if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            tetrominoY--
            if(!isValidMove(facings[tetrominoType][tetrominoRot], tetrominoY, tetrominoX)) {
                tetrominoY++
                placeTetromino()
                tetrominoY = 17
            }
        }
        if(dropTimer == dropSpeeds[level]) {
            tetrominoY--
            if(!isValidMove(facings[tetrominoType][tetrominoRot], tetrominoY, tetrominoX)) {
                tetrominoY++
                placeTetromino()
                tetrominoY = 17
            }
            dropTimer = 0
        }
        for((i: Int, line: List<Int>) in gameMatrix.withIndex()) {
            for((j: Int, gridSquare: Int) in line.withIndex()) {
                if(i != 20) {
                    shape.begin(ShapeRenderer.ShapeType.Line)
                    shape.color = Color.WHITE
                    shape.rect(j * 24.0F + 200.0F, i * 24.0F, 24.0F, 24.0F)
                    shape.end()
                    renderTetromino(j, i)
                    if (gridSquare != 0) {
                        val colors: List<Color> = listOf(
                            Color.YELLOW,
                            Color.SKY,
                            Color.PURPLE,
                            Color.ORANGE,
                            Color.BLUE,
                            Color.GREEN,
                            Color.RED
                        )
                        val color: Color = colors[gridSquare - 1]
                        shape.begin(ShapeRenderer.ShapeType.Filled)
                        shape.color = color
                        shape.rect(
                            j * 24.0F + leftOffset, i * 24.0F + 1f, 24.0F,
                            23.0F
                        )
                        shape.end()
                    }
                }
            }
        }
        batch!!.begin()
        font.draw(batch, "Score:", 10f, 480f)
        font.draw(batch, score.toString().padStart(10, '0'), 10f, 468f)
        batch!!.end()
    }
    fun renderTetromino(x: Int, y: Int) {
        if(x >= tetrominoX && ((tetrominoType == 1 && x <= tetrominoX + 3) ||
                x <= tetrominoX + 2) &&
            y >= tetrominoY && ((tetrominoType == 1 && y <= tetrominoY + 3) ||
                    y <= tetrominoY + 2)) {
            val colorNum: Int =
                facings[tetrominoType][tetrominoRot][y - tetrominoY][x - tetrominoX]
            if(colorNum != 0) {
                val colors: List<Color> = listOf(
                    Color.YELLOW,
                    Color.SKY,
                    Color.PURPLE,
                    Color.ORANGE,
                    Color.BLUE,
                    Color.GREEN,
                    Color.RED
                )
                val color: Color = colors[colorNum - 1]
                shape.begin(ShapeRenderer.ShapeType.Filled)
                shape.color = color
                shape.rect(x * 24.0F + 200.0F, y * 24.0F + 1f, 24.0F, 23.0F)
                shape.end()
            }
        }
    }
    fun placeTetromino() {
        for((y: Int, row: List<Int>) in facings[tetrominoType][tetrominoRot].withIndex()) {
            for((x: Int, square: Int) in row.withIndex()) {
                if(x + tetrominoX in 0..9 && y + tetrominoY in 0..19) {
                    println("ok")
                    if(facings[tetrominoType][tetrominoRot][y][x] != 0) {
                        println("ok")
                        gameMatrix[y + tetrominoY][x + tetrominoX] = square
                    }
                }

            }
        }
        var clearLines: MutableList<Int> = mutableListOf()
        for(i in 0 until gameMatrix.size) {
            if(isLineClear(i)) clearLines.add(i)
        }
        var clearedLines: Int = 0
        while(clearLines.size > 0) {
            gameMatrix.removeAt(clearLines[0])
            gameMatrix.add(MutableList(10) {
                0
            })
            clearedLines++
            clearLines = mutableListOf()
            for(i in 0 until gameMatrix.size) {
                if(isLineClear(i)) clearLines.add(i)
            }
        }
        when(clearedLines) {
            0 -> {}
            1 -> score += 100 * (level + 1)
            2 -> score += 300 * (level + 1)
            3 -> score += 500 * (level + 1)
            else -> score += 800 * (level + 1)
        }
        bagIndex++
        if(bagIndex > 6) {
            bagIndex = 0
            bag = (0..6).toList().shuffled()
        }
        tetrominoType = bag[bagIndex]
        tetrominoX = 0
        tetrominoY = 17
        tetrominoRot = 0
    }
    fun isValidMove(matrix: List<List<Int>>, cellRow: Int, cellCol: Int): Boolean {
        for((i: Int, row: List<Int>) in matrix.withIndex()) {
            for((col: Int, cell: Int) in row.withIndex()) {
                if(cell != 0 && (cellCol + col < 0 || cellCol + col >= gameMatrix[0].size ||
                            cellRow + i <= -1 || cellRow + i >= gameMatrix.size
                            || gameMatrix[cellRow + i][cellCol + col] != 0)) return false
            }
        }
        return true
    }
    fun isLineClear(line: Int): Boolean {
        for(square: Int in gameMatrix[line]) {
            if(square == 0) return false
        }
        return true
    }

    override fun dispose() {
        batch!!.dispose()
        shape.dispose()
    }
}
