// SPDX-License-Identifier: GPL-3.0

pragma solidity >= 0.7 .0 < 0.9 .0;

//reserved index values 0,1

contract TradeNode {
    struct Bid{
        address bidderAddress;
        uint amount;
    }
    
    uint private price; //* in native tokens
    uint private lowestId;
    uint private currentId;
    
    mapping(uint => Bid) private statusMap;
    mapping(address=>uint) private withdrawnAmount;
    
    function placeAsk(uint amount) public returns(bool) {
        statusMap[currentId].bidderAddress = msg.sender;
        statusMap[currentId].amount = amount;
        return true;
    }
  
    function getTotalAmount() public view returns(uint) {
        uint ret = 0;
        for(uint y=lowestId;y<currentId;++y)
        	ret += statusMap[y].amount;
        return ret;
    }
  
  //todo, user + conversion is missing!
    function answer(uint amount) public returns(bool) {
        //todo amount conversion
        uint totalamt = getTotalAmount();
        if (amount <= totalamt)
			if (amount == totalamt)
				lowestId = currentId; //TODO
			else {
				uint[] memory res = findId(amount);
				if(res[0]==0 && res[1]==1) return false;
				lowestId = res[0];
				statusMap[res[0]].amount -= res[1]; //TODO
			}

        return true;
    }
    
    
    function findId(uint amount) public view returns(uint[] memory) {
        uint[] memory ret;
        uint t=lowestId;
        while (t< currentId) {
			uint amtAt = statusMap[t].amount;
			if (amtAt == amount){
			    ret[0] = t+1;
			    ret[1] = amount;
				return ret;
			}
			if (amtAt > amount){
			    ret[0] = t;
			    ret[1] = amount;
				return ret;
			}
			amount -= amtAt;
            ++t;
        }
        ret[0] = 0;
        ret[1] = 0;
        return ret;
    }
    
    
    function findAllowedAmount(address usrAddress) public view returns(uint) {
        uint ret = 0;
        uint t=0;
        while (t<lowestId) {
            if (usrAddress==statusMap[t].bidderAddress)
                ret += statusMap[t].amount;
            ++t;
        }
        if(withdrawnAmount[usrAddress]!=0)
        ret -= withdrawnAmount[usrAddress];
        
        return ret;
    }
    
    function withdrawAll() public returns(bool) {
        uint amt = findAllowedAmount(msg.sender);
        if(amt!=0) withdrawnAmount[msg.sender] = withdrawnAmount[msg.sender]+ amt;
        return true;
    }
    
    //---------------------------------------------------------
    //for testing! TODO!!!
    // public static String asString(){
    function asString() public returns(string memory) {
        string memory ret = "";
        uint t=lowestId;
        while (t< currentId) {
            ret += "[" + t + ":" + statusMap[t].bidderAddress + ":" + statusMap[t].amount + "]";
            ++t;
        }
        return ret+"*";
    }
    
    function strConcat(string _a, string _b, string _c, string _d, string _e) internal returns (string){
    bytes memory _ba = bytes(_a);
    bytes memory _bb = bytes(_b);
    bytes memory _bc = bytes(_c);
    bytes memory _bd = bytes(_d);
    bytes memory _be = bytes(_e);
    string memory abcde = new string(_ba.length + _bb.length + _bc.length + _bd.length + _be.length);
    bytes memory babcde = bytes(abcde);
    uint k = 0;
    for (uint i = 0; i < _ba.length; i++) babcde[k++] = _ba[i];
    for (i = 0; i < _bb.length; i++) babcde[k++] = _bb[i];
    for (i = 0; i < _bc.length; i++) babcde[k++] = _bc[i];
    for (i = 0; i < _bd.length; i++) babcde[k++] = _bd[i];
    for (i = 0; i < _be.length; i++) babcde[k++] = _be[i];
    return string(babcde);
}
    
    
    
    //---------------------------------------------------------
    
    
}
