function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%

% Part 1:
X = [ones(m, 1) X];
for i = 1 : m
    y_i = zeros(num_labels, 1);
    y_i(y(i)) = 1;
    h1 = sigmoid(X(i, :) * Theta1');
    h1 = [1 h1];
    h2 = sigmoid(h1 * Theta2');
    J = J + sum((-1) * log(h2) * y_i - log(1 - h2) * (1 - y_i));
end
J = J/m;

% Regularized term
Theta1_reg = Theta1;
Theta1_reg(:, 1) = 0;
Theta2_reg = Theta2;
Theta2_reg(:, 1) = 0;
regTerm = (sum(sum(Theta1_reg.^2)) + sum(sum(Theta2_reg.^2))) * lambda / (m * 2);

J = J + regTerm;

% Part 2:
acc_delta_1 = zeros(size(Theta1)); % 25 * 401
acc_delta_2 = zeros(size(Theta2)); % 10 * 26

Theta2_tmp = Theta2(:, 2:end);      % 10 * 25

for i = 1 : m
    % Step 1
    a_1 = X(i, :);          % 1 * 401
    z_2 = a_1 * Theta1';	% 1 * 25
    a_2 = sigmoid(z_2);     % 1 * 25
    a_2 = [1 a_2];          % 1 * 26
    z_3 = a_2 * Theta2';    % 1 * 10
    a_3 = sigmoid(z_3);     % 1 * 10
    
    % Step 2
    y_i = zeros(num_labels, 1); % 10 * 1
    y_i(y(i)) = 1;
    delta_3 = a_3' - y_i;       % 10 * 1
    
    z_2_tmp = [1 z_2];          % 1 * 26 ????
    delta_2 = Theta2' * delta_3 .* sigmoidGradient(z_2_tmp'); % 26 * 1
    delta_2 = delta_2(2:end);	% 25 * 1
   
%     delta_2 = Theta2_tmp' * delta_3 .* sigmoidGradient(z_2'); % 25 * 1
    
    acc_delta_1 = acc_delta_1 + delta_2 * a_1;
    acc_delta_2 = acc_delta_2 + delta_3 * a_2;
end

% Step 2
Theta1_grad = acc_delta_1 / m;
Theta2_grad = acc_delta_2 / m;

% Part 3:
Theta1Filtered = Theta1(:, 2:end);
Theta2Filtered = Theta2(:, 2:end);

Theta1_grad(:, 2:end) = Theta1_grad(:, 2:end) + lambda * Theta1Filtered / m;
Theta2_grad(:, 2:end) = Theta2_grad(:, 2:end) + lambda * Theta2Filtered / m;

% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
