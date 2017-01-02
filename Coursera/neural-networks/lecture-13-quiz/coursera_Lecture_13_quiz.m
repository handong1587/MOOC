function coursera_Lecture_13_quiz()


% https://www.coursera.org/learn/neural-networks/discussions/weeks/13
% https://www.coursera.org/learn/neural-networks/discussions/weeks/13/threads/q0Npi73eEeavShIAFgB4-A

w1 = -6.90675478;
w2 = 0.40546511;

% Q2
% P(v=1|h1=0,h2=1)

h1 = 0;
h2 = 1;

v_in = h1*w1 + h2*w2;
v_out = sigmoid(v_in);
ans2 = v_out;

fprintf(1, 'Q2: %f\n', ans2);

% Q3
% P(h1=0,h2=1,v=1) = P(v=1|h1=0,h2=1)P(h1=0,h2=1) = P(C011)

ans3 = ans2 * 0.5 * 0.5;
fprintf(1, 'Q3: %f\n', ans3);

% Q4
% d logP(C011) / dw1

fprintf(1, 'Q4: %d\n', 0);

% Q5
% d logP(C011) / dw2

ans5 = exp(-w2) / (1+exp(-w2));
fprintf(1, 'Q5: %d\n', ans5);

% Q6
% P(h2=1|v=1,h1=0) = P(v=1|h1=0,h2=1)P(h2=1) / ( P(v=1|h1=0,h2=1)P(h2=1) + P(v=1|h1=0,h2=0)P(h2=0)  )

w1 =10;
w2 = -4;

ans6 = (sigmoid(w2)*0.5) / (0.5*0.5+0.5*sigmoid(w2));
fprintf(1, 'Q6: %f\n', ans6);

% Q7

ans7 = (sigmoid(w1+w2)*0.5)/(0.5*sigmoid(w1+w2) + 0.5*sigmoid(w1));
fprintf(1, 'Q7: %d\n', ans7);

function [out] = sigmoid(x)
out = 1 / (1 + exp(x*(-1)));
