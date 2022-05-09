package sections.buildingSimulator;

import gameObjects.Camera;
import gameObjects.Planet;
import guiObjects.GUI;
import guiObjects.buttons.ButtonGUI;
import guiObjects.buttons.TextBoxGUI;
import main.PageManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import rendering.Program;
import rendering.Shader;
import utils.FileHandling;
import utils.Input;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BuildSimMain {

    private TextBoxGUI planetPosX;  //GUI User Inputs
    private TextBoxGUI planetPosY;
    private TextBoxGUI planetVelX;
    private TextBoxGUI planetVelY;
    private TextBoxGUI planetSize;
    private TextBoxGUI planetMass;
    private TextBoxGUI planetColR;
    private TextBoxGUI planetColG;
    private TextBoxGUI planetColB;

    private TextBoxGUI simGravConst;

    private ButtonGUI addPlanetButton;
    private ButtonGUI deletePlanetButton;
    private ButtonGUI planetSimulatorButton;

    private Planet planetSelected;
    private TextBoxGUI GUISelectedLF;
    private Planet planetSelectedLF;

    private void updateInputGUI(Vector2f screenSize) throws Exception {
        planetPosX.use(screenSize);
        planetPosY.use(screenSize);
        planetVelX.use(screenSize);
        planetVelY.use(screenSize);
        planetSize.use(screenSize);
        planetMass.use(screenSize);
        planetColR.use(screenSize);
        planetColG.use(screenSize);
        planetColB.use(screenSize);
        simGravConst.use(screenSize);
    }

    private ArrayList<Planet> planets;
    private ArrayList<GUI> GUIElements;

    private float gravityConstant;

    private Program program;

    public void init(ArrayList<Planet> planets, ArrayList<GUI> GUIElements, float gravityConstantIn, Camera camera) throws Exception {
        this.planets=new ArrayList<>();
        this.planets.addAll(planets);
        this.GUIElements=GUIElements;
        this.gravityConstant=gravityConstantIn;

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

        planetPosX=(TextBoxGUI) GUIElements.get(7);
        planetPosY=(TextBoxGUI) GUIElements.get(8);
        planetVelX=(TextBoxGUI) GUIElements.get(9);
        planetVelY=(TextBoxGUI) GUIElements.get(10);
        planetMass=(TextBoxGUI) GUIElements.get(11);
        planetSize=(TextBoxGUI) GUIElements.get(12);
        planetColR=(TextBoxGUI) GUIElements.get(13);
        planetColG=(TextBoxGUI) GUIElements.get(14);
        planetColB=(TextBoxGUI) GUIElements.get(15);
        simGravConst=(TextBoxGUI) GUIElements.get(16);
        addPlanetButton=(ButtonGUI) GUIElements.get(18);
        deletePlanetButton=(ButtonGUI) GUIElements.get(20);
        planetSimulatorButton=(ButtonGUI) GUIElements.get(22);

        planetPosX.clearFontCharacters();

        String fontPath = "resources/slabo.png";
        String fontCSVPath = "resources/slaboData.csv";
        planetPosX.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetPosY.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetVelX.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetVelY.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetMass.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetSize.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetColR.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetColG.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        planetColB.initText("",6, fontPath, fontCSVPath,"1234567890-.E");
        simGravConst.initText(Float.toString(gravityConstant),10, fontPath, fontCSVPath,"1234567890-.E");

        addPlanetButton.setAction(() -> {
            planetSelected=addPlanet(camera.getPosition());
            try {   //Since updating text can throw an exception, it must be surrounded with a try/catch
                updateGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        deletePlanetButton.setAction(() -> {
            if(planetSelected!=null){
                removePlanet(planetSelected);
                planetSelected=null;
            }
        });

        planetSimulatorButton.setAction(() -> {
            try {
                PageManager.loadRunSim(getPlanets(),gravityConstant);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void init(ArrayList<GUI> GUIElements, float gravityConstant, Camera camera) throws Exception {     //Call if empty universe is to be created
        init(new ArrayList<>(),GUIElements,gravityConstant, camera);
    }

    private Vector3f genRandColour(){   //Generates a random colour, preventing random planets being too dark
        int i=0;
        Random rand= ThreadLocalRandom.current();
        float[] vals=new float[3];
        do{
            vals[i]=rand.nextFloat();
            if(vals[i]>0.3f){   //Doesn't move on until number is above 0.3
                i++;
            }
        }while(i<3);
        return new Vector3f(vals);
    }

    public Planet addPlanet(Vector2f position){
        //Random new planets will have a random colour, so even if the user doesn't want to decide colours, they are still different
        Random rand= ThreadLocalRandom.current();
        Planet planet=new Planet(new Vector2f(position),new Vector2f(),1f,1,genRandColour());
        planets.add(planet);
        return planet;
    }

    public void removePlanet(Planet planet){
        planet.cleanup();
        planets.remove(planet);
    }

    private float square(float a){  //Squares a float
        return a*a;
    }

    private Planet getPlanetSelected(Input input, Camera camera, Vector2f resolution){   //Returns which planet the mouse is over, or null if the mouse isn't over a planet
        int[] mouseScreenPos=input.getMousePos();
        Vector2f mouseClipPos=new Vector2f(mouseScreenPos[0],mouseScreenPos[1]).div(new Vector2f(resolution).div(new Vector2f(2,-2))).sub(new Vector2f(1,-1));  //Normalises mouse screen position between -1 and 1
        Vector4f mouseWorldPos=new Vector4f(mouseClipPos,0,1).mul(new Matrix4f(camera.getViewMatrix()).invert());    //needs to be vector4f to allow matrix multiplication
        for(Planet planet:planets){
            if(square(mouseWorldPos.x-planet.getPosition().x)+square(mouseWorldPos.y-planet.getPosition().y)<square(planet.getRadius())){
                return planet;
            }
        }
        for(GUI gui:GUIElements){   //If GUI is clicked that would need the planet selected to be kept (delete/modify planets)
            if(gui instanceof TextBoxGUI){
                TextBoxGUI tBox=(TextBoxGUI) gui;
                if(tBox.pointInRectangle(new Vector2f(mouseScreenPos[0]-resolution.x/2,mouseScreenPos[1]*-1+resolution.y/2))){
                    return planetSelected;
                }
            }else if(gui==deletePlanetButton){
                ButtonGUI btn=(ButtonGUI) gui;
                if(btn.getBG().pointInRectangle(new Vector2f(mouseScreenPos[0]-resolution.x/2,mouseScreenPos[1]*-1+resolution.y/2))){
                    return planetSelected;
                }
            }
        }
        return null;
    }

    private float validateText(String string, boolean signed){  //Any number larger the 1e38 is considered as Infinity
        boolean firstOfNumber=signed;
        boolean decimalFound=false;
        boolean eFound=false;
        boolean mantissaFound=false;
        boolean exponentFound=false;
        StringBuilder str= new StringBuilder(); //Makes constructing a string easier then using concatenation

        for(char c:string.toCharArray()){  //Removes any duplicate decimal places
            if(c=='-'){
                if(firstOfNumber){
                    str.append(c);
                    firstOfNumber=false;    //Ensures negative sign can only be first character of mantissa or exponent
                }
            }else if(c=='.'){
                if(!decimalFound){  //Ensures decimal points don't repeat or appear in the magnitude
                    str.append(c);
                    decimalFound=true;
                    firstOfNumber=false;
                }
            }else if(c=='E'){
                if(!eFound){    //Ensures only one E appears, and no decimal points appear after it
                    str.append(c);
                    decimalFound=true;
                    eFound=true;
                    firstOfNumber=true; //Allows the exponent to have a negative sign too
                }
            }else{  //Any number
                str.append(c);
                firstOfNumber=false;
                if(eFound){ //Ensures number has both an exponent and a mantissa
                    exponentFound=true;
                }else{
                    mantissaFound=true;
                }
            }

        }
        if(str.length()==0||!mantissaFound||(eFound&&!exponentFound)){
            return 0;
        }

        return clamp(Float.parseFloat(str.toString()),-1E38f, 1E38f);    //Converts StringBuilder to a float, also clamps value
    }

    private float clamp(float val, float min, float max){   //ensures value is within the bounds of min & max, assumes min is smaller then max
        return Math.min(Math.max(val,min),max);
    }

    private void updateGUI() throws Exception {
        planetPosX.setText(Float.toString(planetSelected.getPosition().x));
        planetPosY.setText(Float.toString(planetSelected.getPosition().y));
        planetVelX.setText(Float.toString(planetSelected.getVelocity().x));
        planetVelY.setText(Float.toString(planetSelected.getVelocity().y));
        planetMass.setText(Float.toString(planetSelected.getMass()));
        planetSize.setText(Float.toString(planetSelected.getRadius()));
        planetColR.setText(Float.toString(planetSelected.getColour().x));
        planetColG.setText(Float.toString(planetSelected.getColour().y));
        planetColB.setText(Float.toString(planetSelected.getColour().z));
    }

    private void updatePlanet(Planet planetSelected, TextBoxGUI textbox) throws Exception {   //Takes the value from the text box and set's the relevant planet information to it
        float valueSigned=validateText(textbox.toString(),true);
        float valueUnSigned=Math.abs(valueSigned);

        if(textbox==planetPosX){
            float y=planetSelected.getPosition().y;
            planetSelected.setPosition(new Vector2f(valueSigned,y));
        }else if(textbox==planetPosY){
            float x=planetSelected.getPosition().x;
            planetSelected.setPosition(new Vector2f(x,valueSigned));
        }else if(textbox==planetVelX){
            float y=planetSelected.getVelocity().y;
            planetSelected.setVelocity(new Vector2f(valueSigned,y));
        }else if(textbox==planetVelY){
            float x=planetSelected.getVelocity().x;
            planetSelected.setVelocity(new Vector2f(x,valueSigned));
        }else if(textbox==planetMass){
            planetSelected.setMass(Math.max(valueUnSigned,0.001f));
        }else if(textbox==planetSize){
            planetSelected.setRadius(Math.max(valueUnSigned,0.01f));
        }else if(textbox==planetColR){
            Vector3f colour=planetSelected.getColour().setComponent(0,clamp(valueUnSigned,0,1));
            planetSelected.setColour(colour);
        }else if(textbox==planetColG){
            Vector3f colour=planetSelected.getColour().setComponent(1,clamp(valueUnSigned,0,1));
            planetSelected.setColour(colour);
        }else if(textbox==planetColB) {
            Vector3f colour = planetSelected.getColour().setComponent(2, clamp(valueUnSigned, 0, 1));
            planetSelected.setColour(colour);
        }
        updateGUI();
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

    public void update(float updateRate, Camera camera, Input input, Vector2f resolution) throws Exception {
        updateInputGUI(resolution);
        boolean guiSelected=false;
        int i=0;

        if(input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)){
            planetSelected=getPlanetSelected(input,camera,resolution);  //Runs when the mouse is clicked
            if(planetSelected!=null){   //Runs the frame the planet is selected
                if(planetSelectedLF!=planetSelected){   //Ensures planet information is updated when necessary
                    updateGUI();
                    planetSelectedLF=planetSelected;
                }
            }else{
                planetSelectedLF=null;
            }
        }

        addPlanetButton.use(resolution);
        deletePlanetButton.use(resolution);
        boolean GUISelected=false;
        if(planetSelected!=null){   //Runs when a planet is selected
            for(GUI gui:GUIElements){
                if(gui instanceof TextBoxGUI){  //Iterates through text boxes
                    TextBoxGUI tGui=(TextBoxGUI) gui;
                    if(tGui==simGravConst){ //Ignores gravity constant, since that will be managed separately
                        continue;
                    }
                    if(tGui.getSelected()){    //If GUI is selected
                        GUISelected=true;
                        if(tGui!=GUISelectedLF&&GUISelectedLF!=null){   //Updates information if user changes textbox that is selected
                            updatePlanet(planetSelected,GUISelectedLF);
                        }
                        GUISelectedLF=tGui;
                    }
                }
            }
            if(!GUISelected){   //If no GUI is selected
                if(GUISelectedLF!=null){    //Updates GUI when user deselects GUI
                    updatePlanet(planetSelected,GUISelectedLF);
                }
                GUISelectedLF=null;
            }
        }else{  //Deselects GUI if planet is deselected
            GUISelectedLF=null;
        }
        if(!simGravConst.getSelected()){

            gravityConstant=round(validateText(simGravConst.toString(),false),3);
            simGravConst.setText(Float.toString(gravityConstant));
        }


        if(!GUISelected){
            camera.control(input,3,0.8f,updateRate);
        }
        planetSimulatorButton.use(resolution);
    }

    public void render(Vector2f screenSize, Camera camera){
        program.useProgram();
        program.setUniform("resolution", screenSize);
        program.setUniform("viewMatrix",camera.getViewMatrix());
        for(Planet planet:planets){
            planet.render(program,camera);
        }
        GUIElements.get(17).render(screenSize);
        for(int i=0;i<GUIElements.size();i++){
            switch(i){
                case 0: //All GUI in the planet modifier screen
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 20:
                case 21:
                case 24:
                case 25:
                case 26:
                case 27:
                    if(planetSelected!=null){
                        GUIElements.get(i).render(screenSize);
                    }
                    break;
                case 17:    //Backgrounds need to be rendered first
                    break;
                default:
                    GUIElements.get(i).render(screenSize);
            }
        }
    }

    public ArrayList<Planet> getPlanets(){
        return new ArrayList<>(planets);
    }

    public void closePage(){
        program.cleanup();
        for(Planet planet: planets){
            planet.cleanup();
        }
    }
}
