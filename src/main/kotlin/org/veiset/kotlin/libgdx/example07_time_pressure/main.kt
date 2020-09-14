package org.veiset.kotlin.libgdx.example07_time_pressure

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use
import org.veiset.kotlin.libgdx.FPS
import org.veiset.kotlin.libgdx.HEIGHT
import org.veiset.kotlin.libgdx.WIDTH

fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
    }
    LwjglApplication(TiminingApplication(), config)
}

fun randomizeSequence(size: Int, vararg letters: String): List<String> =
    (0..size).map { letters.random() }

class TiminingApplication : KtxApplicationAdapter {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var character: Texture
    private lateinit var font: BitmapFont

    private val timeLimit = 10f // in seconds
    private var timer = timeLimit
    private var correct = 0
    private val fishSequence = randomizeSequence(size = 10, "A", "S", "D", "W")

    fun isCorrectGuess(key: String) {
        if (correct < fishSequence.size) {
            val nextKey = fishSequence[correct]
            if (nextKey == key) correct += 1 else timer -= 0.5f
        }
    }

    override fun create() {
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        character = Texture(Gdx.files.internal("textures/character.png"))

        val fontBase = FreeTypeFontGenerator(Gdx.files.internal("font/Retron2000.ttf"))

        spriteBatch = SpriteBatch()
        font = fontBase.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = 64
        })
    }

    override fun render() {
        update(Gdx.graphics.deltaTime)
        draw(Gdx.graphics.deltaTime)
    }

    private fun update(delta: Float) {
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.A) -> isCorrectGuess("A")
            Gdx.input.isKeyJustPressed(Input.Keys.S) -> isCorrectGuess("S")
            Gdx.input.isKeyJustPressed(Input.Keys.D) -> isCorrectGuess("D")
            Gdx.input.isKeyJustPressed(Input.Keys.W) -> isCorrectGuess("W")
        }

        if (!(timer > 0 && correct == fishSequence.size)) {
            timer -= 2 * delta
        }
    }

    private fun draw(delta: Float) {
        clearScreen(0f, 0f, 0f, 1f)

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.rect(20f, 100f, timer * (WIDTH / timeLimit), 40f)
        }

        spriteBatch.use {
            font.color = Color.WHITE
            fishSequence.forEachIndexed { index, letter ->
                font.draw(spriteBatch, letter, 400f + 80 * index, 600f)
            }
            val correctString = fishSequence.take(correct)
            font.color = Color.RED
            correctString.forEachIndexed { index, c ->
                font.draw(spriteBatch, c, 400f + 80 * index, 600f)
            }

            if (correct == fishSequence.size) {
                font.color = Color.GREEN
                font.draw(spriteBatch, "YOU WON!", 300f, 300f)
            }

            if (timer < 0) {
                font.draw(spriteBatch, ":((", 300f, 300f)
            }
        }
    }
}
