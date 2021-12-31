package rendering;

import org.joml.Vector3f;

public class CircleMesh extends Mesh{

    private Vector3f colour;
    private float radius;

    public CircleMesh(float[] vertices, int[] indices, Vector3f colour, float radius) {
        super(vertices, indices);
        this.colour=colour;
        this.radius=radius;
    }

    @Override
    public void render(Program program, Camera camera) {
        program.setUniform("colour",colour);
        program.setUniform("radius",radius);
        super.render(program,camera);
    }

}
