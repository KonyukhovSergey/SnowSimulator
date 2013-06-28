/*
 * Vector2D.cpp
 *
 *  Created on: 08.03.2013
 *      Author: jauseg
 */

#include "Vector2D.h"

float interpolate(float v1, float v2, float v3, float v4, float px, float py)
{
	return (v1 * (1.0f - px) + v2 * px) * (1.0f - py)
			+ (v3 * (1.0f - px) + v4 * px) * py;
}
