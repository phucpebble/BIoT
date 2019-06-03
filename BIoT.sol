//Created by Henry Pham
pragma solidity >=0.5.0 <0.7.0;

contract BIoTStorage {
    string public farmlogo;
    string public farmname; 
    string public foodname;
    string public farmaddress;
    string public position;
    string public humidity; 
    string public temperature; 

    
    function setFarmLogo(string memory  _farmLogo) public {
        farmlogo = _farmLogo;
    }
    function setFarm_FoodInfor(string memory  _farmname, string memory _farmaddress, string memory _position, string memory  _foodname, string memory  _humidity, string memory _temperature) public {
        farmname = _farmname;
        farmaddress =_farmaddress;
        position = _position;
        foodname = _foodname;
        humidity = _humidity;
        temperature = _temperature;
    }


    function getFarmLogo() public view returns (string memory){
        return farmlogo;
    }
    
    function getFarm_FoodInfor() public view returns (string memory _fn, string memory _fa, string memory _position, string memory _foodname, string memory _humidity, string memory _temperature){
        _fn = farmname;
        _fa =farmaddress;
        _position =position;
        _foodname =foodname;
        _humidity =humidity;
        _temperature =temperature;
        return (_fn, _fa, _position, _foodname,_humidity, _temperature);
    }

}