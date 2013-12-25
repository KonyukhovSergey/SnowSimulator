package ru.jauseg.snowpaper;

import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;
import android.util.FloatMath;

import js.engine.BufferAllocator;
import js.math.Vector2D;

public class SnakeLine
{
	private float[] widths;
	private Vector2D[] points;

	private Vector2D n1 = new Vector2D();
	private Vector2D n2 = new Vector2D();
	private Vector2D nr = new Vector2D();

	private FloatBuffer vertexes;
	private FloatBuffer textures;

	public SnakeLine(int count, float[] textureCoords)
	{
		points = new Vector2D[count];
		widths = new float[count];

		Random rnd = new Random(SystemClock.elapsedRealtime());

		for (int i = 0; i < count; i++)
		{
			float a = (float) i / (float) count;
			points[i] = new Vector2D(400 + 250 * FloatMath.cos(a * 5.0f), 500 + 400 * FloatMath.sin(a * 5.0f));
			// points[i] = new Vector2D(30 * i + 20, 30 * i + 20);
			widths[i] = 100.0f;// + rnd.nextFloat() * 40.0f;
		}

		fillTextures(textureCoords);

		vertexes = BufferAllocator.createFloatBuffer(count * 2 * 2);
	}

	private void fillTextures(float[] coords)
	{
		textures = BufferAllocator.createFloatBuffer(points.length * 2 * 2);

		// 0 1 2 3
		// x1 y1 x2 y2

		Vector2D p1 = new Vector2D(coords[0], coords[1]);
		Vector2D p2 = new Vector2D(coords[2], coords[1]);
		Vector2D p3 = new Vector2D(coords[0], coords[3]);
		Vector2D p4 = new Vector2D(coords[2], coords[3]);

		Vector2D p = new Vector2D();

		for (int i = 0; i < points.length; i++)
		{
			float v = ((float) i) / ((float) points.length);

			p.interpolate(p1, p3, v);
			textures.put(p.x);
			textures.put(p.y);

			p.interpolate(p2, p4, v);
			textures.put(p.x);
			textures.put(p.y);
		}

		textures.position(0);
	}

	private void fillVertexes()
	{
		if (points.length < 2)
		{
			return;
		}

		Vector2D p;

		p = points[0];

		n1.minus(points[1], p);
		n1.set(n1.y, -n1.x);
		n1.normalize();

		vertexes.put(p.x - n1.x * widths[0]);
		vertexes.put(p.y - n1.y * widths[0]);
		vertexes.put(p.x + n1.x * widths[0]);
		vertexes.put(p.y + n1.y * widths[0]);

		for (int i = 1; i < points.length - 1; i++)
		{
			p = points[i];

			n2.minus(points[i + 1], p);
			n2.set(n2.y, -n2.x);
			n2.normalize();

			nr.plus(n1, n2);
			// nr.normalize();
			nr.scale(widths[i] * 0.5f);

			vertexes.put(p.x - nr.x);
			vertexes.put(p.y - nr.y);
			vertexes.put(p.x + nr.x);
			vertexes.put(p.y + nr.y);

			n1.set(n2);
		}

		p = points[points.length - 1];
		n2.scale(widths[points.length - 1]);

		vertexes.put(p.x - n2.x);
		vertexes.put(p.y - n2.y);
		vertexes.put(p.x + n2.x);
		vertexes.put(p.y + n2.y);

		vertexes.position(0);
	}

	public void setPoint(int index, float x, float y, float w)
	{
		points[index].set(x, y);
		widths[index] = w;
	}

	public void draw(GL10 gl)
	{
		fillVertexes();
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexes);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textures);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, points.length * 2);
	}
}
