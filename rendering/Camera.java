package rendering;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Vector2f position;
    private float zoom;

    private float maxZoom;
    private float minZoom;

    public Camera(float maxZoom, float minZoom){    //Camera is a class created to hold variables related to the view of the scene
        this.position=new Vector2f();
        this.zoom=1;
        this.maxZoom=maxZoom;
        this.minZoom=minZoom;
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

    public void setZoom(float zoom){
        this.zoom=Math.min(Math.max(zoom,minZoom),maxZoom); //Stops the zoom value exceeding the
    }

    public float getZoom(){
        return zoom;
    }

    public void zoom(){
        this.zoom+=Math.min(Math.max(zoom,minZoom),maxZoom);
    }

    public Matrix4f getViewMatrix(){
        return new Matrix4f().identity().scale(1/zoom).translate(new Vector3f(position,0));
    }
}
