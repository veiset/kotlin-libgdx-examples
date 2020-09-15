#version 120
#ifdef GL_ES
precision highp float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_time;


void main()
{
    vec4 col = texture2D(u_texture, v_texCoords);
    gl_FragColor = col;
}