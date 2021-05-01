// SPDX-License-Identifier: GPL-3.0

pragma solidity >= 0.7 .0 < 0.9 .0;
pragma abicoder v2; //remove for testing!

 


contract TradeNode {
    struct Bid{
        address bidderAddress;
        uint amount;
    }
    
    
    uint private lowestId;
    uint private currentId;
    
    mapping(uint => Bid) private statusMap;
    mapping(address=>uint) private withdrawnAmount;

    
    function placeAsk(address usrAddress, uint amount) public returns(bool) {
        require(currentId<10000 && amount>0);
        statusMap[currentId].bidderAddress = usrAddress;
        statusMap[currentId].amount = amount;
        ++currentId;
        
        return true;
    }
    
    function answer(uint amount) public returns(bool) {
        require(amount>0);
        uint totalamt = getTotalAmount();
        require(amount<=totalamt);
        require((totalamt-amount)>(totalamt/100));
        
			if (amount == totalamt){
				lowestId = currentId;
			}
			else {
				uint res0 = findId(amount);
				uint res1 = findAmount(amount);
				require(res0!=0 && res1!=0);
				lowestId = res0;
				statusMap[res0].amount -= res1;
			}
	    return true;
    }
    
    
    //-----------------------------------------------------------
  
    function getTotalAmount() public view returns(uint) {
        uint ret = 0;
        for(uint y=lowestId;y<currentId;++y){
            if(y==10000) return 0;
        	ret += statusMap[y].amount;
        }
        return ret;
    }
  

    function findId(uint amount) private view returns(uint) {
        uint t=lowestId;
        while (t< currentId) {
            if(t==10000) return 0; //not found
			uint amtAt = statusMap[t].amount;
			if (amtAt == amount)
				return t++;
			if (amtAt > amount)
				return t;
			amount -= amtAt;
            ++t;
        }
        return 0; //not found
    }
    
    function findAmount(uint amount) private view returns(uint) {
        uint t=lowestId;
        while (t< currentId) {
            if(t==10000) return 0; //not found
            uint amtAt = statusMap[t].amount;
            if (amtAt == amount)
                return amount;
            if (amtAt > amount)
                return amount;
            amount -= amtAt;
            ++t;
        }
        return 0; //not found
    }
    
    function findAllowedAmount(address usrAddress) private view returns(uint) {
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
    
    function withdrawAll(address usrAddress) public returns(bool) {
        uint amt = findAllowedAmount(usrAddress);
        if(amt!=0) withdrawnAmount[usrAddress] = withdrawnAmount[usrAddress]+ amt;
        return true;
    }
    
    
    
}
