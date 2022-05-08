#version 330

layout(points) in;  //Specify the input/output geometry type
layout(triangle_strip,max_vertices=4) out;

in float geomRadius[];  //Since geometries may have multiple vertices, values passed to the geometry shader must be an array

uniform mat4 viewMatrix;    //Currently identity matrix, will be used by camera to move around the screen

out vec2 centre;
out float fragRadius;

void buildSquare(vec4 position, float sideLength){  //Creates the triangle strip with the 4 vertices to make a square
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
    centre=(viewMatrix*gl_in[0].gl_Position).xy;    //Send values down the pipeline
    fragRadius=geomRadius[0]*viewMatrix[0][0];  //Transforms radius into screen space, used if camera were to zoom in

    buildSquare(gl_in[0].gl_Position,geomRadius[0]);    //Construct square
}
