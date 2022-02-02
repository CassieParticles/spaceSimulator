package utils;

public class Timer {
    private double updateTime;
    private double frameTime;

    private double lastFrame;
    private double lastUpdate;
    private double timeRunning;
    private double lastTick;

    private boolean frame;
    private boolean update;

    public Timer(float UPS, float FPS){
        updateTime=1/UPS;
        frameTime=1/FPS;

        lastTick=System.currentTimeMillis();
    }



    public void update(){
        long currentTime=System.currentTimeMillis();
        timeRunning+=(currentTime-lastTick)/1000.0;
        lastTick=currentTime;

        frame=timeRunning-lastFrame>frameTime;
        update=timeRunning-lastUpdate>updateTime;

        if(frame){
            lastFrame=timeRunning;
        }
        if(update){
            lastUpdate=timeRunning;
        }
    }

    public void setUPS(float UPS){
        updateTime=1/UPS;
    }

    public void setFPS(float FPS){
        frameTime=1/FPS;
    }

    public boolean getFrame(){
        return frame;
    }

    public boolean getUpdate(){
        return update;
    }

    public double getCurrentTime(){
        return timeRunning;
    }

    public double getDeltaFrame(){
        return frameTime;
    }

    public double getDeltaUpdate(){
        return updateTime;
    }
}

