package gameObjects;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera extends Controller{

    public Camera(float minZoom, float maxZoom){    //Camera is a class created to hold variables related to the view of the scene
        super(new Vector2f(),minZoom,maxZoom);
    }

    public Camera(float minZoom, float maxZoom, float zoom){
        super(new Vector2f(),minZoom,maxZoom,zoom);
    }

    public void setPosition(Vector2f position){
        this.position.set(position);
    }

    public Vector2f getPosition(){
        return new Vector2f(position);
    }

    public void translate(Vector2f translation){
        this.position.add(translation);
    }

    public void setScale(float scale){
        this.scale =Math.min(Math.max(scale,minZoom),maxZoom); //Stops the zoom value exceeding the constraints
    }

    public float getScale(){
        return scale;
    }

    public void zoom(float zoom){
        this.scale =Math.min(Math.max(this.scale +zoom,minZoom),maxZoom);
    }

    public Matrix4f getViewMatrix(){
//        return new Matrix4f().identity();
        return new Matrix4f().identity().scale(scale).translate(new Vector3f(position,0).mul(-1));  //Construct a transformation matrix for all the planets
    }
}
