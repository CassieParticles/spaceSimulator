package gameObjects;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import utils.Input;

public class Controller {
    protected Vector2f position;
    protected float scale;

    protected float minZoom;
    protected float maxZoom;

    public Controller(Vector2f position, float minZoom, float maxZoom, float scale){
        this.position=new Vector2f(position);
        this.minZoom=minZoom;
        this.maxZoom=maxZoom;
        this.scale=scale;
    }

    public Controller(Vector2f position, float minZoom, float maxZoom){
        this(position,minZoom,maxZoom,1);
    }

    public void control(Input input, float moveSpeed,float scaleSpeed, float deltaTime){
        Vector2f move=new Vector2f();
        if(input.isKeyDown(GLFW.GLFW_KEY_UP)){
            move.y+=1;
        }if(input.isKeyDown(GLFW.GLFW_KEY_DOWN)){
            move.y-=1;
        }if(input.isKeyDown(GLFW.GLFW_KEY_LEFT)){
            move.x-=1;
        }if(input.isKeyDown(GLFW.GLFW_KEY_RIGHT)){
            move.x+=1;
        }if(input.isKeyDown(GLFW.GLFW_KEY_EQUAL)){
            scale =Math.min(Math.max(scale+scaleSpeed*deltaTime,minZoom),maxZoom);
        }if(input.isKeyDown(GLFW.GLFW_KEY_MINUS)){
            scale =Math.min(Math.max(scale-scaleSpeed*deltaTime,minZoom),maxZoom);
        }
        position.add(move.mul(moveSpeed*deltaTime));
    }


}
