package js.jni.code;

import java.nio.Buffer;

public class NativeCalls
{
	public static native void ssInit(float a, float b);

	public static native void ssFree();

	public static native int ssDraw(Buffer vertexCoords, Buffer textureCoords, Buffer vertexColors, Buffer texBase);

	public static native void ssSkip();

	public static native void ssFling(float x, float y, float dx, float dy);

	public static native void ssSetTurbulence(float value);

	public static native void ssSetSnowSpeed(float value);

	public static native void ssSetSnowCount(int value);

	static
	{
		System.loadLibrary("bufmaker");
	}
}
