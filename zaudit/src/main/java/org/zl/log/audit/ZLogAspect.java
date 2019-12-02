/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zl.log.audit;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * logger on AOP
 * 
 * @author Leo
 * @version 0.1
 */
@Aspect
public class ZLogAspect {


	private Logger log = null;

	String loggerName;
	
	String[] protectParameters;
	
	private boolean showSession;
	
	private Set<String> sessionAttributs;
	
	private boolean showRequestParameters;
	
	public ZLogAspect(String loggerName, String[] protectParameters,String[] sessionAttributes,boolean showRequestParameters) {
		this(loggerName,protectParameters);
		this.sessionAttributs = new TreeSet<>(Arrays.asList(sessionAttributes));
		this.showRequestParameters = showRequestParameters;
		this.showSession = !sessionAttributs.isEmpty();
	}
	
	public ZLogAspect(String loggerName, String[] protectParameters) {
		this.log = LoggerFactory.getLogger(loggerName);
		this.protectParameters = protectParameters;
	}

	@Pointcut("@annotation(org.zl.log.audit.AuditLog)")
	public void logPointCut() {

	}

	@Before("logPointCut()")
	public void beforePointcut(JoinPoint joinPoint) {
		MethodSignature sign = (MethodSignature) joinPoint.getSignature();
		Method method = sign.getMethod();
		AuditLog annotation = method.getAnnotation(AuditLog.class);
		String desc = StringUtils.defaultIfEmpty(annotation.desc(), annotation.value());
		boolean showReqParams = showRequestParameters?true:annotation.requestParameters();
		if( RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
			HttpServletRequest  req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			log.info("call service[{}] ip:{}",desc,req.getRemoteAddr());
			Set<String> names = this.sessionAttributs;
			boolean showSession = this.showSession;
			if(annotation.sessionName()!=null && annotation.sessionName().length>0){
				names = new TreeSet<>();
				names.addAll(sessionAttributs);
				names.addAll(Arrays.asList( annotation.sessionName()));
				showSession = names.isEmpty();
			}
			if(showSession) {
				HttpSession session = req.getSession();
				for (String name : names) {
					Object value = session.getAttribute(name);
					log.info("{} session [{}] : {}",desc,name,value);
				}
			}
		}else {
			log.info("call service[{}] no web",desc);
		}
		String[] parameterNames = sign.getParameterNames();
		Object[] args = joinPoint.getArgs();
		for (int i =0 ,len=parameterNames.length;i < len ;i++){
			if(StringUtils.equalsAnyIgnoreCase( parameterNames[i],protectParameters)) {
				log.info("{} parameter[{}] {} : {}",desc,i,parameterNames[i],"{PROTECTED}");
			}else {
				if(showReqParams && args[i] instanceof HttpServletRequest ) {
					HttpServletRequest request = (HttpServletRequest)args[i];
					Map<String, String[]> parameterMap = request.getParameterMap();
					final int num = i;
					parameterMap.forEach((k,v) ->{
						if(StringUtils.equalsAnyIgnoreCase( k,protectParameters)) {
							log.info("{} parameter[{}] request[{}] : {}",desc,num,k,"{PROTECTED}");
						}else {
							log.info("{} parameter[{}] request[{}] : {}",desc,num,k,v);
						}
					});
				}else {
					log.info("{} parameter[{}] {} : {}",desc,i,parameterNames[i],args[i]);
				}
			}
        }
	}
	
	@AfterReturning(value="logPointCut()", returning = "ret")
	public void returning(JoinPoint joinPoint,Object ret) {
		MethodSignature sign = (MethodSignature) joinPoint.getSignature();
		Method method = sign.getMethod();
		AuditLog annotation = method.getAnnotation(AuditLog.class);
		String desc = StringUtils.defaultIfEmpty(annotation.desc(), annotation.value());
		log.info("called service[{}]:{}",desc,ret);
	}
	
	@AfterThrowing(value="logPointCut()", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint,Throwable ex) {
		MethodSignature sign = (MethodSignature) joinPoint.getSignature();
		Method method = sign.getMethod();
		AuditLog annotation = method.getAnnotation(AuditLog.class);
		String desc = StringUtils.defaultIfEmpty(annotation.desc(), annotation.value());
		log.info("called service[{}] failed: {}",desc,ex);
	}
}
