package ru.jauseg.snowpaper;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import js.data.IndexerContainer;
import js.engine.BufferAllocator;
import js.engine.TextureManager;
import js.jni.code.NativeCalls;

public class SnowSystemNative
{
	private static final String TAG = "SnowSystem";

	private float width;

	private FloatBuffer vertexCoords;
	private FloatBuffer textureCoords;
	private ByteBuffer vertexColors;
	private FloatBuffer texBase;

	public SnowSystemNative(float width, float height, TextureManager textures)
	{
		this.width = (int) width;
		this.vertexCoords = app.vertexCoords;
		this.textureCoords = app.textureCoords;
		this.vertexColors = app.vertexColors;
		this.texBase = app.texBase;

		init(width, height);

		FloatBuffer t = textures.getCoordsBuffer(1);

		texBase.position(0);
		texBase.put(t.get(0));
		texBase.put(t.get(1));
		texBase.put(t.get(2));
		texBase.put(t.get(3));
		texBase.put(t.get(4));
		texBase.put(t.get(5));
		texBase.put(t.get(0));
		texBase.put(t.get(1));
		texBase.put(t.get(4));
		texBase.put(t.get(5));
		texBase.put(t.get(6));
		texBase.put(t.get(7));
		texBase.position(0);
	}

	private synchronized void init(float w, float h)
	{
		Log.v(TAG, "free");
		NativeCalls.ssFree();
		Log.v(TAG, String.format("init: w = %d, h = %d", (int) w, (int) h));
		NativeCalls.ssInit(w, h);
	}

	public int getWidth()
	{
		return (int) width;
	}

	public void draw(GL10 gl, TextureManager textures)
	{
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexCoords);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoords);
		gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, vertexColors);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0,
				3 * NativeCalls.ssDraw(vertexCoords, textureCoords, vertexColors, texBase));

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	public void fling(float px, float py, float vx, float vy)
	{
		float s = app.getTouchSensitivity();
		NativeCalls.ssFling(px, py, vx * s, vy * s);
	}

	public void setBackground(String imageFileName)
	{
		Log.v(TAG, "image = " + imageFileName);
	}

	public void skip()
	{
		NativeCalls.ssSkip();
	}
}