function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%

C_arr = [0.01 0.03 0.1 0.3 1 3 10 30];
sigma_arr = [0.01 0.03 0.1 0.3 1 3 10 30];
m = length(C_arr);
n = length(sigma_arr);
minError = 1.0;
for i = 1 : m
    for j = 1 : n
        C_try = C_arr(i);
        sigma_try = sigma_arr(j);
        model= svmTrain(X, y, C_try, @(x1, x2) gaussianKernel(x1, x2, sigma_try));
        predictions = svmPredict(model, Xval);
        curError = mean(double(predictions ~= yval));
        if curError < minError
            C = C_try;
            sigma = sigma_try;
            minError = curError;
        end
    end
end

% =========================================================================

end
