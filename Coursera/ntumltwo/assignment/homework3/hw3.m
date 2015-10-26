clear;
clc;

trainData=load('hw3_train.dat');
testData=load('hw3_test.dat');
yTrain=trainData(:,3);
yTeat=testData(:,3);

% plot
pos=find(yTest==1); 
neg=find(yTest==-1);
figure;
plot(train(pos,1),train(pos,2),'k+','LineWidth',2,'MarkerSize', 7);
hold on;
plot(train(neg,1),train(neg,2),'ko','MarkerFaceColor','y','MarkerSize', 7);
hold off;

pos=find(yTest==1); 
neg=find(yTest==-1);
figure;
plot(test(pos,1),test(pos,2),'k+','LineWidth',2,'MarkerSize', 7);
hold on;
plot(test(neg,1),test(neg,2),'ko','MarkerFaceColor','y','MarkerSize', 7);
hold off;

% training
featNum=size(trainData,2)-1;
N=size(trainData,1);
sortedTheta1=sortrows(trainData,1);
sortedTheta2=sortrows(trainData,2);

bestS=zeros(N+1,1);
bestTheta=zeros(N+1,1);
bestGini=ones(N+1,1);
allS=[-1,1];
for i=1:N+1
    for j=1:length(allS)
        for k=1:N
            
        end
    end
end