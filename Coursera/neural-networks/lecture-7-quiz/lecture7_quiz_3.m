Wxh = 0.5;
Whh = -1.0;
Why = -0.7;
Hbias = -1.0;
Ybias = 0.0;

x0 = 9;
x1 = 4;
x2 = -2;

z0 = x0 * Wxh + Hbias;
z1 = x1 * Wxh + h0 * Whh + Hbias;
z2 = x2 * Wxh + h1 * Whh + Hbias;

%h0 = sigmoid(x0 * Wxh + Hbias);
%h1 = sigmoid(x1 * Wxh + h0 * Whh + Hbias);

h0 = sigmoid(z0);
h1 = sigmoid(z1);
h2 = sigmoid(z2);

fprintf(1, 'h0=%f, h1=%f, h2=%f\n', h0, h1, h2);

y0 = h0 * Why + Ybias;
y1 = h1 * Why + Ybias;
y2 = h2 * Why + Ybias;

fprintf(1, 'y0=%f, y1=%f, y2=%f\n', y0, y1, y2);

function [out] = sigmoid(x)
out = 1 / (1 + exp(x*(-1)));