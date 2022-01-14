package sections.RunningSimulator;

import gameObjects.Planet;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Physics {
    public void runPhysics(float timeStep,float gravityConstant, ArrayList<Planet> planets){
        for(int a=0;a<planets.size();a++){
            for(int b=a;b<planets.size();b++){
                if(a!=b){
                    applyGravity(planets.get(a),planets.get(b),gravityConstant);
                    checkCollision(planets.get(a), planets.get(b));
                }

            }
            planets.get(a).update(timeStep);
        }
    }

    private void applyGravity(Planet planetA, Planet planetB, float gravityConstant){   //Calculates and applies the force of gravity between 2 planets
        Vector2f dVec=planetB.getPosition().sub(planetA.getPosition());
        float forceScalar=(planetA.getMass()*planetB.getMass()*gravityConstant)/(square(dVec.x)+square(dVec.y));   // Force=(MassA * MassB * Gravity constant)/dist^2
        dVec.normalize().mul(forceScalar);  //Converts the force into a vector

        planetA.addForce(dVec);
        planetB.addForce(dVec.mul(-1));
    }

    private void checkCollision(Planet planetA, Planet planetB){  //Calculates and alters planets if they are currently colliding
        Vector2f dVec=planetB.getPosition().sub(planetA.getPosition());
        float diffSqr=square(dVec.x)+square(dVec.y);   //Distance between 2 planets squared
        if(diffSqr<square(planetA.getRadius()+planetB.getRadius())){   //Checks for intersection, square is less demanding the sqrt
            float intersection= (float) (Math.sqrt(square(planetA.getRadius()+planetB.getRadius()))-Math.sqrt(diffSqr));
            float massRatio=planetA.getMass()/(planetA.getMass()+ planetB.getMass());

            Vector2f normalVector=new Vector2f(dVec).normalize();

            planetA.addPosition(new Vector2f(normalVector).mul((float) (-0.25f*(Math.sqrt(intersection))*(1-massRatio))));
            planetB.addPosition(new Vector2f(normalVector).mul((float) (0.25f*Math.sqrt(intersection)*(massRatio))));

            float perpDotA=planetA.getVelocity().dot(normalVector);
            float perpDotB=planetB.getVelocity().dot(normalVector);

            planetA.addVelocity(new Vector2f(normalVector).mul(perpDotA*(1-massRatio)*-2));
            planetB.addVelocity(new Vector2f(normalVector).mul(perpDotB*massRatio*-2));

            planetA.addVelocity(new Vector2f(normalVector).mul(perpDotB*(1-massRatio)*2));
            planetB.addVelocity(new Vector2f(normalVector).mul(perpDotA*massRatio*2));
        }
    }

    private float square(float a){
        return a*a;
    }
}

