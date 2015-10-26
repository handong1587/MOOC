function [ besti,bestTheta ] = findBestPurity( x,y )
% 此处显示有关此函数的摘要
%   此处显示详细说明
sortX1=sortrows(x,1);
sortX2=sortrows(x,2);
N=length(y);
allS=[-1,1];
bestGini=1;
allTheta1=zeros(N-1,1);
allTheta2=zeros(N-1,1);
for i=1:N-1
    allTheta1(i)=(sortX1(i,1)+sortX1(i+1,1))/2;
    allTheta2(i)=(sortX2(i,2)+sortX2(i+1,2))/2;
end
allTheta=[allTheta1 allTheta2];
featDim=size(x,2);

for j=1:N-1
    for k=1:length(allS)
        for i=1:featDim
            theta=allTheta(j,i);
            yPred=allS(k)*sign(x(:,i)-theta);
            gini=GiniIndex(y,yPred);
            if bestGini>gini
                besti=i;
                bestTheta=theta;
            end
        end
    end
end
end

