function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta

% theta(1) must not be updated when involed with regularization
theta2 = theta;
theta2(1) = 0;

h = 1 ./ (1 + exp((-1) * X * theta));
J = sum((-1) * y .* log(h) - (1 - y) .* log(1 - h))/m + sum(theta2 .* theta2) * lambda / (m * 2);

grad(1) = sum((h - y) .* X(:, 1))/m;
for i = 2:length(theta)
    grad(i) = sum((h - y) .* X(:, i))/m + theta2(i) * lambda / m;
end

% =============================================================

end
