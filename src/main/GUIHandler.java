package main;

import guiObjects.GUI;
import guiObjects.RectangleGUI;
import guiObjects.TextGUI;
import guiObjects.TextureGUI;
import guiObjects.buttons.ButtonGUI;
import guiObjects.buttons.TextBoxGUI;
import guiObjects.buttons.ToggleButtonGUI;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.Input;

import java.util.ArrayList;

public class GUIHandler {
    public static ArrayList<GUI> testSimGUI =new ArrayList<>();
    public static ArrayList<GUI> buildSimGUI=new ArrayList<>();

    private static final String fontDir="resources/slabo.png";
    private static final String fontCSVDir="resources/slaboData.csv";

    public static void init(Input input) throws Exception {
        testSimGUI.add(new RectangleGUI(new Vector2f(-420,-400),new Vector2f(300,400),new Vector3f(0.6f)));
        testSimGUI.add(new TextGUI(new Vector2f(-400,-50),new Vector2f(1,1),"Position: ",10,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-300,-50),new Vector2f(0.9f,1),"",25,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-400,-100),new Vector2f(1,1),"Velocity: ",10,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-300,-100),new Vector2f(0.9f,1),"",25,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-400,-150),new Vector2f(1,1),"Mass: ",10,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-300,-150),new Vector2f(1,1),"",20,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-400,-200),new Vector2f(1,1),"Size: ",20,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-300,-200),new Vector2f(1,1),"",10,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextBoxGUI(new Vector2f(0,355),new Vector2f(200,30),new Vector3f(0.4f),new Vector3f(0.6f),input));
        //Item 10
        testSimGUI.add(new TextGUI(new Vector2f(250,-380),new Vector2f(1,1),"Gravity constant: ",20,250,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(320,-405),new Vector2f(1,1),"",10,150,fontDir,fontCSVDir));
        testSimGUI.add(new RectangleGUI(new Vector2f(240,-410),new Vector2f(200,70),new Vector3f(0.6f)));
        testSimGUI.add(new RectangleGUI(new Vector2f(-120,350),new Vector2f(330,40),new Vector3f(0.3f)));
        testSimGUI.add(new TextGUI(new Vector2f(-110,355),new Vector2f(1,1),"Timestep:",10,150,fontDir,fontCSVDir));
        testSimGUI.add(new ToggleButtonGUI(new Vector2f(-5,390),new Vector2f(100,30),"resources/GUI/pauseButton.png","resources/GUI/playButton.png",input));
        testSimGUI.add(new ButtonGUI(new Vector2f(-120,390),new Vector2f(115,30),new Vector3f(0.3f),input));
        testSimGUI.add(new TextureGUI(new Vector2f(-5,390),new Vector2f(-115,30),"resources/GUI/speedButton.png"));
        testSimGUI.add(new ButtonGUI(new Vector2f(95,390),new Vector2f(115,30),new Vector3f(0.3f),input));
        testSimGUI.add(new TextureGUI(new Vector2f(95,390),new Vector2f(115,30),"resources/GUI/speedButton.png"));
        //Item 20
        testSimGUI.add(new ButtonGUI(new Vector2f(-410,-260),new Vector2f(250,30),new Vector3f(0.3f),input));
        testSimGUI.add(new TextGUI(new Vector2f(-400,-260),new Vector2f(1,1),"Follow planet",15,150,fontDir,fontCSVDir));
        testSimGUI.add(new TextGUI(new Vector2f(-400,-260),new Vector2f(1,1),"Stop following planet",21,220,fontDir,fontCSVDir));
        testSimGUI.add(new ButtonGUI(new Vector2f(240,360),new Vector2f(200,50),new Vector3f(0.6f),input));
        testSimGUI.add(new TextGUI(new Vector2f(245,370),new Vector2f(1,1),"Simulation builder",18,190,fontDir,fontCSVDir));


        buildSimGUI.add(new RectangleGUI(new Vector2f(-420,-400),new Vector2f(350,400),new Vector3f(0.6f)));
        buildSimGUI.add(new TextGUI(new Vector2f(-400,-50),new Vector2f(1,1),"Position: ",10,150,fontDir,fontCSVDir));
        buildSimGUI.add(new TextGUI(new Vector2f(-400,-100),new Vector2f(1,1),"Velocity: ",10,150,fontDir,fontCSVDir));
        buildSimGUI.add(new TextGUI(new Vector2f(-400,-150),new Vector2f(1,1),"Mass: ",10,150,fontDir,fontCSVDir));
        buildSimGUI.add(new TextGUI(new Vector2f(-400,-200),new Vector2f(1,1),"Size: ",20,150,fontDir,fontCSVDir));
        buildSimGUI.add(new TextGUI(new Vector2f(-400,-250),new Vector2f(1,1),"Colour: ",20,150,fontDir,fontCSVDir));
        buildSimGUI.add(new TextGUI(new Vector2f(250,-320),new Vector2f(1,1),"Gravity constant: ",20,250,fontDir,fontCSVDir));
        //Item 7
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-300,-50),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));   //2 position 7
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-190,-50),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-300,-100),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));  //2 veloccity
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-190,-100),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-300,-150),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));  //1 Mass
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-300,-200),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));  //1 Size
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-300,-250),new Vector2f(65,30),new Vector3f(0.2f),new Vector3f(0.4f),input));  //3 Colour
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-225,-250),new Vector2f(65,30),new Vector3f(0.2f),new Vector3f(0.4f),input));
        buildSimGUI.add(new TextBoxGUI(new Vector2f(-150,-250),new Vector2f(65,30),new Vector3f(0.2f),new Vector3f(0.4f),input));
        buildSimGUI.add(new TextBoxGUI(new Vector2f(250,-345),new Vector2f(100,30),new Vector3f(0.2f),new Vector3f(0.4f),input));    //1 gravity constant
        //Item 17
        buildSimGUI.add(new RectangleGUI(new Vector2f(240,-350),new Vector2f(200,60),new Vector3f(0.6f)));

        buildSimGUI.add(new ButtonGUI(new Vector2f(240,-410),new Vector2f(200,50),new Vector3f(0.6f),input));
        buildSimGUI.add(new TextGUI(new Vector2f(250,-405),new Vector2f(1,1),"Add planet",10,150,fontDir,fontCSVDir));
        //Item 20
        buildSimGUI.add(new ButtonGUI(new Vector2f(240,-280),new Vector2f(200,50),new Vector3f(0.6f),input));
        buildSimGUI.add(new TextGUI(new Vector2f(250,-275),new Vector2f(1,1),"Delete planet",13,150,fontDir,fontCSVDir));

        buildSimGUI.add(new ButtonGUI(new Vector2f(240,360),new Vector2f(200,50),new Vector3f(0.6f),input));
        buildSimGUI.add(new TextGUI(new Vector2f(250,370),new Vector2f(1,1),"Planet simulator",16,180,fontDir,fontCSVDir));
    }
    
    public static void cleanup(){
    	for(GUI gui:testSimGUI){
    		gui.cleanup();
    	}
        for(GUI gui:buildSimGUI){
            gui.cleanup();
        }
    }
}
