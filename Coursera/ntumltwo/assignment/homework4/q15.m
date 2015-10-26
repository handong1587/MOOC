train=load('hw4_knn_train.dat');
test=load('hw4_knn_test.dat');
mTrain=size(train,1);
mTest=size(test,1);
expval=zeros(mTrain,1);
errN=0;

for i=1:mTest
    for j=1:mTrain
        expval(j)=exp((-1) * sum((test(i,1:9) - train(j,1:9)) .* (test(i,1:9) - train(j,1:9))));
    end
    [~,idx]=max(expval(:));
    if test(i,10)*train(idx,10) < 0
        errN=errN+1;
    end
end
Eout=errN/mTest;
fprintf('Eout=%f\n',Eout);