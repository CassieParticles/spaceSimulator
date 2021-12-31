#version 330

layout(location=0) in vec2 vertexPosition;    //Vertex data set by the mesh class

uniform vec2 position;
uniform vec2 cameraPosition;

uniform float zoom;

out vec2 centrePos;

void main() {
    gl_Position=vec4(vertexPosition+position,0,1);
    centrePos=position;
}
