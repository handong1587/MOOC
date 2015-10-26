train=load('hw4_knn_train.dat');
test=load('hw4_knn_test.dat');
mTrain=size(train,1);
mTest=size(test,1);
expval=zeros(mTrain,1);
errN=0;
K=5;

% Ein
for i=1:mTrain
    for j=1:mTrain
        expval(j)=exp((-1) * sum((train(i,1:9) - train(j,1:9)) .* (train(i,1:9) - train(j,1:9))));
    end
    [~,sortIndex] = sort(expval,'descend');
    pred=sign(sum(train(sortIndex(1:5),10)));
    if pred*train(i,10)<0
        errN=errN+1;
    end
end
Ein=errN/mTest;
fprintf('Ein=%f\n',Ein);

% Eout
errN=0;
for i=1:mTest
    for j=1:mTrain
        expval(j)=exp((-1) * sum((test(i,1:9) - train(j,1:9)) .* (test(i,1:9) - train(j,1:9))));
    end
    [~,sortIndex] = sort(expval,'descend');
    pred=sign(sum(train(sortIndex(1:5),10)));
    if pred*test(i,10)<0
        errN=errN+1;
    end
end
Eout=errN/mTest;
fprintf('Eout=%f\n',Eout);