
    //---------------------------------------------------------
    //for testing! TODO!!!
        
    string asstr;


    function getstr() public view returns(string memory){
        return asstr;
    }  

        
    function getCurrentId() public view returns(uint){
        return currentId;
    }   
    
    function getLowestId() public view returns(uint){
        return lowestId;
    } 
    
    function getBidderAddrAt(uint t) public view returns(address){
        return statusMap[t].bidderAddress;
    }
    
    function getBidderAmtAt(uint t) public view returns(uint){
        return statusMap[t].amount;
    }
        
    function asString() public returns(string memory) {
        string memory ret = "[";
        string memory b = "]";
        uint t=lowestId;
        while (t< currentId) {
            
             ret = join(ret,join(uint2str(t),join(":",join(toAsciiString(statusMap[t].bidderAddress), join(":",uint2str(statusMap[t].amount))))));
             ret = join(ret,"||");

            ++t;
        }
        ret = join(ret,b);
        asstr = ret;
    }
    
      function join(string memory s1, string memory s2) private returns (string memory){
    return concat(toSlice(s1), toSlice(s2));
  }
    

    function concat(slice memory self, slice memory other) private pure returns (string memory) {
        string memory ret = new string(self._len + other._len);
        uint retptr;
        assembly { retptr := add(ret, 32) }
        memcpy(retptr, self._ptr, self._len);
        memcpy(retptr + self._len, other._ptr, other._len);
        return ret;
    }

    function memcpy(uint dest, uint src, uint len) private pure {
        // Copy word-length chunks while possible
        for(; len >= 32; len -= 32) {
            assembly {
                mstore(dest, mload(src))
            }
            dest += 32;
            src += 32;
        }

        // Copy remaining bytes
        uint mask = 256 ** (32 - len) - 1;
        assembly {
            let srcpart := and(mload(src), not(mask))
            let destpart := and(mload(dest), mask)
            mstore(dest, or(destpart, srcpart))
        }
    }
    
    function toSlice(string memory self)  private returns (slice memory) {
        uint ptr;
        assembly {
            ptr := add(self, 0x20)
        }
        return slice(bytes(self).length, ptr);
    }
    
    struct slice {
        uint _len;
        uint _ptr;
    }
    
function toAsciiString(address x) private view returns (string memory) {
    bytes memory s = new bytes(40);
    for (uint i = 0; i < 20; i++) {
        bytes1 b = bytes1(uint8(uint(uint160(x)) / (2**(8*(19 - i)))));
        bytes1 hi = bytes1(uint8(b) / 16);
        bytes1 lo = bytes1(uint8(b) - 16 * uint8(hi));
        s[2*i] = char(hi);
        s[2*i+1] = char(lo);            
    }
    return string(s);
}

function char(bytes1 b) private view returns (bytes1 c) {
    if (uint8(b) < 10) return bytes1(uint8(b) + 0x30);
    else return bytes1(uint8(b) + 0x57);
} 
    

 function uint2str(
  uint256 _i
)
  internal
  pure
  returns (string memory str)
{
  if (_i == 0)
  {
    return "0";
  }
  uint256 j = _i;
  uint256 length;
  while (j != 0)
  {
    length++;
    j /= 10;
  }
  bytes memory bstr = new bytes(length);
  uint256 k = length;
  j = _i;
  while (j != 0)
  {
    bstr[--k] = bytes1(uint8(48 + j % 10));
    j /= 10;
  }
  str = string(bstr);
}
    
    //---------------------------------------------------------
    
