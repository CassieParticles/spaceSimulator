package main;

import gameObjects.Camera;
import gameObjects.Planet;
import guiObjects.TextureGUI;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import utils.Input;
import utils.Timer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private Window window;
    private Timer timer;
    private Input input;

    private int clicked;

    private Planet planet;

    private TextureGUI background;

    Vector2i screenSize;

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
        screenSize=new Vector2i(900,900);
        window=new Window(screenSize.x, screenSize.y, "Title");
        timer=new Timer(60,60);
        input=new Input();
        Camera camera = new Camera(0.01f, 20, 0.1f);

        window.init();
        input.init(window.getWindowHandle());

        background=new TextureGUI(new Vector2f(screenSize.x/-2,screenSize.y/-2),new Vector2f(screenSize.x,screenSize.y),"resources/background.jpg");

        GUIHandler.init(input);
        PageManager.init(timer, camera,input,window);

        ArrayList<Planet> planets=new ArrayList<>();

        Random rand=ThreadLocalRandom.current();

        int num=500;
        for(int i=0;i<num;i++){
            float mass=0.2f;
            planets.add(new Planet(new Vector2f((float) (rand.nextFloat()*(Math.sqrt(num)+10)),(float) (rand.nextFloat()*(Math.sqrt(num)+10))),new Vector2f(0),mass,mass*10,new Vector3f(i/(float)num)));
        }

//        planets.add(new Planet(new Vector2f(15f,-0.5f),new Vector2f(0,0f),1f,1000f,new Vector3f(0,1,0)));
////        planets.add(new Planet(new Vector2f(15.75f,-0.5f),new Vector2f(0,2f),0.04f,0.1f,new Vector3f(1,0,1)));
//        planets.add(new Planet(new Vector2f(0,0),new Vector2f(0,0f),1f,1000,new Vector3f(1,0,0)));
////        planets.add(new Planet(new Vector2f(14.392f,-0.4f),new Vector2f(0,2f),0.05f,0.00001f,new Vector3f(1,1,0)));

        PageManager.loadBuildSim(planets,0.1f);

        GL46.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        window.loop();
    }



    private void render() throws Exception {
        window.loop();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT); //Clears the frame so the last rendered frame doesn't remain on the screen
        background.render(window.getResolution());
        PageManager.render();
    }

    private void update() throws Exception {
        if(input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){
            window.close();
        }
        PageManager.update();
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
        
        GUIHandler.cleanup();
        PageManager.cleanup();

        background.cleanup();

        System.out.println("Cleaned up");
    }

}
