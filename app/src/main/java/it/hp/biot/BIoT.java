package it.hp.biot;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.0.
 */
public class BIoT extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610fe3806100206000396000f3fe608060405234801561001057600080fd5b50600436106100a95760003560e01c80637c587bfc116100715780637c587bfc146103c1578063a113efa3146103c9578063adccea12146103d1578063b569ad48146103d9578063da80dd8e1461047f578063de6621a014610487576100a9565b806309218e91146100ae578063204b058f1461012b57806341bcead0146101335780635716df1a1461013b5780636ad749ee14610143575b600080fd5b6100b66107c4565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100f05781810151838201526020016100d8565b50505050905090810190601f16801561011d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6100b6610852565b6100b66108ac565b6100b6610907565b61014b61099e565b6040518080602001806020018060200180602001806020018060200187810387528d818151815260200191508051906020019080838360005b8381101561019c578181015183820152602001610184565b50505050905090810190601f1680156101c95780820380516001836020036101000a031916815260200191505b5087810386528c5181528c516020918201918e019080838360005b838110156101fc5781810151838201526020016101e4565b50505050905090810190601f1680156102295780820380516001836020036101000a031916815260200191505b5087810385528b5181528b516020918201918d019080838360005b8381101561025c578181015183820152602001610244565b50505050905090810190601f1680156102895780820380516001836020036101000a031916815260200191505b5087810384528a5181528a516020918201918c019080838360005b838110156102bc5781810151838201526020016102a4565b50505050905090810190601f1680156102e95780820380516001836020036101000a031916815260200191505b5087810383528951815289516020918201918b019080838360005b8381101561031c578181015183820152602001610304565b50505050905090810190601f1680156103495780820380516001836020036101000a031916815260200191505b5087810382528851815288516020918201918a019080838360005b8381101561037c578181015183820152602001610364565b50505050905090810190601f1680156103a95780820380516001836020036101000a031916815260200191505b509c5050505050505050505050505060405180910390f35b6100b6610d1f565b6100b6610d7a565b6100b6610dd5565b61047d600480360360208110156103ef57600080fd5b810190602081018135600160201b81111561040957600080fd5b82018360208201111561041b57600080fd5b803590602001918460018302840111600160201b8311171561043c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610e30945050505050565b005b6100b6610e47565b61047d600480360360c081101561049d57600080fd5b810190602081018135600160201b8111156104b757600080fd5b8201836020820111156104c957600080fd5b803590602001918460018302840111600160201b831117156104ea57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561053c57600080fd5b82018360208201111561054e57600080fd5b803590602001918460018302840111600160201b8311171561056f57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156105c157600080fd5b8201836020820111156105d357600080fd5b803590602001918460018302840111600160201b831117156105f457600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561064657600080fd5b82018360208201111561065857600080fd5b803590602001918460018302840111600160201b8311171561067957600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156106cb57600080fd5b8201836020820111156106dd57600080fd5b803590602001918460018302840111600160201b831117156106fe57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561075057600080fd5b82018360208201111561076257600080fd5b803590602001918460018302840111600160201b8311171561078357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610e9f945050505050565b6004805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b820191906000526020600020905b81548152906001019060200180831161082d57829003601f168201915b505050505081565b60018054604080516020600284861615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b6003805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b60008054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156109935780601f1061096857610100808354040283529160200191610993565b820191906000526020600020905b81548152906001019060200180831161097657829003601f168201915b505050505090505b90565b60608060608060608060018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a3d5780601f10610a1257610100808354040283529160200191610a3d565b820191906000526020600020905b815481529060010190602001808311610a2057829003601f168201915b505060038054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152969c5091945092508401905082828015610ace5780601f10610aa357610100808354040283529160200191610ace565b820191906000526020600020905b815481529060010190602001808311610ab157829003601f168201915b505060048054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152969b5091945092508401905082828015610b5f5780601f10610b3457610100808354040283529160200191610b5f565b820191906000526020600020905b815481529060010190602001808311610b4257829003601f168201915b505060028054604080516020601f60001961010060018716150201909416859004938401819004810282018101909252828152969a5091945092508401905082828015610bed5780601f10610bc257610100808354040283529160200191610bed565b820191906000526020600020905b815481529060010190602001808311610bd057829003601f168201915b505060058054604080516020601f6002600019610100600188161502019095169490940493840181900481028201810190925282815296995091945092508401905082828015610c7e5780601f10610c5357610100808354040283529160200191610c7e565b820191906000526020600020905b815481529060010190602001808311610c6157829003601f168201915b505060068054604080516020601f6002600019610100600188161502019095169490940493840181900481028201810190925282815296985091945092508401905082828015610d0f5780601f10610ce457610100808354040283529160200191610d0f565b820191906000526020600020905b815481529060010190602001808311610cf257829003601f168201915b5093945050505050909192939495565b6000805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b6005805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b6006805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b8051610e43906000906020840190610f1f565b5050565b6002805460408051602060018416156101000260001901909316849004601f8101849004840282018401909252818152929183018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b8551610eb2906001906020890190610f1f565b508451610ec6906003906020880190610f1f565b508351610eda906004906020870190610f1f565b508251610eee906002906020860190610f1f565b508151610f02906005906020850190610f1f565b508051610f16906006906020840190610f1f565b50505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610f6057805160ff1916838001178555610f8d565b82800160010185558215610f8d579182015b82811115610f8d578251825591602001919060010190610f72565b50610f99929150610f9d565b5090565b61099b91905b80821115610f995760008155600101610fa356fea165627a7a723058205a9ee9caa19a430e7acd03237ba4b9dbf21e4454889a78e3b81a689c174fe4580029";

    public static final String FUNC_POSITION = "position";

    public static final String FUNC_FARMNAME = "farmname";

    public static final String FUNC_FARMADDRESS = "farmaddress";

    public static final String FUNC_GETFARMLOGO = "getFarmLogo";

    public static final String FUNC_GETFARM_FOODINFOR = "getFarm_FoodInfor";

    public static final String FUNC_FARMLOGO = "farmlogo";

    public static final String FUNC_HUMIDITY = "humidity";

    public static final String FUNC_TEMPERATURE = "temperature";

    public static final String FUNC_SETFARMLOGO = "setFarmLogo";

    public static final String FUNC_FOODNAME = "foodname";

    public static final String FUNC_SETFARM_FOODINFOR = "setFarm_FoodInfor";

    @Deprecated
    protected BIoT(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BIoT(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BIoT(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BIoT(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> position() {
        final Function function = new Function(FUNC_POSITION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> farmname() {
        final Function function = new Function(FUNC_FARMNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> farmaddress() {
        final Function function = new Function(FUNC_FARMADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getFarmLogo() {
        final Function function = new Function(FUNC_GETFARMLOGO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple6<String, String, String, String, String, String>> getFarm_FoodInfor() {
        final Function function = new Function(FUNC_GETFARM_FOODINFOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple6<String, String, String, String, String, String>>(
                new Callable<Tuple6<String, String, String, String, String, String>>() {
                    @Override
                    public Tuple6<String, String, String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, String, String, String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (String) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<String> farmlogo() {
        final Function function = new Function(FUNC_FARMLOGO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> humidity() {
        final Function function = new Function(FUNC_HUMIDITY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> temperature() {
        final Function function = new Function(FUNC_TEMPERATURE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setFarmLogo(String _farmLogo) {
        final Function function = new Function(
                FUNC_SETFARMLOGO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_farmLogo)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> foodname() {
        final Function function = new Function(FUNC_FOODNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setFarm_FoodInfor(String _farmname, String _farmaddress, String _position, String _foodname, String _humidity, String _temperature) {
        final Function function = new Function(
                FUNC_SETFARM_FOODINFOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_farmname), 
                new org.web3j.abi.datatypes.Utf8String(_farmaddress), 
                new org.web3j.abi.datatypes.Utf8String(_position), 
                new org.web3j.abi.datatypes.Utf8String(_foodname), 
                new org.web3j.abi.datatypes.Utf8String(_humidity), 
                new org.web3j.abi.datatypes.Utf8String(_temperature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static BIoT load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BIoT(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BIoT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BIoT(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BIoT load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BIoT(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BIoT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BIoT(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BIoT> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BIoT.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BIoT> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BIoT.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<BIoT> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BIoT.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BIoT> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BIoT.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
