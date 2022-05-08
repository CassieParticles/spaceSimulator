package rendering;

import gameObjects.Camera;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {

    protected int vaoId;
    protected int vertexVboId;
    protected int indexVboId;

    protected int vertexCount;

    public Mesh(float[] vertices, int[] indices){
        vertexCount=indices.length;

        FloatBuffer verticesBuffer=null;
        IntBuffer indicesBuffer=null;

        try{
            vaoId= GL46.glGenVertexArrays();
            GL46.glBindVertexArray(vaoId);

            vertexVboId=GL46.glGenBuffers();
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, verticesBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(0);
            GL46.glVertexAttribPointer(0, 2, GL46.GL_FLOAT, false, 0, 0);

            indexVboId=GL46.glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);


        }finally{
            if(verticesBuffer!=null){
                MemoryUtil.memFree(verticesBuffer);
            }if(indicesBuffer!=null){
                MemoryUtil.memFree(indicesBuffer);
            }
        }
        GL46.glBindVertexArray(0);
    }


    public void render(Program program, Camera camera){
        program.useProgram();

        GL46.glBindVertexArray(getVaoId());

        GL46.glEnableVertexAttribArray(0);

        GL46.glDrawElements(GL46.GL_TRIANGLES, getVertexCount(), GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);

        GL46.glBindVertexArray(0);

        program.unlinkProgram();
    }

    public void cleanup(){
        GL46.glDisableVertexAttribArray(0);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glDeleteBuffers(vertexVboId);
        GL46.glDeleteBuffers(indexVboId);

        // Delete the VAO
        GL46.glBindVertexArray(0);
        GL46.glDeleteVertexArrays(vaoId);
    }

    public int getVertexCount(){
        return vertexCount;
    }

    public int getVaoId(){
        return vaoId;
    }

}
