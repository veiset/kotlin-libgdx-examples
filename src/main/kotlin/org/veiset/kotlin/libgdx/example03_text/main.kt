package org.veiset.kotlin.libgdx.example03_text

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
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
    LwjglApplication(TextApplication(), config)
}

class TextApplication : KtxApplicationAdapter {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var font: BitmapFont

    override fun create() {
        val fontBase = FreeTypeFontGenerator(Gdx.files.internal("font/Retron2000.ttf"))

        spriteBatch = SpriteBatch()
        font = fontBase.generateFont(FreeTypeFontParameter().apply {
            size = 64
        })
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        spriteBatch.use {
            font.draw(it, "Hello LibGDX!", 300f, 300f)
        }

    }
}
