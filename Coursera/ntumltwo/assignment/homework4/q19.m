train=load('hw4_kmeans_train.dat');
K=10;
m=size(train,1);
n=size(train,2);
T=500;
Ein=zeros(T,1);

for tt=1:T
    sel = randperm(m);
    sel=sel(1:K);
    U=zeros(K,n);

    for i=1:K
        U(i,:)=train(sel(i),:);
    end

    S=zeros(m,1);
    prevS=zeros(m,1);
    dist=zeros(K,1);
    
    while true
        %optimize S(Set):
        for i=1:m
            for kk=1:K
                dist(kk)=sum((train(i,:)-U(kk,:)) .* (train(i,:)-U(kk,:)));
            end
            [~,idx]=min(dist);
            S(i)=idx;
        end

        %optimize U:
        U(:)=0;
        for i=1:m
            U(S(i),:)=U(S(i),:) + train(i,:);
        end
        for kk=1:K
            U(kk,:) = U(kk,:) ./ length(find(S==kk));
        end
        
        % check converge
        if sum(prevS==S)==m
            break;
        end
        
        prevS=S;
    end
    
    for i=1:m
        d=sum((train(i,:)-U(S(i),:)) .* (train(i,:)-U(S(i),:)));
        Ein(tt)=Ein(tt)+d;
    end
    Ein(tt)=Ein(tt)/m;
end

fprintf('average Ein=%f\n',sum(Ein)/500);
