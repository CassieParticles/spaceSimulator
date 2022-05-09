package sections.RunningSimulator;

import gameObjects.Camera;
import gameObjects.Planet;
import guiObjects.GUI;
import guiObjects.TextGUI;
import guiObjects.buttons.ButtonGUI;
import guiObjects.buttons.TextBoxGUI;
import guiObjects.buttons.ToggleButtonGUI;
import main.PageManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import rendering.Program;
import rendering.Shader;
import utils.FileHandling;
import utils.Input;
import utils.Timer;

import java.util.ArrayList;

public class RunSimMain {

    Program program;
    Physics physics;
    Timer timer;

    Planet planetSelected;

    private float timeStep=1;


    public RunSimMain(){

    }

    private ArrayList<Planet> initialPlanets;
    private Planet[] planets;
    private ArrayList<GUI> GUIElements;
    private float gravityConstant;

    private boolean paused;

    private ButtonGUI slowDownButton;
    private ToggleButtonGUI pauseButton;
    private ButtonGUI speedUpButton;
    private TextBoxGUI timeStepBox;
    private ButtonGUI followPlanetButton;
    private ButtonGUI simulationBuilderButton;

    public void init(ArrayList<Planet> planets,ArrayList<GUI> GUIElements, float gravityConstant,Timer timer, Camera camera) throws Exception {   //Initialise everything in the planet simulator, ready to be used
        initialPlanets=new ArrayList<>();
        initialPlanets.addAll(planets);
        this.planets=new Planet[planets.size()];   //Initialising variables
        for(int i=0;i<planets.size();i++) {
        	this.planets[i]=planets.get(i).clone();
        }
        
        this.GUIElements=GUIElements;
        this.gravityConstant=gravityConstant;
        this.timer=timer;

        physics=new Physics();
        program=new Program();

        program.attachShaders(new Shader[]{ //Create the shader used to render the planets
                new Shader(FileHandling.loadResource("shaders/vertex.glsl"), GL46.GL_VERTEX_SHADER),
                new Shader(FileHandling.loadResource("shaders/geometry.glsl"),GL46.GL_GEOMETRY_SHADER),
                new Shader(FileHandling.loadResource("shaders/fragment.glsl"),GL46.GL_FRAGMENT_SHADER)});

        program.link(); //Compiling the shader

        program.createUniform("position");
        program.createUniform("radius");
        program.createUniform("viewMatrix");
        program.createUniform("resolution");
        program.createUniform("colour");

        timeStepBox=((TextBoxGUI)GUIElements.get(9));
        slowDownButton=((ButtonGUI)GUIElements.get(16));
        pauseButton=((ToggleButtonGUI)GUIElements.get(15));
        speedUpButton=((ButtonGUI)GUIElements.get(18));
        followPlanetButton=(ButtonGUI)GUIElements.get(20);
        simulationBuilderButton=(ButtonGUI)GUIElements.get(23);

        timeStepBox.clearFontCharacters();
        
        timeStepBox.initText(Float.toString(timeStep),5,"resources/slabo.png","resources/slaboData.csv","1234567890.");
        ((TextGUI)GUIElements.get(11)).changeText(Float.toString(gravityConstant));
        ((TextGUI)GUIElements.get(11)).generateText();

        slowDownButton.setAction(() -> {
            changeTimeStep((timeStep*10-1)/10); //Prevents floating point error, effectively adds 0.1
            try {
                timeStepBox.setText(Float.toString(timeStep));  //Since creating a new mesh can technically throw an error, this needs to be done in a try/catch
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        speedUpButton.setAction(() -> {
            changeTimeStep((timeStep*10+1)/10);
            try {
                timeStepBox.setText(Float.toString(timeStep));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        followPlanetButton.setAction(() -> {
            if(planetSelected!=camera.getPlanetFollowing()||!camera.getFollowing()){
                camera.setFollowing(planetSelected);
            }else{
                camera.stopFollowing();
            }
        });

        simulationBuilderButton.setAction(() -> {
            try {   //Can throw an exception, needs to be surrounded by a try/catch
                PageManager.loadBuildSim(initialPlanets,gravityConstant);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pauseButton.setEnabled(false);
    }

    public void changeTimeStep(float timeStep){ //Change the timestep for the physics simulation, and change the update rate to compensate
        this.timeStep=(Math.max(Math.min(timeStep,3),0.5f));
        if(timeStep!=0){
            timer.setUPS(60*timeStep);
        }else{
            timer.setUPS(60);
        }
    }

    private float square(float a){  //Squares a float
        return a*a;
    }

    private Planet getPlanetSelected(Input input,Camera camera, Vector2f resolution){   //Returns which planet the mouse is over, or null if the mouse isn't over a planet
        int[] mouseScreenPos=input.getMousePos();
        Vector2f mouseClipPos=new Vector2f(mouseScreenPos[0],mouseScreenPos[1]).div(new Vector2f(resolution).div(new Vector2f(2,-2))).sub(new Vector2f(1,-1));  //Normalises mouse screen position between -1 and 1
        Vector4f mouseWorldPos=new Vector4f(mouseClipPos,0,1).mul(new Matrix4f(camera.getViewMatrix()).invert());    //needs to be vector4f to allow matrix multiplication
        for(Planet planet:planets){
            if(square(mouseWorldPos.x-planet.getPosition().x)+square(mouseWorldPos.y-planet.getPosition().y)<square(planet.getRadius())){
                return planet;
            }
            if(followPlanetButton.getBG().pointInRectangle(new Vector2f(mouseScreenPos[0]-resolution.x/2,mouseScreenPos[1]*-1+resolution.y/2))){
                return planetSelected;
            }
        }
        return null;
    }

    private String validateText(String input){
        boolean decimalFound=false;
        StringBuilder str= new StringBuilder();

        for(char c:timeStepBox.toString().toCharArray()){  //Removes any duplicate decimal places
            if(!(decimalFound&&c=='.')){
                str.append(c);
            }
            decimalFound=c=='.'||decimalFound;  //Ensures decimalFound remains true once it is set to true
        }
        if(str.length()==0||str.toString().equals(".")){    //Returns string, or null if string is 0 characters long
            return null;
        }else{
            return str.toString();
        }
    }

    public void update(float updateRate, Camera camera, Input input, Vector2f resolution) throws Exception {  //Update physics simulation and manage inputs
        if(input.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            pauseButton.setEnabled(!pauseButton.getEnabled());
        }
        if (pauseButton.getEnabled()) {
            physics.runPhysics(updateRate*timeStep,gravityConstant,planets);
        }

        camera.control(input,3,0.8f, updateRate);
        timeStepBox.use(resolution);
        pauseButton.use(resolution);
        slowDownButton.use(resolution);
        speedUpButton.use(resolution);
        simulationBuilderButton.use(resolution);
        if(planetSelected!=null){
            followPlanetButton.use(resolution);
        }
        if(!timeStepBox.getSelected()){ //Only modifies timestep once box is unselected
            String string=validateText(timeStepBox.toString());

            if(string==null){   //If no numbers are provided
                timeStepBox.setText(Float.toString(timeStep));
            }
            else if(Float.parseFloat(string)!=timeStep){
                changeTimeStep(Math.max(Math.min(Float.parseFloat(string),3),0.5f));
                timeStepBox.setText(Float.toString(timeStep));  //Ensures text bo contains validated input
            }
        }

        if(input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)){
            planetSelected=getPlanetSelected(input,camera,resolution);
        }
    }

    private float power(float number, int power){   //Raises a number to an integer power
        float num=1;
        for(int i=0;i<power;i++){
            num*=number;
        }
        return num;
    }

    private float round(float number,int dp){   //Rounds a number to a number of decimal places
        int s=(int)power(10,dp);
        return (float)Math.floor(number*s)/s;
    }

    public void render(Vector2f resolution, Camera camera) throws Exception { //Render objects to the screen

        program.useProgram();
        program.setUniform("resolution", resolution);
        program.setUniform("viewMatrix",camera.getViewMatrix());
        for(Planet planet:planets){
            planet.render(program,camera);
        }
        program.unlinkProgram();
        GUIElements.get(12).render(resolution); //Must be rendered first of it'll overlap other GUI elements
        GUIElements.get(13).render(resolution);

        for(int i=0;i<GUIElements.size();i++){  //GUI is rendered differently depending on which piece of GUI it is
            switch(i){
                case 9: //All GUI elements that are rendered even if no planet is selected
                case 10:
                case 11:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 23:
                case 24:
                case 25:
                    GUIElements.get(i).render(resolution);
                    break;
                case 2:
                    if(planetSelected!=null){   //Ensure planet is selected before rendering
                        TextGUI text=(TextGUI)GUIElements.get(i);
                        text.changeText(round(planetSelected.getPosition().x,3)+","+round(planetSelected.getPosition().y,3));   //Update text
                        text.generateText();
                        text.render(resolution);    //Render text
                    }
                    break;
                case 4:
                    if(planetSelected!=null){
                        TextGUI text=(TextGUI)GUIElements.get(i);
                        text.changeText(round(planetSelected.getVelocity().x,3)+","+round(planetSelected.getVelocity().y,3));
                        text.generateText();
                        text.render(resolution);
                    }
                    break;
                case 6:
                    if(planetSelected!=null){
                        TextGUI text=(TextGUI)GUIElements.get(i);
                        text.changeText(round(planetSelected.getMass(),3)+"");
                        text.generateText();
                        text.render(resolution);
                    }
                    break;
                case 8:
                    if(planetSelected!=null){
                        TextGUI text=(TextGUI)GUIElements.get(i);
                        text.changeText(round(planetSelected.getRadius(),3)+"");
                        text.generateText();
                        text.render(resolution);
                    }
                    break;
                case 12:
                case 13:
                    continue;
                case 21:
                    if(planetSelected != null && planetSelected != camera.getPlanetFollowing()){
                        GUIElements.get(i).render(resolution);
                    }
                    continue;
                case 22:
                    if(planetSelected!=null&&(planetSelected==camera.getPlanetFollowing())){
                        GUIElements.get(i).render(resolution);
                    }
                    continue;
                default:
                    if(planetSelected!=null) {
                        GUIElements.get(i).render(resolution);
                    }
            }
        }
    }
    
    public void closePage(){
        timer.setUPS(60);
        program.cleanup();
    	for(Planet planet:planets){
    		planet.cleanup();
    	}
    }
}
