
package com.selcukcihan.android.xface;


import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.imageio.ImageIO;
import javax.microedition.khronos.opengles.GL11;

public class GLTest implements GLEventListener
{
	private IntBuffer m_texName = IntBuffer.allocate(1);
	
	private int m_displayList;
	private float [][] m_textureCoordinates = {
			{0, 0},
			{1, 0},
			{1, 1},
			{0, 1}};
	private float [][] m_points = {
			{-1, -1, 1},
			{1, -1, 1},
			{1, 1, 1},
			{-1, 1, 1},
			{-1, -1, -1},
			{1, -1, -1},
			{1, 1, -1},
			{-1, 1, -1}};
	private int [][] m_faces = {
			{0, 1, 2, 3},
			{4, 7, 6, 5},
			{3, 2, 6, 7},
			{0, 4, 5, 1},
			{1, 5, 6, 2},
			{0, 3, 7, 4}};
	
	private float [][] m_faceColors = {
			{0, 1, 0},
			{0, 0, 1},
			{0, 1, 1},
			{1, 0, 0},
			{1, 0, 1},
			{1, 1, 0}};
	
	public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslated(0, 0, -45);
		gl.glRotated(45, 0, 1, 0);
		gl.glRotated(45, 1, 0, 0);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		//gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		float [] envcolor = {1,1,1,1};
		gl.glTexEnvfv(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_COLOR, envcolor, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, m_texName.get(0));
		gl.glCallList(m_displayList);
		gl.glFlush();
		gl.glDisable(GL.GL_TEXTURE_2D);
	}
		
	protected void drawCube(GL gl)
	{
		gl.glColor4f(1,1,1,1);
		for(int i = 0; i<6; i++)
		{
			//gl.glColor3fv(m_faceColors[i],0);
			gl.glBegin(GL.GL_POLYGON);
			for(int j = 0; j<4; j++)
			{
				gl.glTexCoord2fv(m_textureCoordinates[j], 0);
				gl.glVertex3fv(m_points[m_faces[i][j]], 0);
			}
			gl.glEnd();
		}
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {

	}
	
	public void printout(int val)
	{
		//for(int i = 0; i<32; i++)
		{
			System.out.println((byte)((val>>16)&0x000000FF));
			System.out.println((byte)((val>>8)&0x000000FF));
			System.out.println((byte)((val)&0x000000FF));
		}
		System.out.println("");
	}

	public void init(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		gl.glClearDepth(1.0f);
		//gl.glEnable(GL.GL_DEPTH_TEST);
		//gl.glDepthFunc(GL.GL_LEQUAL);
		//gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
		gl.glGenTextures(1, m_texName);
		gl.glBindTexture(GL.GL_TEXTURE_2D, m_texName.get(0));
		
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);

		
		File file = new File("C:\\Users\\selcuk\\Desktop\\marble.bmp");
		//File file = new File("C:\\Users\\selcuk\\Desktop\\MARBLE8.png");
		//File file = new File("C:\\Users\\selcuk\\Desktop\\resim1.png");
		BufferedImage bufferedImage = null;
		
		try
		{
			bufferedImage = ImageIO.read(file);
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			
			IntBuffer data = IntBuffer.allocate(width*height);
			for(int i = 0; i<height; i++)
			{
				int pixel;
				for(int j = 0; j<width; j++)
				{
					pixel = bufferedImage.getRGB(j, i);
					data.put((int)((pixel<<8)|0xFF));
				}
			}
			data.rewind();
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height,
					0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		
		gl.glEnable(GL.GL_CULL_FACE);
		//gl.glEnable(GL.GL_LIGHTING);
		//gl.glEnable(GL.GL_LIGHT0);
		gl.glCullFace(GL.GL_BACK);
		
		m_displayList = gl.glGenLists(1);
		gl.glNewList(m_displayList, GL.GL_COMPILE);
		drawCube(gl);
		gl.glEndList();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		final GL gl = drawable.getGL();
		// final GLU glu = new GLU();
		gl.setSwapInterval(1);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glFrustum(-2, 2, -2, 2, 40, 50);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

}
