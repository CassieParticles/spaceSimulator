package gameObjects;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import utils.Input;

public class Controller {
    protected Vector2f position;
    protected float scale;

    private Planet planetFollowing;

    protected float minZoom;
    protected float maxZoom;

    private boolean followingPlanet;

    public Controller(Vector2f position, float minZoom, float maxZoom, float scale){
        this.position=new Vector2f(position);
        this.minZoom=minZoom;
        this.maxZoom=maxZoom;
        this.scale=scale;
        this.followingPlanet=false;
    }

    public Controller(Vector2f position, float minZoom, float maxZoom){
        this(position,minZoom,maxZoom,1);
    }

    public Planet getPlanetFollowing(){
        return planetFollowing;
    }
    public void setFollowing(Planet planet){ //Sets the camera to follow a planet
        this.planetFollowing=planet;
        this.followingPlanet=true;
    }

    public void stopFollowing(){
        this.position=new Vector2f(planetFollowing.getPosition());
        planetFollowing=null;
        followingPlanet=false;
    }

    public boolean getFollowing(){
        return followingPlanet;
    }

    public void control(Input input, float moveSpeed,float scaleSpeed, float deltaTime){
        boolean moved=false;
        Vector2f move=new Vector2f();
        if(input.isKeyDown(GLFW.GLFW_KEY_UP)){
            move.y+=1;
            moved=true;
        }if(input.isKeyDown(GLFW.GLFW_KEY_DOWN)){
            move.y-=1;
            moved=true;
        }if(input.isKeyDown(GLFW.GLFW_KEY_LEFT)){
            move.x-=1;
            moved=true;
        }if(input.isKeyDown(GLFW.GLFW_KEY_RIGHT)){
            move.x+=1;
            moved=true;
        }if(input.isKeyDown(GLFW.GLFW_KEY_EQUAL)){
            scale =Math.min(Math.max(scale*(1+scaleSpeed*deltaTime),minZoom),maxZoom);
        }if(input.isKeyDown(GLFW.GLFW_KEY_MINUS)){
            scale =Math.min(Math.max(scale*(1-scaleSpeed*deltaTime),minZoom),maxZoom);
        }
        if(followingPlanet&&moved){    //Allows the camera to easily escape following a planet
            stopFollowing();
        }
        position.add(move.mul(moveSpeed*deltaTime/(scale*2)));
    }


}
