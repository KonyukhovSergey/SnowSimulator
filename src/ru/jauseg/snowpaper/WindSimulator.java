package ru.jauseg.snowpaper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

import js.math.MathUtils;
import js.math.Vector2D;

public class WindSimulator
{
	private int widthCount;
	private int heigthCount;
	private int borderCount = 2;
	private float cellSize;

	private ByteBuffer lineBuffer;

	private Vector2D[] dirs = new Vector2D[] { new Vector2D(-0.707107f, -0.707107f), new Vector2D(0.0f, -1.0f),
			new Vector2D(0.707107f, -0.707107f), new Vector2D(1.0f, 0.0f), new Vector2D(0.707107f, 0.707107f),
			new Vector2D(0.0f, 1.0f), new Vector2D(-0.707107f, 0.707107f), new Vector2D(-1.0f, 0.0f) };

	private WindCell cells[] = null;

	public WindSimulator(float width, float heigth, float cellSize)
	{
		init(width, heigth, cellSize);
	}

	public WindSimulator(float width, float height)
	{
		float c = 16;
		init(width, height, width > height ? width / c : height / c);
	}

	private void init(float width, float heigth, float cellSize)
	{
		makeBuffer();

		widthCount = (int) (width / cellSize) + 1 + 2 * borderCount;
		heigthCount = (int) (heigth / cellSize) + 1 + 2 * borderCount;

		this.cellSize = cellSize;

		// distribute cells

		cells = new WindCell[widthCount * heigthCount];

		for (int i = 0; i < cells.length; i++)
		{
			cells[i] = new WindCell(new Vector2D(), new Vector2D(cellSize * (float) ((i % widthCount) - borderCount),
					cellSize * (float) ((int) (i / widthCount) - borderCount)));
		}
	}

	public void tick()
	{
		for (int cy = 1; cy < heigthCount - 1; cy++)
		{
			for (int cx = 1; cx < widthCount - 1; cx++)
			{
				int i = ((cy - 1) * widthCount) + (cx - 1);

				WindCell cell = cells[i + 1 + widthCount];

				cell.calc(dirs[0], cells[i], 0.707107f);
				i++;
				cell.calc(dirs[1], cells[i], 1.0f);
				i++;
				cell.calc(dirs[2], cells[i], 0.707107f);
				i += widthCount;
				cell.calc(dirs[3], cells[i], 1.0f);
				i += widthCount;
				cell.calc(dirs[4], cells[i], 0.707107f);
				i--;
				cell.calc(dirs[5], cells[i], 1.0f);
				i--;
				cell.calc(dirs[6], cells[i], 0.707107f);
				i -= widthCount;
				cell.calc(dirs[7], cells[i], 1.0f);
			}
		}

		for (WindCell c : cells)
		{
			c.dir.plus(c.plus);
			c.plus.set(0, 0);
			c.dir.scale(0.32f);
		}
	}

	public void fling(float x, float y, float dx, float dy)
	{
		WindCell cell = cellAt(x, y);
		cell.dir.plus(dx, dy);
	}

	public WindCell cellAt(float x, float y)
	{
		int cx = (int) (x / cellSize) + borderCount;
		int cy = (int) (y / cellSize) + borderCount;

		return cells[cy * widthCount + cx];
	}

	public void calcAt(Vector2D pos, Vector2D dir, float k)
	{
		int cx = (int) (pos.x / cellSize) + borderCount;
		int cy = (int) (pos.y / cellSize) + borderCount;

		float px = (pos.x - (float) (cx - borderCount) * cellSize) / cellSize;
		float py = (pos.y - (float) (cy - borderCount) * cellSize) / cellSize;

		Vector2D v1, v2, v3, v4;

		if (cx < widthCount - 1 && cy < heigthCount - 1 && cx >= 0 && cy >= 0)
		{
			v1 = cells[(cx + 0) + (cy + 0) * widthCount].dir;
			v2 = cells[(cx + 1) + (cy + 0) * widthCount].dir;
			v3 = cells[(cx + 0) + (cy + 1) * widthCount].dir;
			v4 = cells[(cx + 1) + (cy + 1) * widthCount].dir;
			dir.plus(MathUtils.interpolate(v1.x, v2.x, v3.x, v4.x, px, py),
					MathUtils.interpolate(v1.y, v2.y, v3.y, v4.y, px, py), k);
		}
	}

	public void draw(GL10 gl)
	{
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, lineBuffer);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);

		for (WindCell c : cells)
		{
			gl.glColor4f(1, 1, 1, 1);
			line(gl, c.pos.x, c.pos.y, c.pos.x + c.dir.x * 10, c.pos.y + c.dir.y * 10);
			gl.glColor4f(1, 0, 0, 1);
			line(gl, c.pos.x, c.pos.y, c.pos.x + 2, c.pos.y);
		}

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	private void line(GL10 gl, float x1, float y1, float x2, float y2)
	{
		lineBuffer.putFloat(x1);
		lineBuffer.putFloat(y1);
		lineBuffer.putFloat(x2);
		lineBuffer.putFloat(y2);
		lineBuffer.position(0);

		gl.glDrawArrays(GL10.GL_LINES, 0, 2);
	}

	private void makeBuffer()
	{
		lineBuffer = ByteBuffer.allocateDirect(4 * 4);
		lineBuffer.order(ByteOrder.nativeOrder());

		lineBuffer.putFloat(0);
		lineBuffer.putFloat(0);
		lineBuffer.putFloat(0);
		lineBuffer.putFloat(0);

		lineBuffer.position(0);
	}
}

class WindCell
{
	public Vector2D dir;
	public Vector2D plus = new Vector2D();
	public Vector2D pos;
	public WindCell(Vector2D dir, Vector2D pos)
	{
		this.dir = dir;
		this.pos = pos;
	}

	public void calc(Vector2D dirs, WindCell cell, float k)
	{
		plus.plus(dirs, 0.6f * k * cell.dir.scalar(dirs));
	}
}