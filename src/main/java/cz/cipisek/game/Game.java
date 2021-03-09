package cz.cipisek.game;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Game {

    private static final float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };
    private static final float[] colors = {
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f,
    };

    private static final int[] indices = {
            0, 1, 3,
            0, 2, 3
    };

    private static int vaoId;
    private static int vboId;
    private static double increase = 0.001;
    private static int eboId;
    private static int colorId;

    public static void init(long window) {
        // Setup shaders
        Shaders.initShaders();

        // Generate all the ids
        vaoId = GL33.glGenVertexArrays();
        vboId = GL33.glGenBuffers();
        eboId = GL33.glGenBuffers();
        colorId = GL33.glGenBuffers();


        // Tell OpenGL we are currently using this object (vaoId)
        GL33.glBindVertexArray(vaoId);
        // Tell OpenGL we are currently writing to this buffer (vboId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(fb);
    }

    public static void render(long window) {
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glBindVertexArray(vaoId);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, vertices.length / 3);
    }

    public static void update(long window) {
        GL33.glBindVertexArray(vaoId);
        {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vaoId);
            vertices[0] += increase;
            vertices[3] += increase;
            vertices[6] += increase;
            vertices[9] += increase;
            vertices[15] += increase;
            vertices[12] += increase;

            FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                    .put(vertices)
                    .flip();
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        }


    }

}
