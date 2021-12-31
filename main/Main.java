package main;

import guiObjects.TextGUI;
import guiObjects.buttons.TextBoxGUI;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import rendering.Camera;
import utils.Input;
import utils.Timer;

public class Main {

    private Window window;
    private Timer timer;
    private Input input;

    private int clicked;

//    private Program circleProgram;

    private TextGUI text;
    private TextBoxGUI button;

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

        text=new TextGUI(new Vector2f(50,150),new Vector2f(1,1),"sus",15,500,"src/resources/slabo.png","src/resources/slaboData.csv");

        button=new TextBoxGUI(new Vector2f(50,50),new Vector2f(500,100),new Vector3f(0.3f, 0.2f, 0.8f),new Vector3f(0.69f,0.75f,0.1f), input);
        button.initText("sus",15,"src/resources/slabo.png","src/resources/slaboData.csv","QWERTYUIOPASDFGHJKLZXCVBNM 0123456789");

        System.out.println(button.toString());

        GL46.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        window.loop();
    }

    private void render(){
        window.loop();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
        button.render(new Vector2f(window.getWidth(),window.getHeight()));
        text.render(new Vector2f(window.getWidth(),window.getHeight()));
    }

    private void update() throws Exception {
        if(input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){
            window.close();
        }

        button.use(new Vector2f(window.getWidth(),window.getHeight()));
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
        button.cleanup();
        window.cleanup();
        text.cleanup();
    }

}
