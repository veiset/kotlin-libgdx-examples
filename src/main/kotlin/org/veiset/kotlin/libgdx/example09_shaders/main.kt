package org.veiset.kotlin.libgdx.example09_shaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import ktx.app.clearScreen
import org.veiset.kotlin.libgdx.FPS
import org.veiset.kotlin.libgdx.HEIGHT
import org.veiset.kotlin.libgdx.WIDTH
import org.veiset.kotlin.libgdx.example08_modules.GameModule
import org.veiset.kotlin.libgdx.example08_modules.ModuleRunner
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private var time = 0f

fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
        samples = 4
    }
    LwjglApplication(ModuleRunner { WaterModule() }, config)
}

class WaterModule : GameModule {
    private val spriteBatch = SpriteBatch()
    private val water0000 = Texture(Gdx.files.internal("textures/water/wavepattern3_0000.png"))
    private val water0001 = Texture(Gdx.files.internal("textures/water/wavepattern2_0001.png"))
    private val water0002 = Texture(Gdx.files.internal("textures/water/wavepattern2_0002.png"))
    private val boatTexture = Texture(Gdx.files.internal("textures/character.png"))
    private val boat = Sprite(boatTexture)

    private val waveShader = ShaderProgram(
        Gdx.files.internal("shaders/waves/wave.vert"),
        Gdx.files.internal("shaders/waves/wave.frag")
    )
    private val rippleShader = ShaderProgram(
        Gdx.files.internal("shaders/ripple/ripple.vert"),
        Gdx.files.internal("shaders/ripple/ripple.frag")
    )

    val size = 3048f
    val offset = -500f
    var speed = 1f

    private val player = Player(Vector2(310f, 400f), 0f, 0f, boat, spriteBatch, rippleShader)

    init {
        Gdx.graphics.gL20.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        boat.setOriginCenter()

        ShaderProgram.pedantic = false
        println("Loading shaders...")
        if (!waveShader.isCompiled) println(waveShader.log) else println("Wave Shader loaded.")
        if (!rippleShader.isCompiled) println(rippleShader.log) else println("Ripple Shader loaded.")
    }

    override fun update(delta: Float) {
        time += delta
        player.update(delta)
    }

    override fun draw(delta: Float) {
        clearScreen(27f / 255, 106f / 255, 152f / 255, 1f)

        spriteBatch.shader = waveShader
        spriteBatch.begin()
        waveShader.setVars(
            "useCustomColor" to false,
            "rippleTimeValue" to 0.004f * speed, "opacity" to 0.9f, "rippleFreq" to 105f, "rippleAmpl" to 25f
        )
        spriteBatch.draw(water0002, offset, offset, size, size)
        spriteBatch.end()

        spriteBatch.begin()
        waveShader.setVars(
            "useCustomColor" to true, "color" to Color(0.3f, 0.6f, 0.6f, 1f),
            "rippleTimeValue" to 0.01f * speed, "opacity" to 0.4f, "rippleFreq" to 55f, "rippleAmpl" to 81f
        )
        spriteBatch.draw(water0001, offset, offset, size, size)
        spriteBatch.end()

        spriteBatch.begin()
        waveShader.setVars(
            "useCustomColor" to true, "color" to Color(0.4f, 0.8f, 0.9f, 1f),
            "rippleTimeValue" to 0.005f * speed, "opacity" to 1f, "rippleFreq" to 85f, "rippleAmpl" to 40f
        )
        spriteBatch.draw(water0000, offset, offset, size, size)
        spriteBatch.end()

        player.draw(delta)

        spriteBatch.shader = null
    }

    override fun nextState(): GameModule = this

}

data class Player(
    val pos: Vector2,
    var speed: Float,
    var direction: Float,
    val sprite: Sprite,
    val spriteBatch: SpriteBatch,
    val shaderProgram: ShaderProgram,
) {
    private val maxSpeed: Float = 15f
    private val maxReverseSpeed: Float = 1f
    private val accSpeed: Float = 1.2f
    private val deaccSpeed: Float = 5f
    private val turnSpeed: Float = 180f
    private val drawSize = Vector2(128f, 128f)

    fun update(delta: Float) {
        val turningSpeedMovingRatio = 1f / (1f + abs(speed))

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction += turnSpeed * turningSpeedMovingRatio * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction -= turnSpeed * turningSpeedMovingRatio * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            speed = min(speed + (accSpeed * delta), maxSpeed)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            speed = max(speed - (deaccSpeed * delta), maxReverseSpeed * -1)
        }

        val v = Vector2(cos(direction / 180f * PI.toFloat()), sin(direction / 180f * PI.toFloat()))
        pos.x += v.x * speed
        pos.y += v.y * speed

        if (pos.x > 1920f && (v.x * speed) > 0) pos.x = -drawSize.x
        if (pos.x < -drawSize.x && (v.x * speed) < 0) pos.x = 1920f
        if (pos.y > 1028f && (v.y * speed) > 0) pos.y = -drawSize.y
        if (pos.y < -drawSize.y && (v.y * speed) < 0) pos.y = 1080f

        if (direction > 360f) direction = 0f
        if (direction < 0) direction = 360f
        if (direction < 90f || direction > 270f) {
            sprite.setFlip(false, false)
        } else {
            sprite.setFlip(false, true)
        }
    }

    fun draw(delta: Float) {
        spriteBatch.shader = null
        spriteBatch.shader = shaderProgram
        spriteBatch.begin()
        shaderProgram.setVars("speed" to speed)
        spriteBatch.draw(sprite, pos.x, pos.y, drawSize.x / 2f, drawSize.y / 2f, drawSize.x, drawSize.y, 1f, 1f, direction)
        spriteBatch.end()
    }
}

fun ShaderProgram.setVars(vararg args: Pair<String, Any>) {
    this.setUniformf("u_time", time)
    this.setUniformf("u_resolution", Vector2(1920f, 1080f))
    this.setUniformf("u_mouse", Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))
    args.forEach { (paramName, value) ->
        when (value) {
            is Boolean -> this.setUniformi(paramName, if (value) 1 else 0)
            is Float -> this.setUniformf(paramName, value)
            is Color -> this.setUniformf(paramName, value)
            else -> throw Exception("Type of $value not mapped yet")
        }
    }
}
