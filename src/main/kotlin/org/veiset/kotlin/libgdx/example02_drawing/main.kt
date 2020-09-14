package org.veiset.kotlin.libgdx.example02_drawing

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
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
    LwjglApplication(DrawApplication(), config)
}

class DrawApplication: KtxApplicationAdapter {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var character: Texture

    override fun create() {
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        character = Texture(Gdx.files.internal("textures/character.png"))
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        spriteBatch.use {
            it.draw(character, 500f, 300f, 256f, 256f)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.RED
            it.rect(450f, 300f, 100f, 50f)
            it.color = Color.BLUE
            it.circle(750f, 400f, 50f)
        }
    }
}
