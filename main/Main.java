package main;

import gameObjects.Camera;
import gameObjects.Planet;
import guiObjects.GUI;
import guiObjects.TextGUI;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import rendering.Program;
import rendering.Shader;
import sections.RunningSimulator.RunSimMain;
import utils.FileHandling;
import utils.Input;
import utils.Timer;

import java.util.ArrayList;

public class Main {

    private Window window;
    private Timer timer;
    private Input input;

    private RunSimMain simRunning;

    private int clicked;

    private Program program;

    private Planet planet;

    private Camera camera;

    public static void main(String[] args){
        new Main().gameLoop();
    }

    public void gameLoop(){
        try{
            init();
            loop();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            cleanup();
        }
    }

    public void init() throws Exception {
        window=new Window(900,900,"Title");
        timer=new Timer(60,60);
        input=new Input();


        window.init();
        input.init(window.getWindowHandle());

        simRunning=new RunSimMain();

        program=new Program();

        program.attachShaders(new Shader[]{
                new Shader(FileHandling.loadResource("src/shaders/vertex.glsl"),GL46.GL_VERTEX_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/geometry.glsl"),GL46.GL_GEOMETRY_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/fragment.glsl"),GL46.GL_FRAGMENT_SHADER)});

        program.link();

        program.createUniform("position");
        program.createUniform("viewMatrix");
        program.createUniform("resolution");
        program.createUniform("colour");

        ArrayList<Planet> planets=new ArrayList<>();
        ArrayList<GUI> gui=new ArrayList<>();

        gui.add(new TextGUI(new Vector2f(),new Vector2f(1,1),"Amougus",15,150,"src/resources/slabo.png","src/resources/slaboData.csv"));

        planets.add(new Planet(new Vector2f(2f,-0.5f),new Vector2f(0,0.1f),0.08f,0.5f,new Vector3f(0,1,0)));
        planets.add(new Planet(new Vector2f(-0.5f,-0.5f),new Vector2f(0,0f),0.2f,50,new Vector3f(1,0,0)));

        simRunning.init(planets,gui, 0.1f,timer);

        camera=new Camera(0.01f,20,0.1f);

        GL46.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        window.loop();
    }



    private void render() throws Exception {
        window.loop();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
        simRunning.render(window.getResolution(),camera);
    }

    private void update() throws Exception {
        if(input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){
            window.close();
        }
        simRunning.update((float) timer.getDeltaUpdate(),camera, input,window.getResolution());
        input.updateInputs();
    }

    public void loop() throws Exception {
        while(!window.shouldClose()){
            timer.update();
            if(timer.getFrame()){
                render();
            }if(timer.getUpdate()){
                update();
            }
        }
    }

    public void cleanup(){
        System.out.println("Cleaning up");
        window.cleanup();

        program.cleanup();
        simRunning.cleanup();
    }

}
