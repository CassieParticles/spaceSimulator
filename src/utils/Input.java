package utils;

import org.lwjgl.glfw.GLFW;

public class Input {
    private final boolean[] keys=new boolean[GLFW.GLFW_KEY_LAST];
    private final boolean[] keysAllreadyPressed=new boolean[GLFW.GLFW_KEY_LAST];
    private final boolean[] mouseButtons=new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private final boolean[] mouseButtonsAllreadyPressed=new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

    private int mouseX;
    private int mouseY;

    public Input(){

    }

    public void init(long windowHandle){

        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> keys[key]=(action == GLFW.GLFW_PRESS||action==GLFW.GLFW_REPEAT));
        GLFW.glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> mouseButtons[button]=(action== GLFW.GLFW_PRESS||action==GLFW.GLFW_REPEAT));
        GLFW.glfwSetCursorPosCallback(windowHandle, (window,xPos,yPos) -> {
            mouseX=(int)xPos;
            mouseY=(int)yPos;
        });
    }

    public void updateInputs(){
        System.arraycopy(keys, 0, keysAllreadyPressed, 0, keys.length);
        System.arraycopy(mouseButtons,0,mouseButtonsAllreadyPressed,0,mouseButtons.length);
    }

    public boolean isKeyDown(int key){
        return keys[key];
    }

    public boolean isKeyPressed(int key){
        return keys[key]&&!keysAllreadyPressed[key];
    }

    public boolean isMouseButtonPressed(int mouseButton){
        return mouseButtons[mouseButton]&&!mouseButtonsAllreadyPressed[mouseButton];
    }

    public int[] getMousePos(){
        return new int[]{mouseX,mouseY};
    }
}
