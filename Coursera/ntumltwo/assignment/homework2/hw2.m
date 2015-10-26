train=load('hw2_adaboost_train.dat');
test=load('hw2_adaboost_test.dat');

yTrain=train(:,3);
pos=find(yTrain==1); 
neg=find(yTrain==-1);

figure;
plot(train(pos,1),train(pos,2),'k+','LineWidth',2,'MarkerSize', 7);
hold on;
plot(train(neg,1),train(neg,2),'ko','MarkerFaceColor','y','MarkerSize', 7);
hold off;

Xdim=size(train,2)-1;
tr1=sortrows(train,1);
tr2=sortrows(train,2);
len=size(train,1);
T=300;
bestTheta=zeros(T,1);
bestS=zeros(T,1);
bestFeat=zeros(T,1);
bestEin=ones(T,1);
bestErrNum=zeros(T,1);

u=ones(T+1,len)/len;
eps=zeros(T,1);
delta=zeros(T,1);
alpha=zeros(T,1);

allS=[-1 1];
allTheta1=zeros(len,1);
allTheta2=zeros(len,1);
allTheta1(1)=-100;
allTheta2(1)=-100;
for i=2:len
    allTheta1(i)=(tr1(i-1,1)+tr1(i,1))/2;
    allTheta2(i)=(tr2(i-1,2)+tr2(i,2))/2;
end

allTheta=[allTheta1 allTheta2];

for t=1:T
    for i=1:length(allS)
        for j=1:Xdim
            for k=1:length(allTheta(:,j))
                s=allS(i);
                theta=allTheta(k,j);
                yPred=s*sign(train(:,j)-theta);
                correctIdx=find(yPred==yTrain);
                errIdx=find(yPred~=yTrain);
                Ein=sum(u(t,errIdx));
                if Ein<bestEin(t)
                    bestEin(t)=Ein;
                    bestTheta(t)=theta;
                    bestS(t)=s;
                    bestFeat(t)=j;
                    bestErrNum(t)=length(errIdx);
                end
            end
        end
    end
    % update
    yPred=bestS(t)*sign(train(:,bestFeat(t))-bestTheta(t));
    bestCorrectIdx=find(yPred==yTrain);
    bestErrIdx=find(yPred~=yTrain);
    
    eps(t)=bestEin(t)/sum(u(t,:));
    delta(t)=sqrt((1-eps(t))/eps(t));
    u(t+1,bestErrIdx)=u(t,bestErrIdx)*delta(t);
    u(t+1,bestCorrectIdx)=u(t,bestCorrectIdx)/delta(t);
    alpha(t)=log(eps(t));
end

fprintf('Ein_g1 = %f\n', bestEin(1));

% test
yTest=test(:,3);
pos=find(yTest==1); 
neg=find(yTest==-1);
figure;
plot(test(pos,1),test(pos,2),'k+','LineWidth',2,'MarkerSize', 7);
hold on;
plot(test(neg,1),test(neg,2),'ko','MarkerFaceColor','y','MarkerSize', 7);
hold off;

% Question 17
yPred=bestS(1)*sign(test(:,bestFeat(1))-bestTheta(1));
Eout_g1=sum(yPred~=yTest);
fprintf('Eout_g1 = %f\n', Eout_g1);

% Question 18
Gx=zeros(length(yTest),1);
for t=1:T
    Gx=Gx+alpha(t)*bestS(t)*sign(test(:,bestFeat(t))-bestTheta(t));
end
yPred=sign(Gx);
Eout_G=sum(yPred~=yTest);
fprintf('Eout_G = %f\n', Eout_G);