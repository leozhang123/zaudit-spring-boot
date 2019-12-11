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
package org.zl.log.audit.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * properties for the Z log
 * 
 * @author Leo
 * @version 0.1
 */
@ConfigurationProperties(prefix = AuditProperties.ZAUDIT_PREFIX)
public class AuditProperties {

	public static final String ZAUDIT_PREFIX = "org.zl.log.audit";
	
	/**
	 * these parameters is protect , they don't show real value
	 */
	private String[] protectParameters = {"password","loginpwd","newPwd","pwd"};
	
	/**
	 * logger name
	 */
	private String loggerName = "zaudit";

	/**
	 * there attribute values will be print to the logger 
	 */
	private String[] sessionAttributes = {};
	
	/**
	 * if this is set true and parameter type is web request,them print all parameter values to the logger of this web requst
	 */
	private boolean showRequestParameters;
	
	/**
	 * @return the protectParameters
	 */
	public String[] getProtectParameters() {
		return protectParameters;
	}

	/**
	 * @param protectParameters the protectParameters to set
	 */
	public void setProtectParameters(String[] protectParameters) {
		this.protectParameters = protectParameters;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @param loggerName the loggerName to set
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * @return the sessionAttributes
	 */
	public String[] getSessionAttributes() {
		return sessionAttributes;
	}

	/**
	 * @param sessionAttributes the sessionAttributes to set
	 */
	public void setSessionAttributes(String[] sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}

	/**
	 * @return the showRequestParameters
	 */
	public boolean isShowRequestParameters() {
		return showRequestParameters;
	}

	/**
	 * @param showRequestParameters the showRequestParameters to set
	 */
	public void setShowRequestParameters(boolean showRequestParameters) {
		this.showRequestParameters = showRequestParameters;
	}
}
