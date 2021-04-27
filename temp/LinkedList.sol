// SPDX-License-Identifier: GPL-3.0

pragma solidity >= 0.7 .0 < 0.9 .0;

//reserved index values 0,1

contract LinkedList {

  struct node {
    uint previousIndex;
    uint value;
  }

  mapping(uint => node) private linkedList;
  uint private currentIndex = 0;
  bool private asc = true; //false for descending

  function setAsc(bool isAscending) public returns(bool) {
    asc = isAscending;
    return true;
  }

  function putNode(uint index, uint previousIndex, uint num) public returns(bool) {
    require(index != 1 && previousIndex != 1);
    linkedList[index].previousIndex = previousIndex;
    linkedList[index].value = num;
    return true;
  }

  function push(uint index, uint num) public returns(bool) {
    require(index != 1);
    require(!((asc && index <= currentIndex) || (!asc && index >= currentIndex && currentIndex != 0)));
    putNode(index, currentIndex, num);
    currentIndex = index;
    return true;
  }

  function pop() public returns(bool) {
    require(currentIndex != 0);
    require(linkedList[currentIndex].previousIndex != 1);
    currentIndex = linkedList[currentIndex].previousIndex;
    return true;
  }

  function insert(uint index, uint num) public returns(bool) {
    require(index != 1);
    require(!isIndexInChain(index) && currentIndex != index);

    if (currentIndex == 0)
      push(index, num);

    else if (asc)
      insertAtAsc(index, num);

    else
      insertAtDesc(index, num);

    return true;
  }

  function insertAtDesc(uint index, uint num) private returns(bool) {
    require(!asc && index != 1);

    if (currentIndex > index) {
      push(index, num);
      return true;
    }

    uint closestIndex = findClosestIndexDesc(index);
    require(closestIndex != 1);
    if (closestIndex == 0)
      closestIndex = findPointerIndex(closestIndex);
    require(closestIndex != 1);
    putNode(index, linkedList[closestIndex].previousIndex, num);
    linkedList[closestIndex].previousIndex = index;
    return true;
  }

  function insertAtAsc(uint index, uint num) public returns(bool) {
    require(asc && index != 1);

    if (currentIndex < index) {
      push(index, num);
      return true;
    }

    uint closestIndex = findClosestIndexAsc(index);
    require(closestIndex != 1);
    putNode(index, linkedList[closestIndex].previousIndex, num);
    linkedList[closestIndex].previousIndex = index;

    return true;
  }

  function remove(uint index) public returns(bool) {
    require(index != 1 && index != 0 && currentIndex != 0 && isIndexInChain(index));

    if (currentIndex == index) {
      pop();
      return true;
    }
    uint pointerIndex = findPointerIndex(index);
    require(pointerIndex != 1);

    if (pointerIndex != 0 && linkedList[pointerIndex].previousIndex != 0)
      linkedList[pointerIndex].previousIndex = linkedList[index].previousIndex;

    if (linkedList[pointerIndex].previousIndex == 0)
      return true;

    pointerIndex = findPointerIndex(pointerIndex);
    require(pointerIndex != 1);
    linkedList[pointerIndex].previousIndex = 0;
    return true;
  }

  function findPointerIndex(uint index) public view returns(uint) {
    require(index != 1);
    uint atCurrent = currentIndex;
    uint prev = linkedList[atCurrent].previousIndex;
    uint count = 0;
    while (prev != index && prev != 0) {
      if (count >= 10000)
        return 1;
      atCurrent = prev;
      prev = linkedList[atCurrent].previousIndex;
      ++count;
    }
    return atCurrent;
  }

  function truncateAt(uint index) public returns(bool) {
    require(index != 1 && index != 0 && index != currentIndex && isIndexInChain(index));
    currentIndex = index;
    return true;
  }

  function isIndexInChain(uint index) public view returns(bool) {
    require(index != 1);
    if (currentIndex == 0 && index != currentIndex) return false;
    if (currentIndex == index) return true;
    uint atCurrent = currentIndex;
    uint prev = linkedList[atCurrent].previousIndex;
    uint count = 0;
    while (prev != 0) {
      if (count >= 10000)
        break;
      atCurrent = prev;
      prev = linkedList[atCurrent].previousIndex;
      if (prev == index || atCurrent == index)
        return true;
      count++;
    }
    return false;
  }

  function findClosestIndexAsc(uint searchIndex) private view returns(uint) {
    require(searchIndex != 1);
    uint atCurrent = currentIndex;
    uint prev = linkedList[atCurrent].previousIndex;
    uint count = 0;
    while (prev > searchIndex) {
      if (count >= 10000)
        return 1;
      atCurrent = prev;
      prev = linkedList[atCurrent].previousIndex;
      ++count;
    }
    return atCurrent;
  }

  function findClosestIndexDesc(uint searchIndex) private view returns(uint) {
    require(searchIndex != 1);
    uint atCurrent = currentIndex;
    uint prev = linkedList[atCurrent].previousIndex;
    uint count = 0;
    while (prev < searchIndex) {
      if (count >= 10000)
        return 1;
      atCurrent = prev;
      if (atCurrent == 0)
        return 0;
      prev = linkedList[atCurrent].previousIndex;
      ++count;
    }
    return atCurrent;
  }

}
