package gameObjects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import rendering.CircleMesh;
import rendering.Program;

public class Planet {
    private final CircleMesh mesh;

    private final Vector2f position;
    private final Vector2f velocity;
    private float radius;
    private float mass;

    private final Vector2f acceleration;

    private final Vector3f colour;

    public Planet(Vector2f position,Vector2f velocity, float radius,float mass, Vector3f colour){
        this.position=position;
        this.radius=radius;
        this.colour=colour;
        this.mass=mass;

        this.velocity=velocity;
        this.acceleration=new Vector2f();


        this.mesh=new CircleMesh();
    }

    public void setPosition(Vector2f position){
        this.position.set(position);
    }

    public Vector2f getPosition(){
        return new Vector2f(position);
    }

    public void addPosition(Vector2f translation){
        position.add(translation);
    }

    public void setVelocity(Vector2f velocity){
        this.velocity.set(velocity);
    }

    public void addVelocity(Vector2f velocity){
        this.velocity.add(velocity);
    }

    public Vector2f getVelocity(){
        return new Vector2f(velocity);
    }

    public void setMass(float mass){
        this.mass=mass;
    }

    public float getMass(){
        return mass;
    }

    public void addForce(Vector2f force){
        acceleration.add(new Vector2f(force).div(mass));  //F=ma, a=F/m
    }

    public Vector3f getColour(){
        return new Vector3f(colour);
    }

    public void setColour(Vector3f colour){
        this.colour.set(colour);
    }

    public void update(float deltaUpdate){
        velocity.add(acceleration.mul(deltaUpdate));
        acceleration.set(0);
        this.position.add(new Vector2f(velocity).mul(deltaUpdate));
    }

    public void setRadius(float radius){
        this.radius=radius;
    }

    public float getRadius(){
        return radius;
    }

    public void render(Program program, Camera camera){
        program.setUniform("position",position);
        program.setUniform("colour",colour);
        program.setUniform("radius",radius);

        mesh.render();
    }

    public void cleanup(){
        mesh.cleanup();
    }
    
    @Override
    public Planet clone() throws CloneNotSupportedException {
        Planet clone = (Planet) super.clone();
        return new Planet(new Vector2f(position),new Vector2f(velocity),radius, mass,new Vector3f(colour));
    }
}
