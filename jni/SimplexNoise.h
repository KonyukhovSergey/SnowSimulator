/*
 * SimplexNoise.h
 *
 *  Created on: 14.08.2013
 *      Author: konyukhov.sergey
 *
 *      code based from java code.
 *
 */

#ifndef SIMPLEXNOISE_H_
#define SIMPLEXNOISE_H_

//#define F2 0,36602540378443864676372317075294
//#define G2 0,21132486540518711774542560974902
//#define F3 0,33333333333333333333333333333333
//#define G3 0,16666666666666666666666666666667

#define F2 0.36602540
#define G2 0.21132486
#define F3 0.33333333
#define G3 0.16666667

extern int grad3[][3];
extern short p[];

extern short perm[512];
extern short permMod12[512];

class SimplexNoise
{

	private:


		static int fastfloor(double x)
		{
			int xi = (int) x;
			return x < xi ? xi - 1 : xi;
		}

		static float dot(int* g, float x, float y)
		{
			return g[0] * x + g[1] * y;
		}

		static float dot(int* g, float x, float y, float z)
		{
			return g[0] * x + g[1] * y + g[2] * z;
		}

	public:
		SimplexNoise();
		virtual ~SimplexNoise();

		static void init()
		{
			for (int i = 0; i < 512; i++)
			{
				perm[i] = p[i & 255];
				permMod12[i] = (short) (perm[i] % 12);
			}
		}

		static float noise(float xin, float yin)
		{
			float n0, n1, n2;

			float s = (xin + yin) * F2;
			int i = fastfloor(xin + s);
			int j = fastfloor(yin + s);
			float t = (i + j) * G2;
			float X0 = i - t;
			float Y0 = j - t;
			float x0 = xin - X0;
			float y0 = yin - Y0;

			int i1, j1;

			if (x0 > y0)
			{
				i1 = 1;
				j1 = 0;
			}
			else
			{
				i1 = 0;
				j1 = 1;
			}

			float x1 = x0 - i1 + G2;

			float y1 = y0 - j1 + G2;
			float x2 = x0 - 1.0f + 2.0f * G2;

			float y2 = y0 - 1.0f + 2.0f * G2;

			int ii = i & 255;
			int jj = j & 255;
			int gi0 = permMod12[ii + perm[jj]];
			int gi1 = permMod12[ii + i1 + perm[jj + j1]];
			int gi2 = permMod12[ii + 1 + perm[jj + 1]];

			float t0 = 0.5f - x0 * x0 - y0 * y0;

			if (t0 < 0)
			{
				n0 = 0.0f;
			}
			else
			{
				t0 *= t0;
				n0 = t0 * t0 * dot(grad3[gi0], x0, y0);
			}

			float t1 = 0.5f - x1 * x1 - y1 * y1;

			if (t1 < 0)
			{
				n1 = 0.0f;
			}
			else
			{
				t1 *= t1;
				n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
			}

			float t2 = 0.5f - x2 * x2 - y2 * y2;

			if (t2 < 0)
			{
				n2 = 0.0f;
			}
			else
			{
				t2 *= t2;
				n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
			}

			return 70.0f * (n0 + n1 + n2);
		}

		static float noise(float xin, float yin, float zin)
		{
			float n0, n1, n2, n3;

			float s = (xin + yin + zin) * F3;

			int i = fastfloor(xin + s);
			int j = fastfloor(yin + s);
			int k = fastfloor(zin + s);
			float t = (i + j + k) * G3;
			float X0 = i - t;
			float Y0 = j - t;
			float Z0 = k - t;
			float x0 = xin - X0;
			float y0 = yin - Y0;
			float z0 = zin - Z0;

			int i1, j1, k1;
			int i2, j2, k2;

			if (x0 >= y0)
			{
				if (y0 >= z0)
				{
					i1 = 1;
					j1 = 0;
					k1 = 0;
					i2 = 1;
					j2 = 1;
					k2 = 0;
				}
				else
				{
					if (x0 >= z0)
					{
						i1 = 1;
						j1 = 0;
						k1 = 0;
						i2 = 1;
						j2 = 0;
						k2 = 1;
					}
					else
					{
						i1 = 0;
						j1 = 0;
						k1 = 1;
						i2 = 1;
						j2 = 0;
						k2 = 1;
					}
				}
			}
			else
			{
				if (y0 < z0)
				{
					i1 = 0;
					j1 = 0;
					k1 = 1;
					i2 = 0;
					j2 = 1;
					k2 = 1;
				}
				else
				{
					if (x0 < z0)
					{
						i1 = 0;
						j1 = 1;
						k1 = 0;
						i2 = 0;
						j2 = 1;
						k2 = 1;
					}
					else
					{
						i1 = 0;
						j1 = 1;
						k1 = 0;
						i2 = 1;
						j2 = 1;
						k2 = 0;
					}
				}
			}

			float x1 = x0 - i1 + G3;
			float y1 = y0 - j1 + G3;
			float z1 = z0 - k1 + G3;
			float x2 = x0 - i2 + 2.0f * G3;

			float y2 = y0 - j2 + 2.0f * G3;
			float z2 = z0 - k2 + 2.0f * G3;
			float x3 = x0 - 1.0f + 3.0f * G3;

			float y3 = y0 - 1.0f + 3.0f * G3;
			float z3 = z0 - 1.0f + 3.0f * G3;

			int ii = i & 255;
			int jj = j & 255;
			int kk = k & 255;

			int gi0 = permMod12[ii + perm[jj + perm[kk]]];
			int gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]];
			int gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]];
			int gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]];

			float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;

			if (t0 < 0)
			{
				n0 = 0.0f;
			}
			else
			{
				t0 *= t0;
				n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
			}

			float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;

			if (t1 < 0)
			{
				n1 = 0.0f;
			}
			else
			{
				t1 *= t1;
				n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
			}

			float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;

			if (t2 < 0)
			{
				n2 = 0.0f;
			}
			else
			{
				t2 *= t2;
				n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
			}

			float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;

			if (t3 < 0)
			{
				n3 = 0.0f;
			}
			else
			{
				t3 *= t3;
				n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
			}

			return 32.0f * (n0 + n1 + n2 + n3);
		}
};

#endif /* SIMPLEXNOISE_H_ */
