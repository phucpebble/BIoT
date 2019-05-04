package it.hp.biot;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
    private static final String BINARY = "608060405234801561001057600080fd5b5061011f806100206000396000f3fe6080604052348015600f57600080fd5b506004361060595760003560e01c806228737314605e5780633428a9fc14607a57806348f1447014609257806360fe47b11460ac5780636421d04b1460c65780636d4ce63c1460cc575b600080fd5b607860048036036020811015607257600080fd5b503560d2565b005b608060d7565b60408051918252519081900360200190f35b60786004803603602081101560a657600080fd5b503560dd565b60786004803603602081101560c057600080fd5b503560e2565b608060e7565b608060ed565b600455565b60045490565b600555565b600055565b60055490565b6000549056fea165627a7a72305820cb69ecc20541e8a6726ee1ed27c789ae8bdd0e19101d6ee43e55017baf28d6980029";

    public static final String FUNC_SETHUMIDITY = "setHumidity";

    public static final String FUNC_GETHUMIDITY = "getHumidity";

    public static final String FUNC_SETTEMPERATURE = "setTemperature";

    public static final String FUNC_SET = "set";

    public static final String FUNC_GETTEMPERATURE = "getTemperature";

    public static final String FUNC_GET = "get";

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

    public RemoteCall<TransactionReceipt> setHumidity(BigInteger _humidity) {
        final Function function = new Function(
                FUNC_SETHUMIDITY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_humidity)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getHumidity() {
        final Function function = new Function(FUNC_GETHUMIDITY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setTemperature(BigInteger _temperature) {
        final Function function = new Function(
                FUNC_SETTEMPERATURE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_temperature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> set(BigInteger x) {
        final Function function = new Function(
                FUNC_SET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(x)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getTemperature() {
        final Function function = new Function(FUNC_GETTEMPERATURE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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
