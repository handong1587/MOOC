#include <stdio.h>
#include <stdlib.h>
#include <cstdlib>
#include <ctime>
#include <math.h>
#include <random>
#include "bp.h"

using namespace std;

const int D0 = 2; // input data dimension, not count bias
const int M = 3; // hidden layer neuron, not count bias
const int D2 = 1; // output neuron
const double eta = 1;
const double r = 0.1;
const int T = 50000; // number of epochs

int train_N = 25; // number of training sample
double train_X[25][D0];
int train_Y[25];

int test_N = 250; // number of training sample
double test_X[250][D0];
int test_Y[250];

double X0[D0 + 1];
int Y;
double S1[M + 1], X1[M + 1];
double S2[D2 + 1], X2[D2 + 1];
double W1[D0 + 1][M + 1], W2[M + 1][D2 + 1];

double delta2[D2 + 1];
double delta1[M + 1];

FILE *fLog;
double maxEout, minEout, averEout;

double f_rand(double f_min, double f_max)
{
	double f = (double)rand() / RAND_MAX;
	return f_min + f * (f_max - f_min);
}

void open_log()
{
	char log_name[260];
	sprintf(log_name, "log_M(%d)_eta(%lf)_r(%lf).txt", M, eta, r);
	fLog = fopen(log_name, "w+");
}

void close_log()
{
	fclose(fLog);
}

void setup()
{
	open_log();
}

void init()
{
	// initialize bias
	X0[0] = X1[0] = X2[0] = 1; // X2[0] not used

	// initialize weights
	for (int i = 0; i < D0 + 1; i++)
	{
		for (int j = 0; j < M + 1; j++)
		{
			W1[i][j] = f_rand(-r, r); // W1[][0] not used
		}
		W1[i][0] = 0;
	}
	for (int i = 0; i < M + 1; i++)
	{
		for (int j = 0; j < D2 + 1; j++)
		{
			W2[i][j] = f_rand(-r, r); // W2[][0] not used
		}
		W2[i][0] = 0;
	}
}

void load_data()
{
	// training data
	FILE *fp = fopen("E:\\work\\self study\\Coursera\\機器學習技法 (Machine Learning Techniques)\\homework4\\hw4_nnet_train.dat", "r");
	float x_0, x_1;
	int y;
	int n = 0;
	while (fscanf(fp, "%f %f %d", &x_0, &x_1, &y) != EOF)
	{
		train_X[n][0] = x_0;
		train_X[n][1] = x_1;
		train_Y[n] = y;
		n++;
	}
	fclose(fp);

	// testing data
	fp = fopen("E:\\work\\self study\\Coursera\\機器學習技法 (Machine Learning Techniques)\\homework4\\hw4_nnet_test.dat", "r");
	n = 0;
	while (fscanf(fp, "%f %f %d", &x_0, &x_1, &y) != EOF)
	{
		test_X[n][0] = x_0;
		test_X[n][1] = x_1;
		test_Y[n] = y;
		n++;
	}
	fclose(fp);
}

void free_mem()
{
	close_log();
}

void random_pick()
{
	int n = rand() % train_N;
	X0[1] = train_X[n][0];
	X0[2] = train_X[n][1];
	Y = train_Y[n];
}

void calc_S1()
{
	for (int j = 1; j < M + 1; j++) // bias neuron = 1, just update weight
	{
		S1[j] = 0;
		for (int i = 0; i < D0 + 1; i++)
		{
			S1[j] += X0[i] * W1[i][j];
		}
	}
}

void calc_S2()
{
	for (int j = 1; j < D2 + 1; j++) // bias neuron = 1, just update weight
	{
		S2[j] = 0;
		for (int i = 0; i < M + 1; i++)
		{
			S2[j] += X1[i] * W2[i][j];
		}
	}
}

double _tanh_(double s)
{
	return (exp(s) - exp(-s)) / (exp(s) + exp(-s));
}

