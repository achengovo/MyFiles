package com.achengovo.myfiles.myfilesweb.filter;

import com.achengovo.lightning.client.Client;
import com.achengovo.lightning.client.filter.Filter;
import com.achengovo.lightning.commons.message.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;
@Slf4j
public class LogFilter implements Filter {
//    Logger log=org.slf4j.LoggerFactory.getLogger(LogFilter.class);
    /**
     * Lightning调用日志
     * @param client 客户端
     * @param rpcRequest
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Object filter(Client client, RpcRequest rpcRequest) throws ExecutionException, InterruptedException {
        Object result = client.request(rpcRequest);
        log.info("Server{Host{},Port{}},{},Result{{}}",client.getHost(),client.getPort(),rpcRequest,result);
        return result;
    }
}
