/*
 * SnowSystem.cpp
 *
 *  Created on: 10.03.2013
 *      Author: jauseg
 */

#include "SnowSystem.h"

SnowSystem::SnowSystem()
{
	isInited = false;
	width = 480;
	height = 800;
	counter = 0;
	border = 0;
}

SnowSystem::~SnowSystem()
{
	free();
}

