package com.tea.server.core.aop;

import com.alibaba.fastjson.JSON;
import com.tea.server.utils.CoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class RestAdvisor {

    @Around(value = "com.tea.server.core.aop.CommPointCut.rest()")
    public Object filter(ProceedingJoinPoint joinPoint) throws Throwable {
        String id = null;
        if (log.isDebugEnabled()) {
            id = CoreUtil.shortUUID();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String paramJson;
            try {
                paramJson = JSON.toJSONString(Arrays.stream(joinPoint.getArgs()).filter(param -> !(param instanceof HttpServletRequest))
                        .filter(param -> !(param instanceof HttpServletResponse))
                        .filter(param -> !(param instanceof MultipartFile))
                        .collect(Collectors.toList()));
            } catch (Exception e) {
                paramJson = "[" + Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", ")) + "]";
            }
            log.debug("requestBody  id: -->{}<--, url: {}, method: {}.{}, param: {}"
                    , id
                    , request.getRequestURI()
                    , joinPoint.getTarget().getClass().getName()
                    , joinPoint.getSignature().getName()
                    , paramJson);
        }
        Object res = joinPoint.proceed();
        if (log.isDebugEnabled()) {
            String resJson;
            if (res instanceof List && ((List) res).size() > 10) {
                resJson = "List(" + ((List) res).size() + ")";
            } else {
                resJson = JSON.toJSONString(res);
            }
            log.debug("responseBody id: -->{}<--, return:{}", id, resJson);
        }
        return res;
    }

}
