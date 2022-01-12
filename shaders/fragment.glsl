#version 330

in vec2 centre;
in float fragRadius;

uniform vec3 colour;
uniform vec2 resolution;

out vec4 FragColour;

float square(float a){
    return a*a;
}

void main() {
    vec2 circleCentre=(centre+1)/2;
    vec2 st=gl_FragCoord.xy/resolution;
    vec2 dPos=st-circleCentre;

    float distSqr=square(dPos.x)+square(dPos.y);
    float radSqr=square(fragRadius/2);

    if(radSqr<distSqr){
        discard;
    }else{
        FragColour=vec4(colour,1);
    }
}
