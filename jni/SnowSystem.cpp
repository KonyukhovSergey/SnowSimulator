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
}

SnowSystem::~SnowSystem()
{
	free();
}

