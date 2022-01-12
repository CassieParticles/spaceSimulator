package rendering;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class CircleMesh{

    private int vaoId;
    private int radiusBufferId;

    public CircleMesh(float radius){
        FloatBuffer radiusBuffer=null;

        try{
            vaoId= GL46.glGenVertexArrays();
            GL46.glBindVertexArray(vaoId);

            radiusBufferId=GL46.glGenBuffers(); //Create the buffer containing the radius
            radiusBuffer= MemoryUtil.memAllocFloat(1);
            radiusBuffer.put(radius).flip();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER,radiusBufferId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER,radiusBuffer,GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(0);
            GL46.glVertexAttribPointer(0,1,GL46.GL_FLOAT,false,0,0);

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER,0);
            GL46.glBindVertexArray(0);
        }finally{
            if (radiusBuffer != null) {
                MemoryUtil.memFree(radiusBuffer);
            }
        }
    }

    public void render(){
        GL46.glBindVertexArray(vaoId);
        GL46.glEnableVertexAttribArray(0);

        GL46.glDrawArrays(GL46.GL_POINTS,0,1);

        GL46.glDisableVertexAttribArray(0);
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
