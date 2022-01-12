package sections.RunningSimulator;

import gameObjects.Camera;
import gameObjects.Planet;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL46;
import rendering.Program;
import rendering.Shader;
import utils.FileHandling;
import utils.Input;

import java.util.ArrayList;

public class RunSimMain {
    /*
        Input:
            -Planets simulated
            -Gravity constant

        Methods:
            -Update
            -Render
     */

    Program program;
    Physics physics;

    public RunSimMain(){

    }

    private ArrayList<Planet> planets;
    private float gravityConstant;

    public void init(ArrayList<Planet> planets, float gravityConstant) throws Exception {
        this.planets=planets;
        this.gravityConstant=gravityConstant;
        physics=new Physics();
        program=new Program();

        program.attachShaders(new Shader[]{
                new Shader(FileHandling.loadResource("src/shaders/vertex.glsl"), GL46.GL_VERTEX_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/geometry.glsl"),GL46.GL_GEOMETRY_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/fragment.glsl"),GL46.GL_FRAGMENT_SHADER)});

        program.link();

        program.createUniform("position");
        program.createUniform("viewMatrix");
        program.createUniform("resolution");
        program.createUniform("colour");
    }

    public void update(float updateRate, Camera camera, Input input){
        physics.runPhysics(updateRate,gravityConstant,planets);
        camera.control(input,3,1, updateRate);
    }

    public void render(Vector2f resolution, Camera camera){
        program.useProgram();
        program.setUniform("resolution", resolution);
        program.setUniform("viewMatrix",camera.getViewMatrix());

        for(Planet planet:planets){
            planet.render(program,camera);
        }
    }

    public void cleanup(){
        if(planets!=null){
            for(Planet planet:planets){
                planet.cleanup();
            }
            planets=null;
        }
        program.cleanup();
    }
}
