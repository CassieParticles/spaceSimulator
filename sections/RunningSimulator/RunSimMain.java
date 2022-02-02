package sections.RunningSimulator;

import gameObjects.Camera;
import gameObjects.Planet;
import guiObjects.GUI;
import guiObjects.TextGUI;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import rendering.Program;
import rendering.Shader;
import utils.FileHandling;
import utils.Input;
import utils.Timer;

import java.util.ArrayList;

public class RunSimMain {

    Program program;
    Physics physics;
    Timer timer;

    Planet planetSelected;
    GUI GUISelected;

    private float timeStep=1;

    public RunSimMain(){

    }

    private ArrayList<Planet> planets;
    private ArrayList<GUI> GUIElements;
    private float gravityConstant;

    public void init(ArrayList<Planet> planets,ArrayList<GUI> GUIElements, float gravityConstant,Timer timer) throws Exception {   //Initialise everything in the planet simulator, ready to be used
        this.planets=planets;   //Initialising variables
        this.GUIElements=GUIElements;
        this.gravityConstant=gravityConstant;
        this.timer=timer;
        physics=new Physics();
        program=new Program();

        program.attachShaders(new Shader[]{ //Create the shader used to render the planets
                new Shader(FileHandling.loadResource("src/shaders/vertex.glsl"), GL46.GL_VERTEX_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/geometry.glsl"),GL46.GL_GEOMETRY_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/fragment.glsl"),GL46.GL_FRAGMENT_SHADER)});

        program.link(); //Compiling the shader

        program.createUniform("position");
        program.createUniform("viewMatrix");
        program.createUniform("resolution");
        program.createUniform("colour");
    }

    public void changeTimeStep(float timeStep){ //Change the timestep for the physics simulation, and change the update rate to compensate
        this.timeStep=timeStep;
        if(timeStep!=0){
            timer.setUPS(60/timeStep);
        }else{
            timer.setUPS(60);
        }
    }

    private float square(float a){  //Squares a float
        return a*a;
    }

    private Planet getPlanetSelected(Input input,Camera camera, Vector2f resolution){   //Returns which planet the mouse is over, or null if the mouse isn't over a planet
        int[] mouseScreenPos=input.getMousePos();
        Vector2f mouseClipPos=new Vector2f(mouseScreenPos[0],mouseScreenPos[1]).div(new Vector2f(resolution).div(new Vector2f(2,-2))).sub(new Vector2f(1,-1));  //Normalises mouse screen position between -1 and 1
        Vector4f mouseWorldPos=new Vector4f(mouseClipPos,0,1).mul(new Matrix4f(camera.getViewMatrix()).invert());    //needs to be vector4f to allow matrix multiplication
        for(Planet planet:planets){
            if(square(mouseWorldPos.x-planet.getPosition().x)+square(mouseWorldPos.y-planet.getPosition().y)<square(planet.getRadius())){
                return planet;
            }
        }
        return null;
    }

    public void update(float updateRate, Camera camera, Input input, Vector2f resolution){  //Update physics simulation and manage inputs
        if(input.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            changeTimeStep(1-timeStep);
        }
        physics.runPhysics(updateRate*timeStep,gravityConstant,planets);
        camera.control(input,3,1, 1/60f);
        if(input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)){
            planetSelected=getPlanetSelected(input,camera,resolution);
            if(planetSelected!=null){
                System.out.println(planetSelected.getPosition().toString());
            }else{
                System.out.println("Planet not selected");
            }
        }
    }

    public void render(Vector2f resolution, Camera camera) throws Exception { //Render objects to the screen

        if(planetSelected!=null){
            TextGUI text=(TextGUI)GUIElements.get(0);
            text.changeText(Float.toString(planetSelected.getPosition().x));
            text.generateText();
            text.render(resolution);
        }
        program.useProgram();
        program.setUniform("resolution", resolution);
        program.setUniform("viewMatrix",camera.getViewMatrix());
        for(Planet planet:planets){
            planet.render(program,camera);
        }
    }

    public void cleanup(){  //Cleanup objects after the simulation is closed, frees up memory allocated for them
        timer.setUPS(60);
        if(planets!=null){
            for(Planet planet:planets){
                planet.cleanup();
            }
            planets=null;
        }
        program.cleanup();
    }
}
