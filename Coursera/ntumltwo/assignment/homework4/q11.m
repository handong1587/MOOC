train=load('hw4_nnet_train.dat');
test=load('hw4_nnet_test.dat');

yTrain=train(:,3);
pos=find(yTrain==1); 
neg=find(yTrain==-1);

figure;
plot(train(pos,1),train(pos,2),'k+','LineWidth',2,'MarkerSize', 7);
hold on;
plot(train(neg,1),train(neg,2),'ko','MarkerFaceColor','y','MarkerSize', 7);
hold off;

% test
yTest=test(:,3);
pos=find(yTest==1); 
neg=find(yTest==-1);
figure;
plot(test(pos,1),test(pos,2),'k+','LineWidth',2,'MarkerSize', 7);
hold on;
plot(test(neg,1),test(neg,2),'ko','MarkerFaceColor','y','MarkerSize', 7);
hold off;