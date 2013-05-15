package com.example.Origami4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-5-3
 * Time: 下午6:48
 * To change this template use File | Settings | File Templates.
 */
public class DemoView extends GLSurfaceView implements GLSurfaceView.Renderer {

    //投影矩阵
    private float[] projectionMatrix = new float[16];

    //视图矩阵
    private float[] viewMatrix = new float[16];

    //模型矩阵
    private float[] modelMatrix = new float[16];

    //模型视图投影矩阵
    private float[] mvpMatrix = new float[16];

    private TextureMesh textureMesh;

    public DemoView(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        this.setEGLContextClientVersion(2);

        this.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);

        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0, 0, 0, 0);

        //照相机位置
        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 9f;

        //照相机拍照方向
        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = -5.0f;

        //照相机的垂直方向
        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        textureMesh = new TextureMesh(getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        float left = -ratio;
        float right = ratio;
        float top = 1;
        float bottom = -1;
        float near = 8.5f;
        float far = 10;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(modelMatrix, 0);

        Bitmap bitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.a)).getBitmap().copy(Bitmap.Config.ARGB_8888,false);
        textureMesh.setTexture(bitmap);

        textureMesh.setVertexes(new Vertex[]{
                new Vertex(-0.5f, 0.5f, 0,1),
                new Vertex(-0.5f, -0.5f, 0,1),
                new Vertex(0.5f, 0.5f, 0,1),
                new Vertex(0.5f, -0.5f, 0,1)
        });

        Matrix.rotateM(modelMatrix, 0, 80, 1.0f, 0.0f, 0.0f);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

        textureMesh.draw(mvpMatrix);
    }
}
