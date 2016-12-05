early_stopping = true;
%hid_layer_size = [100, 30, 130, 170, 10];
hid_layer_size = [236, 83, 113, 37, 18];

for i = 1:5,
	fprintf('hid_layer_size = %f\n', hid_layer_size(i));
	a3(0, hid_layer_size(i), 1000, 0.35, 0.9, early_stopping, 100);
end

%weight_decay = [0, 1, 0.1, 10, 0.001, 0.0001];
%lr = [0.002, 0.01, 0.05, 0.2, 1.0, 5.0, 20.0];
%momentum = 0.9;
%for i = 1:7,
%	fprintf('lr = %f, momentum = %f\n', lr(i), momentum);
%	a3(weight_decay, 10, 70, lr(i), momentum, early_stopping, 4);
%end
