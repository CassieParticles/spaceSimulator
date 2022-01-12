#version 330

layout(points) in;
layout(triangle_strip,max_vertices=4) out;

in float geomRadius[];

uniform mat4 viewMatrix;

out vec2 centre;
out float fragRadius;

void buildSquare(vec4 position, float sideLength){
    gl_Position=viewMatrix*(position+vec4(-sideLength,-sideLength,0,0));
    EmitVertex();
    gl_Position=viewMatrix*(position+vec4(sideLength,-sideLength,0,0));
    EmitVertex();
    gl_Position=viewMatrix*(position+vec4(-sideLength,sideLength,0,0));
    EmitVertex();
    gl_Position=viewMatrix*(position+vec4(sideLength,sideLength,0,0));
    EmitVertex();
    EndPrimitive();
}

void main() {
    centre=(viewMatrix*gl_in[0].gl_Position).xy;
    fragRadius=geomRadius[0]*viewMatrix[0][0];

    buildSquare(gl_in[0].gl_Position,geomRadius[0]);
}
