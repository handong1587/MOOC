Wxh = -0.1;
Whh = 0.5;
Why = 0.25;
Hbias = 0.4;
Ybias = 0.0;

x0 = 18;
x1 = 9;
x2 = -8;

t0 = 0.1;
t1 = -0.1;
t2 = -0.2;

z0 = x0 * Wxh + Hbias;
z1 = x1 * Wxh + h0 * Whh + Hbias;
z2 = x2 * Wxh + h1 * Whh + Hbias;

h0 = sigmoid(z0);
h1 = sigmoid(z1);
h2 = sigmoid(z2);

fprintf(1, 'h0=%f, h1=%f, h2=%f\n', h0, h1, h2);

y0 = h0 * Why + Ybias;
y1 = h1 * Why + Ybias;
y2 = h2 * Why + Ybias;

fprintf(1, 'y0=%f, y1=%f, y2=%f\n', y0, y1, y2);

E0 = (t0 - y0)**2 / 2;
E1 = (t1 - y1)**2 / 2;
E2 = (t2 - y2)**2 / 2;

error_derivative1 = (y1 - t1) * Why * (h1 * (1 - h1));
fprintf(1, 'error derivative at T=1: %f\n', error_derivative1);

error_derivative2 = (y2 - t2) * Why * (h2 * (1 - h2));
fprintf(1, 'error derivative at T=2: %f\n', error_derivative2);

function [out] = sigmoid(x)
out = 1 / (1 + exp(x*(-1)));