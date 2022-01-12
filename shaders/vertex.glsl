#version 330

layout(location=0) in float vertRadius;

uniform vec2 position;

out float geomRadius;

void main() {
    gl_Position=vec4(position,0,1);
    geomRadius=vertRadius;
}
