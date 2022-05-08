#version 330

uniform vec2 position;  //Uniform set before render call
uniform float radius;

out float geomRadius;

void main() {
    gl_Position=vec4(position,0,1); //Passes values down the pipeline
    geomRadius=radius;
}
