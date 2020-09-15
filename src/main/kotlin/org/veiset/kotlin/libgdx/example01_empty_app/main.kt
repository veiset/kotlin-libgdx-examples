package org.veiset.kotlin.libgdx.example01_empty_app

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import org.veiset.kotlin.libgdx.FPS
import org.veiset.kotlin.libgdx.HEIGHT
import org.veiset.kotlin.libgdx.WIDTH

fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
    }
    LwjglApplication(EmptyApplication(), config)
}

class EmptyApplication: KtxApplicationAdapter {
    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
    }
}
