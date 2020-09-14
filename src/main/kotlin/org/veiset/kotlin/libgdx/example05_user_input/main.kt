package org.veiset.kotlin.libgdx.example05_user_input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
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
    LwjglApplication(UserInputApplication(), config)
}

class UserInputApplication: KtxApplicationAdapter {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var character: Texture

    private val characterPosition = Vector2(500f, 300f)
    private val characterSize = Vector2(256f, 256f)
    private var time = 0f

    override fun create() {
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        character = Texture(Gdx.files.internal("textures/character.png"))
    }

    override fun render() {
        update()
        draw()
    }

    private fun update() {
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            characterPosition.x += 2
        }
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            characterPosition.x -= 2
        }
        printCursorPositionEverySecond()
    }

    private fun draw() {
        clearScreen(0f, 0f, 0f, 1f)
        spriteBatch.use {
            spriteBatch.draw(character, characterPosition.x, characterPosition.y, characterSize.x, characterSize.y)
        }
    }

    private fun printCursorPositionEverySecond() {
        // output location every second
        val oldTime = time
        time += Gdx.graphics.deltaTime
        if (time.toInt() != oldTime.toInt()) {
            println("Cursor at position: ${Gdx.input.x}, ${Gdx.input.y}")
        }
    }
}
