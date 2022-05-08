package rendering;

import org.lwjgl.opengl.GL46;

import java.nio.FloatBuffer;

public class CircleMesh{

    private int vaoId;
    private int radiusBufferId;

    public CircleMesh(){
        FloatBuffer radiusBuffer=null;

        try{
            vaoId= GL46.glGenVertexArrays();
            GL46.glBindVertexArray(vaoId);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void render(){
        GL46.glBindVertexArray(vaoId);

        GL46.glDrawArrays(GL46.GL_POINTS,0,1);

        GL46.glBindVertexArray(0);
    }

    public void cleanup(){
        GL46.glDisableVertexAttribArray(0);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER,0);
        GL46.glDeleteBuffers(radiusBufferId);

        GL46.glBindVertexArray(0);
        GL46.glDeleteVertexArrays(vaoId);
    }
}
