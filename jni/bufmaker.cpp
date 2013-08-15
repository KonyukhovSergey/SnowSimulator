#include <jni.h>

#include "Vector2D.h"
#include "WindSimulator.h"
#include "SnowSystem.h"
#include "Sneginka.h"
#include "SimplexNoise.h"

SnowSystem ss;

extern "C"
{

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssSetTurbulence(JNIEnv *env, jclass, jfloat value)
	{
		turbulence = value;
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssSetSnowCount(JNIEnv *env, jclass, jint value)
	{
		snowCount = value;
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssSetSnowSpeed(JNIEnv *env, jclass, jfloat value)
	{
		snowSpeed = value;
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssInit(JNIEnv *env, jclass, jfloat a, jfloat b)
	{
		ss.init(a, b);
		SimplexNoise::init();
	}

	JNIEXPORT jint JNICALL Java_js_jni_code_NativeCalls_ssDraw(JNIEnv *env, jclass, jobject vertexCoords,
			jobject textureCoords, jobject vertexColors, jobject texBase)
	{
		float* pVertex = (float*) env->GetDirectBufferAddress(vertexCoords);
		float* pTex = (float*) env->GetDirectBufferAddress(textureCoords);
		float* pTexBase = (float*) env->GetDirectBufferAddress(texBase);
		int* pColors = (int*) env->GetDirectBufferAddress(vertexColors);

		return ss.draw(pVertex, pTex, pColors, pTexBase);
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssSkip(JNIEnv *env, jclass)
	{
		ss.skip();
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssFling(JNIEnv *env, jclass, jfloat x, jfloat y, jfloat dx,
			jfloat dy)
	{
		ss.fling(x, y, dx, dy);
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_ssFree(JNIEnv *env, jclass)
	{
		ss.free();
	}

	JNIEXPORT void JNICALL Java_js_jni_code_NativeCalls_noise(JNIEnv *env, jclass, jobject buffer,
			jint size, jfloat freq, jfloat time)
	{
		unsigned int * pBuf = (unsigned int *) env->GetDirectBufferAddress(buffer);

		int length = size * size;

		for (int i = 0; i < length; i++)
		{
			float x = ((float) (i % size)) / freq;
			float y = ((float) (i / size)) / freq;

			int r = (int) (128.0f * (1.0f + SimplexNoise::noise(x, y, time)));
			int g = (int) (128.0f * (1.0f + SimplexNoise::noise(x + size, y, time)));
			int b = (int) (128.0f * (1.0f + SimplexNoise::noise(x, y + size, time)));

			*pBuf = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			pBuf++;
		}
	}



}