double _tanh_differential_(double s)
{
	return (1 - _tanh_(s) *_tanh_(s));
}

void calc_tanh_vector(double *s, double *x, int d)
{
	for (int i = 1; i < d; i++) // bias neuron = 1, do not calculate tanh
	{
		x[i] = _tanh_(s[i]);
	}
}

void fwd()
{
	// input -> hidden layer
	//calc_S_vector(X0, D0 + 1, S1, M + 1, W1); // +1: plus bias neuron
	calc_S1();
	calc_tanh_vector(S1, X1, M + 1);

	// hidden layer -> output
	//calc_S_vector(X1, M + 1, S2, D2 + 1, W2);
	calc_S2();
	calc_tanh_vector(S2, X2, D2 + 1);
}

void bwd()
{
	for (int i = 1; i < D2 + 1; i++) // delta2[0] for bias, output layer do not calculate this.
	{
		delta2[i] = -2 * (Y - X2[i]) * (1 - X2[i] * X2[i]);
	}
	
	for (int i = 0; i < M + 1; i++)
	{
		delta1[i] = 0;
		for (int j = 1; j < D2 + 1; j++)
		{
			if (i > 0)
			{
				delta1[i] += W2[i][j] * delta2[j] * (1 - X1[i] * X1[i]);
			}
			else
			{
				delta1[i] += W2[i][j] * delta2[j] * (1 - X1[i] * X1[i]);
			}
		}
	}
}

void update_weight()
{
	// update W2
	for (int i = 0; i < M + 1; i++)
	{
		for (int j = 1; j < D2 + 1; j++)
		{
			W2[i][j] = W2[i][j] - eta * X1[i] * delta2[j];
		}
	}
	// update W1
	for (int i = 0; i < D0 + 1; i++)
	{
		for (int j = 1; j < M + 1; j++)
		{
			W1[i][j] = W1[i][j] - eta * X0[i] * delta1[j];
		}
	}
}

void nn_iterate()
{
	maxEout = 0;
	minEout = 1;
	averEout = 0;
	for (int i = 0; i < 500; i++)
	{
		init();
		for (int t = 0; t < T; t++)
		{
			// 1) randomly pick one training sample
			random_pick();

			// 2) forward
			fwd();

			// 3) backward
			bwd();

			// 4) gradient descent
			update_weight();

			//if (t == 0 || (t + 1) % 100 == 0)
// 			{
// 				double err = (Y - X2[1]) * (Y - X2[1]);
// 				printf("epoch: %03d, loss = %lf\n", t, err);
// 				fprintf(fLog, "epoch: %03d, loss = %lf\n", t, err);
// 			}
		}
		test(i);
	}
	printf("\nmax Eout = %f\n", maxEout);
	printf("min Eout = %f\n", minEout);
	printf("aver Eout = %f\n", averEout/500);
	fprintf(fLog, "\nmax Eout = %f\n", maxEout);
	fprintf(fLog, "min Eout = %f\n", minEout);
	fprintf(fLog, "aver Eout = %f\n", averEout / 500);
}

void test(int expm)
{
	int err_N = 0;
	for (int i = 0; i < test_N; i++)
	{
		X0[0] = 1;
		X0[1] = test_X[i][0];
		X0[2] = test_X[i][1];
		Y = test_Y[i];
		fwd();
		if (X2[1] * Y < 0)
		{
			err_N++;
		}
	}
	double Eout = (double)err_N / test_N;
	printf("experiment #%03d: Eout = %f\n", expm, Eout);
	fprintf(fLog, "experiment #%03d: Eout = %f\n", expm, Eout);
	if (maxEout < Eout)
	{
		maxEout = Eout;
	}
	if (minEout > Eout)
	{
		minEout = Eout;
	}
	averEout += Eout;
}

int main()
{
	srand(static_cast <unsigned> (time(0)));

	load_data();
	setup();
	nn_iterate();	
	free_mem();
	return 0;
}