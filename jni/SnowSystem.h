/*
 * SnowSystem.h
 *
 *  Created on: 10.03.2013
 *      Author: jauseg
 */

#ifndef SNOWSYSTEM_H_
#define SNOWSYSTEM_H_

#include "Sneginka.h"
#include "Indexer.h"

#define CELLS_COUNT 32
extern float scale;
extern int snowCount;

class SnowSystem
{
private:
	bool isInited;

	Indexer indexer;

	Sneginka* allocate()
	{
		TwoLinkedList *item = indexer.allocate();

		if (item->object == 0)
		{
			item->object = new Sneginka();
		}

		return (Sneginka*) item->object;
	}

public:
	SnowSystem();
	virtual ~SnowSystem();

	float width;
	float height;
	float border;

	WindSimulator wind;

	void init(float width, float height)
	{
		if (isInited)
		{
			free();
		}

		counter = 0;

		this->width = width;
		this->height = height;

		this->border = 2.0f
				* (width > height ?
						width / (float) CELLS_COUNT :
						height / (float) CELLS_COUNT);

		scale = width < height ? width / 720.0f : height / 720.0f;

		wind.init(width, height);

		for (int i = 0; i < 100; i++)
		{
			allocate()->init(width, height);
		}

		isInited = true;
	}

	void free()
	{
		if (isInited)
		{
			wind.free();

			while (indexer.allocate()->object != 0)
				;

			TwoLinkedList *item = indexer.getFirst();

			while (item)
			{
				if (item->object)
				{
					delete (Sneginka*) item->object;
				}

				item = item->next;
			}

			indexer.free();

			isInited = false;
		}
	}

	int counter;

	int draw(float *pVertex, float *pTex, int* pColor, float *pTexBase)
	{
		if (counter % snowCount == 0)
		{
			allocate()->init(0, -border * 0.75f, width, 0);
			allocate()->init(0, -border * 0.75f, width, 0);
			allocate()->init(-border * 0.75f, 0, 0, height);
			allocate()->init(width + border * 0.75f, 0, 0, height);
		}

		if (counter % 5 == 0)
		{
			wind.tick();
			float v = border * 0.05f;
			fling(rndf() * width, rndf() * height, rndf() * v - 0.5f * v,
					rndf() * v - 0.5f * v);
		}

		counter++;

		TwoLinkedList *item = indexer.getFirst();

		int triangleCount = 0;

		while (item)
		{
			Sneginka *sneginka = (Sneginka*) item->object;

			sneginka->tick(&wind);

			sneginka->putTo(pVertex, pColor);
			pVertex += 12;
			pColor += 6;
			memcpy(pTex, pTexBase, 12 * 4);
			pTex += 12;

			triangleCount += 2;

			if (sneginka->pos.y > height + border || sneginka->pos.x < -border
					|| sneginka->pos.x > width + border
					|| sneginka->pos.y < -border)
			{
				item = indexer.releaseAndGetNext(item);
			}
			else
			{
				item = item->next;
			}
		}

		return triangleCount;
	}

	void skip()
	{
		if (counter % snowCount == 0)
		{
			allocate()->init(0, -border * 0.75f, width, 0);
			allocate()->init(0, -border * 0.75f, width, 0);
			allocate()->init(-border * 0.75f, 0, 0, height);
			allocate()->init(width + border * 0.75f, 0, 0, height);
		}

		if (counter % 5 == 0)
		{
			wind.tick();
			float v = border * 0.05f;
			fling(rndf() * width, rndf() * height, rndf() * v - 0.5f * v,
					rndf() * v - 0.5f * v);
		}

		counter++;

		TwoLinkedList *item = indexer.getFirst();

		int triangleCount = 0;

		while (item)
		{
			Sneginka *sneginka = (Sneginka*) item->object;

			sneginka->tick(&wind);

			if (sneginka->pos.y > height + border || sneginka->pos.x < -border
					|| sneginka->pos.x > width + border
					|| sneginka->pos.y < -border)
			{
				item = indexer.releaseAndGetNext(item);
			}
			else
			{
				item = item->next;
			}
		}
	}

	void fling(float px, float py, float vx, float vy)
	{
		wind.fling(px, py, vx * 0.25f, vy * 0.25f);
	}
};

#endif /* SNOWSYSTEM_H_ */
