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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * annotation for logger
 * 
 * @author Leo
 * @version 0.1
 */
public @interface AuditLog {

	/**
	 * method name
	 * @return
	 */
	String name() default "";
	
	@AliasFor("name")
	String value() default "";
	
	/**
	 * description
	 * @return
	 */
	String desc() default "";
	
	/**
	 * print all parameter to log with request parameter
	 * @return {@code true} is print
	 */
	boolean requestParameters() default false;
	
	/**
	 * print attribute value to log for session
	 * @return
	 */
	String[] sessionName() default "";
	
	/**
	 * Logging results.
	 * if error ,them always to logging
	 * @return
	 */
	boolean logResults() default true;
}
