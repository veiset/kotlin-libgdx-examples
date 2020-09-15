#version 120
attribute vec2 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform float u_time;
uniform float speed;

varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
    v_color = a_color;
    v_texCoords = a_texCoord0;

    vec2 offset;
    offset.x = cos(u_time*0.4)*3.0*1.4;
    offset.y = sin(u_time*3)*10.0*1.4;

    gl_Position = u_projTrans * vec4(a_position + offset, 0.0, 1.0);
}