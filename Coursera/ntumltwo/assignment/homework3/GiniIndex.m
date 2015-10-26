function [ gini ] = GiniIndex( yTruth, yPred )
%UNTITLED3 此处显示有关此函数的摘要
%   此处显示详细说明
N=length(yTruth);
u_pos=sum(yTruth==1&yPred==1)/N;
u_neg=sum(yTruth==-1&yPred==-1)/N;
gini=1-u_pos-u_neg;

end

