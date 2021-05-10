package cz.cipisek.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {

        String input = "-0.19999999;-1.0;0.4\n" +
                "0.20000005;-1.0;0.4\n" +
                "0.6;-1.0;0.4\n" +
                "0.6;-0.6;0.4\n" +
                "-1.0;-0.19999999;0.4\n" +
                "-0.6;-0.19999999;0.4\n" +
                "-0.19999999;-0.19999999;0.4\n" +
                "0.6;-0.19999999;0.4\n" +
                "-1.0;0.20000005;0.4\n" +
                "-1.0;0.6;0.4\n" +
                "-0.6;0.6;0.4\n" +
                "-0.19999999;0.6;0.4\n";

        String[] maze = input.split("\n");

        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);

        long window = GLFW.glfwCreateWindow(800, 600, "WindowInWindow", 0, 0);

        if (window == 0) {

            GLFW.glfwTerminate();
            throw new Exception("Nefacha-to");

        }

        GLFW.glfwMakeContextCurrent(window);

        GL.createCapabilities();

        GL33.glViewport(0, 0, 800, 600);

        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {

            GL33.glViewport(0, 0, w, h);

        });

        Shader.initShaders();

        ArrayList<Square> squares = new ArrayList<>();

        for (int i = 0; i < maze.length; i++) {

            String[] position = maze[i].split(";");

            Square square = new Square(

                    Float.parseFloat(position[0]),
                    Float.parseFloat(position[1]),
                    Float.parseFloat(position[2]));

            squares.add(square);
        }

        Square MySquare = new Square(0f, 0f, 0.25f);


        while (!GLFW.glfwWindowShouldClose(window)) {

            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)

                GLFW.glfwSetWindowShouldClose(window, true);

            GL33.glClearColor(0f, 0f, 0f, 1f);

            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            for (int i = 0; i < squares.size(); i++) {

                squares.get(i).render();
            }

            boolean kolize = false;

            for (int i = 0; i < squares.size(); i++) {

                if (didCollide(MySquare, squares.get(i))) {

                    kolize = true;
                }
            }

            MySquare.update(window);

            MySquare.render();

            if (kolize) {

                MySquare.changeColorToRed();

            } else {

                MySquare.changeColorToGreen();

            }

            GLFW.glfwSwapBuffers(window);

            GLFW.glfwPollEvents();
        }
        GLFW.glfwTerminate();
    }

    public static boolean didCollide(Square a, Square b) {

        return (a.getX() + a.getZ() > b.getX() && a.getX() < b.getX() + b.getZ() && a.getY() + a.getZ() / 2 + a.getZ() > b.getY() && a.getY() + a.getZ() / 2 < b.getY() + b.getZ());

    }
}