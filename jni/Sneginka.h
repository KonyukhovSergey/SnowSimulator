/*
 * Sneginka.h
 *
 *  Created on: 08.03.2013
 *      Author: jauseg
 */

#ifndef SNEGINKA_H_
#define SNEGINKA_H_

#include <stdlib.h>

float rndf();
extern float scale;

#include "Vector2D.h"
#include "WindSimulator.h"

extern float turbulence;
extern float snowSpeed;
extern float parallax;

extern float ax, ay, az;

class Sneginka
{
	public:
		Vector2D pos;
		Vector2D vel;
		float alfa;
		float size;

		void init(float width, float height)
		{
			init(0, 0, width, height);
		}

		void init(float left, float top, float width, float height)
		{
			alfa = rndf() * 0.75f + 0.25f;
			pos.set(rndf() * width + left, rndf() * height + top);
			vel.set(0, 0);
			size = (rndf() * 120.0f + 8.0f) * scale;
		}

		void putTo(float *pVertex, int *pColor)
		{
			float dc = (size - 120 * scale) * parallax;
			float dx = ax * dc;
			float dy = -ay * dc;

			float x = pos.x + dx;
			float y = pos.y + dy;
			float s = size * 0.5f;

			unsigned int c = 0xFFFFFF | (((unsigned int) (255.0f * ((1.0f - size / (scale * 148.0f)) * alfa))) << 24);

			*pVertex = x - s;
			pVertex++;
			*pVertex = y - s;
			pVertex++;
			*pVertex = x + s;
			pVertex++;
			*pVertex = y - s;
			pVertex++;
			*pVertex = x + s;
			pVertex++;
			*pVertex = y + s;
			pVertex++;
			*pVertex = x - s;
			pVertex++;
			*pVertex = y - s;
			pVertex++;
			*pVertex = x + s;
			pVertex++;
			*pVertex = y + s;
			pVertex++;
			*pVertex = x - s;
			pVertex++;
			*pVertex = y + s;
			//pVertex++;

			*pColor = c;
			pColor++;
			*pColor = c;
			pColor++;
			*pColor = c;
			pColor++;
			*pColor = c;
			pColor++;
			*pColor = c;
			pColor++;
			*pColor = c;
			//pColor++;
		}

		void tick(WindSimulator *wind)
		{
			float s = scale * size * snowSpeed;
			pos.x += vel.x * s;
			pos.y += vel.y * s;
			vel.y += 0.05f;
			wind->calcAt(&pos, &vel, turbulence);
			vel.x *= 0.97f;
			vel.y *= 0.97f;
		}
};

#endif /* SNEGINKA_H_ */
