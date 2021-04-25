// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.7.0 <0.9.0;

//assume ascending order for now...
//reserved index values 0,1
 
contract LinkedList {

struct node{
    uint previousIndex;
    uint value;
}

mapping(uint=>node) private linkedList;
uint private currentIndex = 0;


function getCurrentIndex() public view returns (uint) {
    return currentIndex;
}

function setCurrentIndex(uint num) public returns(bool){
    currentIndex = num;
    return true;
}

//----------------------------------------------------
function getValueAt(uint index) external view returns(uint){
    return linkedList[index].value;
}

function setValueAt(uint index, uint num) public returns(bool){
    linkedList[index].value = num;
    return true;
}

function getPreviousIndexAt(uint index) external view returns(uint){
    return linkedList[index].previousIndex;
}

function setPreviousIndexAt(uint index, uint num) public returns(bool){
    linkedList[index].previousIndex = num;
    return true;
}

//----------------------------------------------------
function push(uint index, uint num) public returns(bool){
    linkedList[index].previousIndex = currentIndex;
    linkedList[index].value = num;
    currentIndex = index;
    return true;
}

function pop() public returns(bool){
    currentIndex = linkedList[currentIndex].previousIndex;
    return true;
}
//----------------------------------------------------
function insertAt(uint searchIndex, uint index, uint num, bool asc) public returns(bool){
uint closestIndex = asc? findClosestIndexAsc(searchIndex):findClosestIndexDesc(searchIndex);
linkedList[index].previousIndex = linkedList[closestIndex].previousIndex;
linkedList[index].value = num;
linkedList[closestIndex].previousIndex = index;
return true;
}

function truncateAt(uint searchIndex, bool asc) public returns(bool){
uint closestIndex = asc? findClosestIndexAsc(searchIndex):findClosestIndexDesc(searchIndex);
currentIndex = closestIndex;
return true;
}

//assumes ascending order
//returns 1 if not found
function findClosestIndexAsc(uint searchIndex) public view returns(uint){
    uint tmpIndex = currentIndex;
    while(searchIndex<tmpIndex){
        tmpIndex = linkedList[tmpIndex].previousIndex;
    }
    return tmpIndex;
}


//assumes descending order
//returns 1 if not found
function findClosestIndexDesc(uint searchIndex) public view returns(uint){
    uint tmpIndex = currentIndex;
    while(searchIndex>tmpIndex){
        tmpIndex = linkedList[tmpIndex].previousIndex;
    }
    return tmpIndex;
}


}
