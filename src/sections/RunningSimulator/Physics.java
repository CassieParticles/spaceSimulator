package sections.RunningSimulator;

import gameObjects.Planet;
import org.joml.Vector2f;

public class Physics {
    public void runPhysics(float timeStep,float gravityConstant, Planet[] planets){
        for(int a=0;a<planets.length;a++){
            for(int b=a;b<planets.length;b++){
                if(a!=b){
                    applyGravity(planets[a],planets[b],gravityConstant);
                    checkCollision(planets[a], planets[b]);
                }
            }
            planets[a].update(timeStep);
        }
    }

    private void applyGravity(Planet planetA, Planet planetB, float gravityConstant){   //Calculates and applies the force of gravity between 2 planets
        Vector2f dVec=planetB.getPosition().sub(planetA.getPosition());
        float forceScalar=(planetA.getMass()*planetB.getMass()*gravityConstant)/(square(dVec.x)+square(dVec.y));   // Force=(MassA * MassB * Gravity constant)/dist^2
        dVec.normalize().mul(forceScalar);  //Converts the force into a vector

        planetA.addForce(dVec); //Applies force to both planets
        planetB.addForce(dVec.mul(-1));
    }

    private void checkCollision(Planet planetA, Planet planetB){  //Calculates and alters planets if they are currently colliding
        Vector2f dVec=planetB.getPosition().sub(planetA.getPosition());
        float diffSqr=square(dVec.x)+square(dVec.y);   //Distance between 2 planets squared
        if(diffSqr<square(planetA.getRadius()+planetB.getRadius())){   //Checks for intersection, square is less demanding the sqrt
            float intersection= (float) (planetA.getRadius()+planetB.getRadius()-Math.sqrt(diffSqr));
            float massRatio=planetA.getMass()/(planetA.getMass()+ planetB.getMass());   //How much of the total mass is planetA

            Vector2f normalVector=new Vector2f(dVec).normalize();

            planetA.addPosition(new Vector2f(normalVector).mul((-1f*(intersection)*(1-massRatio))));    //Displace planets
            planetB.addPosition(new Vector2f(normalVector).mul((1f*intersection*(massRatio))));

            float perpDotA=planetA.getVelocity().dot(normalVector);
            float perpDotB=planetB.getVelocity().dot(normalVector);

            planetA.addVelocity(new Vector2f(normalVector).mul(perpDotA*(1-massRatio)*-2)); //Reflect each planet's own velocity
            planetB.addVelocity(new Vector2f(normalVector).mul(perpDotB*massRatio*-2));

            planetA.addVelocity(new Vector2f(normalVector).mul(perpDotB*(1-massRatio)*2));  //Planet's affect eachother's velocity
            planetB.addVelocity(new Vector2f(normalVector).mul(perpDotA*massRatio*2));
        }
    }

    private float square(float a){
        return a*a;
    }
}

