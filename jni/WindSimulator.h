/*
 * WindSimulator.h
 *
 *  Created on: 08.03.2013
 *      Author: jauseg
 */

#ifndef WINDSIMULATOR_H_
#define WINDSIMULATOR_H_

#define borderCount 2

float rndf();

class WindCell
{
	public:
		Vector2D dir;
		Vector2D plus;
		Vector2D pos;
		float mass;

		WindCell()
		{
			mass = 0;
		}

		void set(float x, float y)
		{
			pos.x = x;
			pos.y = y;
		}

		void calc(Vector2D *dirs, WindCell *cell, float k)
		{
			plus.plus(dirs, 0.6f * k * cell->dir.scalar(dirs));
		}
};

class WindSimulator
{
	public:
		int widthCount;
		int heigthCount;
		float cellSize;

		Vector2D dirs[8];

		WindCell *cells;
		int cellsCount;

		void init(float width, float height)
		{
			float c = 24;
			cells = 0;
			init(width, height, width > height ? width / c : height / c);
		}

		void init(float width, float heigth, float cellSize)
		{
			dirs[0].x = -0.707107f;
			dirs[0].y = -0.707107f;
			dirs[1].x = 0.0f;
			dirs[1].y = -1.0f;
			dirs[2].x = 0.707107f;
			dirs[2].y = -0.707107f;
			dirs[3].x = 1.0f;
			dirs[3].y = 0.0f;
			dirs[4].x = 0.707107f;
			dirs[4].y = 0.707107f;
			dirs[5].x = 0.0f;
			dirs[5].y = 1.0f;
			dirs[6].x = -0.707107f;
			dirs[6].y = 0.707107f;
			dirs[7].x = -1.0f;
			dirs[7].y = 0.0f;

			//makeBuffer();

			widthCount = (int) (width / cellSize) + 1 + 2 * borderCount;
			heigthCount = (int) (heigth / cellSize) + 1 + 2 * borderCount;

			this->cellSize = cellSize;

			// distribute cells

			cellsCount = widthCount * heigthCount;

			free();

			cells = new WindCell[cellsCount];

			for (int i = 0; i < cellsCount; i++)
			{
				cells[i].set(cellSize * (float) ((i % widthCount) - borderCount),
						cellSize * (float) ((int) (i / widthCount) - borderCount));
			}
		}

		void free()
		{
			if (cells)
			{
				delete[] cells;
				cells = 0;
			}
		}

		void tick()
		{
			for (int cy = 1; cy < heigthCount - 1; cy++)
			{
				for (int cx = 1; cx < widthCount - 1; cx++)
				{
					int i = ((cy - 1) * widthCount) + (cx - 1);

					WindCell *cell = &cells[i + 1 + widthCount];
					cell->mass *= 0.9f;

					WindCell *c = &cells[i];

					cell->calc(&dirs[0], c, 0.707107f);
					c++;
					cell->calc(&dirs[1], c, 1.0f);
					c++;
					cell->calc(&dirs[2], c, 0.707107f);
					c += widthCount;
					cell->calc(&dirs[3], c, 1.0f);
					c += widthCount;
					cell->calc(&dirs[4], c, 0.707107f);
					c--;
					cell->calc(&dirs[5], c, 1.0f);
					c--;
					cell->calc(&dirs[6], c, 0.707107f);
					c -= widthCount;
					cell->calc(&dirs[7], c, 1.0f);
				}
			}

			{
				WindCell *c = &cells[0];
				for (int i = 0; i < cellsCount; i++)
				{
					c->dir.plus(&c->plus);
					c->plus.set(0, 0);
					c->dir.scale(0.325f);
					c++;
				}
			}
		}

		void fling(float x, float y, float dx, float dy)
		{
			WindCell *cell = cellAt(x, y);
			cell->dir.plus(dx, dy);
		}

		WindCell* cellAt(float x, float y)
		{
			int cx = (int) (x / cellSize) + borderCount;
			int cy = (int) (y / cellSize) + borderCount;

			return &cells[cy * widthCount + cx];
		}

		void calcAt(Vector2D *pos, Vector2D *dir, float k)
		{
			int cx = (int) (pos->x / cellSize) + borderCount;
			int cy = (int) (pos->y / cellSize) + borderCount;

			float px = (pos->x - (float) (cx - borderCount) * cellSize) / cellSize;
			float py = (pos->y - (float) (cy - borderCount) * cellSize) / cellSize;

			Vector2D v1, v2, v3, v4;

			if (cx < widthCount - 1 && cy < heigthCount - 1 && cx >= 0 && cy >= 0)
			{
				WindCell *cell = &cells[(cx + 0) + (cy + 0) * widthCount];
				cell->mass += 1;

				v1 = cells[(cx + 0) + (cy + 0) * widthCount].dir;
				v2 = cells[(cx + 1) + (cy + 0) * widthCount].dir;
				v3 = cells[(cx + 0) + (cy + 1) * widthCount].dir;
				v4 = cells[(cx + 1) + (cy + 1) * widthCount].dir;

				dir->plus(interpolate(v1.x, v2.x, v3.x, v4.x, px, py), interpolate(v1.y, v2.y, v3.y, v4.y, px, py), k);

				dir->plus(rndf() - 0.5f, rndf() - 0.5f, cell->mass * 0.002f);
				cell->dir.plus(rndf() - 0.5f, rndf() - 0.5f, cell->mass * 0.002f);
			}

		}
};

#endif
