function [ node ] = build_tree( x,y,depth )
%UNTITLED4 此处显示有关此函数的摘要
%   此处显示详细说明
if length(y)==1
    node.isLeaf=true;
    node.label=y;
end
node.isLeaf=false;

end

