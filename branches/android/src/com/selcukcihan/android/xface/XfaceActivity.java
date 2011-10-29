package com.selcukcihan.android.xface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.selcukcihan.android.xface.xfaceapp.ModelCamera;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class XfaceActivity extends Activity {
	XFaceApplication m_pApp;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GLSurfaceView view = new GLSurfaceView(this);
        OpenGL11Renderer renderer = new OpenGL11Renderer(view);
        view.setRenderer(renderer);
        setContentView(view);
        //m_pApp.
    }
}

class OpenGL11Renderer implements GLSurfaceView.Renderer {
	XFaceApplication m_pApp;
	ModelCamera m_pCamera;
	GLSurfaceView m_canvas;
	
	public OpenGL11Renderer(GLSurfaceView p_canvas)
	{
		m_canvas = p_canvas;
	}
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
		gl.glShadeModel(GL11.GL_SMOOTH);
		gl.glCullFace(GL11.GL_BACK);
		gl.glFrontFace(GL11.GL_CCW);
		gl.glEnable(GL11.GL_CULL_FACE);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL11.GL_DEPTH_TEST);
		gl.glEnable(GL11.GL_LIGHTING);
		gl.glEnable(GL11.GL_LIGHT0);
		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL11.GL_MODELVIEW);

		m_pApp = new XFaceApplication(m_canvas);
		m_pCamera = new ModelCamera();
		
		m_pApp.onLoadFDP("alice.fdp", "face\\", (GL11)gl);
		m_pCamera.setAxisAngle(m_pApp.m_pFace.getFDP().getGlobalAxisAngle(), (GL11)gl);
		m_pCamera.setTranslation(m_pApp.m_pFace.getFDP().getGlobalTranslation());
		
		m_pApp.onLoadPHO("face\\say-suffering-angina.pho", "english");
		
		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}
