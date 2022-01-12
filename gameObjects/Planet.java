package gameObjects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import rendering.CircleMesh;
import rendering.Program;

public class Planet {
    private CircleMesh mesh;

    private Vector2f position;
    private Vector2f velocity;
    private float radius;
    private float mass;

    private Vector2f acceleration;

    private Vector3f colour;

    public Planet(Vector2f position,Vector2f velocity, float radius,float mass, Vector3f colour){
        this.position=position;
        this.radius=radius;
        this.colour=colour;
        this.mass=mass;

        this.velocity=velocity;
        this.acceleration=new Vector2f();


        this.mesh=new CircleMesh(radius);
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

    public void addForce(Vector2f force){   //Applies force, so mas is taken into account
        acceleration.add(new Vector2f(force).div(mass));  //F=ma, a=F/m
    }

    public void addAcc(Vector2f acc){
        acceleration.add(acc);
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

    public void setAcc(Vector2f acc){
        this.acceleration=acc;
    }

    public Vector2f getAcc(){
        return new Vector2f(acceleration);
    }

    public void render(Program program, Camera camera){
        program.setUniform("position",position);
        program.setUniform("colour",colour);

        mesh.render();
    }

    public void cleanup(){
        mesh.cleanup();
    }
}
