#version 120
#ifdef GL_ES
precision highp float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_time;

uniform float rippleTimeValue = 0.05f;
uniform float rippleFreq = 20.0;
uniform float rippleAmpl = 8.0;
uniform float opacity = 1.0;


void main()
{
    vec2 sinOffsetCoord = v_texCoords;
    vec3 color = v_color.rgb;

    vec2 texSize = vec2(0.01, 0.01);
    sinOffsetCoord.y += sin((v_texCoords.x + u_time*rippleTimeValue) * rippleFreq)*rippleAmpl/texSize.y;
    sinOffsetCoord.x += cos((v_texCoords.y + u_time*rippleTimeValue) * rippleFreq)*rippleAmpl/texSize.x;
    vec2 offset;
    offset.x = cos(u_time)*0.001;
    offset.y = sin(u_time)*0.001;

    gl_FragColor = vec4(color, opacity) * texture2D(u_texture, sinOffsetCoord + offset);
}