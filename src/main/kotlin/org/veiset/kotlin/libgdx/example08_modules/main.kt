package org.veiset.kotlin.libgdx.example08_modules

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.*
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use
import org.veiset.kotlin.libgdx.FPS
import org.veiset.kotlin.libgdx.HEIGHT
import org.veiset.kotlin.libgdx.WIDTH

interface GameModule {
    fun update(delta: Float)
    fun draw(delta: Float)
    fun nextState(): GameModule
}

class ModuleRunner(var initModule: () -> GameModule): KtxApplicationAdapter {
    private lateinit var currentModule: GameModule

    override fun create() {
        println("Starting up...")
        println("Running OpenGL " + Gdx.app.graphics.glVersion.majorVersion + "." + Gdx.app.graphics.glVersion.minorVersion);
        currentModule = initModule()
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        currentModule = currentModule.nextState()
        currentModule.update(Gdx.graphics.deltaTime)
        currentModule.draw(Gdx.graphics.deltaTime)
    }
}

fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
    }
    LwjglApplication(ModuleRunner { ModuleOne() }, config)
}

class ModuleOne: GameModule {
    private val shapeRenderer = ShapeRenderer()

    override fun update(delta: Float) { }

    override fun draw(delta: Float) {
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.RED
            it.rect(100f, 100f, 100f, 100f)
        }
    }

    override fun nextState(): GameModule =
        if (input.isKeyJustPressed(Keys.NUM_2)) ModuleTwo(Color.BLUE) else this
}

class ModuleTwo(private val color: Color): GameModule {
    private val shapeRenderer = ShapeRenderer()

    override fun update(delta: Float) { }

    override fun draw(delta: Float) {
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = color
            it.rect(100f, 300f, 150f, 100f)
        }
    }

    override fun nextState(): GameModule =
        if (input.isKeyJustPressed(Keys.NUM_1)) ModuleOne() else this

}
