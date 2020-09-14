package org.veiset.kotlin.libgdx.example04_movement

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
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
    LwjglApplication(MovementApplication(), config)
}

class MovementApplication: KtxApplicationAdapter {

    private lateinit var shapeRenderer: ShapeRenderer
    private val position = Vector2(500f, 500f)

    override fun create() {
        shapeRenderer = ShapeRenderer()
    }

    override fun render() {
        update()
        draw()
    }

    private fun update() {
        position.x += 2
    }

    private fun draw() {
        clearScreen(0f, 0f, 0f, 1f)
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.RED
            it.rect(position.x, position.y, 100f, 100f)
        }
    }
}
