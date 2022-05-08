package main;

import gameObjects.Camera;
import gameObjects.Planet;
import sections.RunningSimulator.RunSimMain;
import sections.buildingSimulator.BuildSimMain;
import utils.Input;
import utils.Timer;

import java.util.ArrayList;

public class PageManager {
	
	private static RunSimMain runSim;
	private static BuildSimMain buildSim;
	
	private static Page currentPage;
	
	private static Timer timer;
	private static Camera camera;
	private static Window window;
	private static Input input;
	
	public static void init(Timer timerIn, Camera cameraIn, Input inputIn, Window windowIn){
		runSim=new RunSimMain();
		buildSim=new BuildSimMain();
		camera=cameraIn;
		timer=timerIn;
		input=inputIn;
		window=windowIn;
		currentPage=Page.NULL;
	}
	
	private static void cleanupOldPage(){
		switch(currentPage){
		case RunSim:
			runSim.closePage();
			break;
		case BuildSim:
			buildSim.closePage();
			break;
		}

		
		currentPage=Page.NULL;
	}
	
	public static void loadRunSim(ArrayList<Planet> planets, float gravConst) throws Exception{
		if(currentPage==Page.RunSim){
			return;
		}
		cleanupOldPage();
		runSim.init(planets,GUIHandler.testSimGUI, gravConst,timer,camera);
		currentPage=Page.RunSim;
	}

	public static void loadBuildSim(ArrayList<Planet> planets, float gravConst) throws Exception{
		if(currentPage==Page.BuildSim){
			return;
		}

		cleanupOldPage();
		if(planets==null){
			buildSim.init(GUIHandler.buildSimGUI,gravConst, camera);
		}else{
			buildSim.init(planets,GUIHandler.buildSimGUI,gravConst, camera);
		}
		currentPage=Page.BuildSim;
	}
	
	public static void render() throws Exception{
		switch(currentPage){
		case RunSim:
			runSim.render(window.getResolution(),camera);
			break;
		case BuildSim:
			buildSim.render(window.getResolution(),camera);
		}
	}

	public static void update() throws Exception{
		switch(currentPage){
			case RunSim:
				runSim.update((float) timer.getDeltaUpdate(),camera, input,window.getResolution());
				break;
			case BuildSim:
				buildSim.update((float)timer.getDeltaUpdate(),camera,input,window.getResolution());
				break;
		}
	}
	
	public static void cleanup() {
		switch(currentPage){
			case RunSim:
				runSim.closePage();
			case BuildSim:
				buildSim.closePage();
		}
	}
}
