function test_a4()

% Q9

for lr = 0.1:0.05:1
    fprintf(1, 'lr = %f\n', lr);
    a4_main(300, .02, lr, 1000);
end
