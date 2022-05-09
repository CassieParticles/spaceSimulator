package main;

import gameObjects.Camera;
import gameObjects.Planet;
import guiObjects.TextGUI;
import guiObjects.TextureGUI;
import guiObjects.buttons.ButtonGUI;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
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

    private ButtonGUI quitButton;
    private TextGUI quitText;
    private boolean run;

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
        run=true;
        screenSize=new Vector2i(900,900);
        window=new Window(screenSize.x, screenSize.y, "Title");
        timer=new Timer(60,60);
        input=new Input();
        Camera camera = new Camera(0.01f, 20, 0.1f);

        window.init();
        input.init(window.getWindowHandle());

        background=new TextureGUI(new Vector2f(screenSize.x/-2,screenSize.y/-2),new Vector2f(screenSize.x,screenSize.y),"resources/background.jpg");
        quitButton=new ButtonGUI(new Vector2f(-440,360),new Vector2f(100,50),new Vector3f(0.6f),input,()->{run=false;});
        quitText=new TextGUI(new Vector2f(-430,370),new Vector2f(1,1),"Quit",4,50,"resources/slabo.png","resources/slaboData.csv");

        GUIHandler.init(input);
        PageManager.init(timer, camera,input,window);

        ArrayList<Planet> planets=new ArrayList<>();

        Random rand=ThreadLocalRandom.current();

        PageManager.loadBuildSim(planets,0.1f);

        GL46.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        window.loop();
    }



    private void render() throws Exception {
        window.loop();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT); //Clears the frame so the last rendered frame doesn't remain on the screen
        background.render(window.getResolution());
        PageManager.render();
        quitButton.render(window.getResolution());
        quitText.render(window.getResolution());
    }

    private void update() throws Exception {
        if(!run){
            window.close();
        }
        PageManager.update();
        quitButton.use(window.getResolution());
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
