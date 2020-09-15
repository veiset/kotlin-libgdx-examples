#version 120
attribute vec2 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoords;

uniform vec4 color;
uniform bool useCustomColor = false;

void main()
{
    if (useCustomColor) {
        v_color = color;
    } else {
        v_color = a_color;
    }
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * vec4(a_position, 0.0, 1.0);
}