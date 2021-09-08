package main;

import utils.Input;
import utils.Timer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

public class Main {

    private Window window;
    private Timer timer;
    private Input input;

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

    public void init(){
        window=new Window(600,600,"Title");
        timer=new Timer(60,60);
        input=new Input();

        window.init();
        input.init(window);

        GL46.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        window.loop();
    }

    private void render(){
        window.loop();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    }

    private void update(){
        if(input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){
            window.close();
        }
    }

    public void loop(){
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
    }

}
