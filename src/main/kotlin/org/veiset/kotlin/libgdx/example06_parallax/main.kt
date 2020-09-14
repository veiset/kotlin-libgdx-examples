import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.*
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
    LwjglApplication(ParallaxApplication(), config)
}

data class ParallaxLayer(val texture: Texture, val speed: Float, val position: Vector2, val heightScale: Float) {

    fun update() {
        position.x += speed
        if (position.x > WIDTH) {
            position.x = 0f
        }
    }

    fun draw(spriteBatch: SpriteBatch) {
        spriteBatch.draw(texture, position.x - WIDTH, position.y, WIDTH.toFloat(), HEIGHT/heightScale)
        spriteBatch.draw(texture, position.x, position.y, WIDTH.toFloat(), HEIGHT/heightScale)
    }
}


class ParallaxApplication : KtxApplicationAdapter {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var bg01: Texture
    private lateinit var bg02: Texture
    private lateinit var bg03: Texture
    private lateinit var layers: List<ParallaxLayer>

    private val backgroundColor = Color.valueOf("ebe3d4")
    private val skyColor = Color.valueOf("ffffff")

    override fun create() {
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()

        bg01 = Texture(Gdx.files.internal("textures/parallax/bg_01.png"))
        bg02 = Texture(Gdx.files.internal("textures/parallax/bg_02.png"))
        bg03 = Texture(Gdx.files.internal("textures/parallax/bg_03.png"))

        layers = listOf(
            ParallaxLayer(bg03, speed = 0.5f, position = Vector2(0f, 240f), heightScale = 2.5f),
            ParallaxLayer(bg02, speed = 1f, position = Vector2(0f, 230f), heightScale = 2.5f),
            ParallaxLayer(bg01, speed = 1.4f, position = Vector2(0f, 200f), heightScale = 2.0f)
        )
    }

    override fun render() {
        update()
        draw()
    }

    private fun update() {
        layers.forEach(ParallaxLayer::update)
    }

    private fun draw() {
        clearScreen(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1f)

        shapeRenderer.use(ShapeType.Filled) {
            it.color = skyColor
            it.rect(0f, HEIGHT / 3f, WIDTH.toFloat(), HEIGHT.toFloat())
        }

        spriteBatch.use { spriteBatch ->
            layers.forEach { it.draw(spriteBatch) }
        }
    }
}
