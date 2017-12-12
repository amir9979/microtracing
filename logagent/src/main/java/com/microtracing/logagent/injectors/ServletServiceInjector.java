package com.microtracing.logagent.injectors;

import com.microtracing.logagent.LogTraceConfig;


import com.microtracing.logagent.CallInjector;

public class ServletServiceInjector implements CallInjector {
	LogTraceConfig config;

	protected  String methodCallBefore 
        = "  com..microtracing.tracespan.web.TraceHelper _$helper = new com..microtracing.tracespan.web.TraceHelper(); \n"
        + "  boolean _$ignoreTrace = false; \n"
        + "  com..microtracing.tracespan.Span _$span = null; \n"
        + "  if ($0.getClass().getName().startsWith(\"weblogic.\")||$0.getClass().getName().startsWith(\"apache.\")) { _$ignoreTrace = true; } \n"
        + "  if (!_$ignoreTrace) _$ignoreTrace = _$helper.ignoreTrace($1); \n"
        + "  if (!_$ignoreTrace){ \n"
        + "     _$span = _$helper.beforeService($1, $2);  \n"
        + "     $1 = _$helper.wrapRequest($1);  \n"
        + "     $2 = _$helper.wrapResponse($1, $2);  \n"
        + "  }  \n"
        + "  try{ \n";

    
	protected   String methodCallAfter  
        = "  }finally{ \n"
        + "    if (!_$ignoreTrace) _$helper.afterService($1, $2, _$span); \n"
        + "  }\n";
                            
	public ServletServiceInjector(LogTraceConfig config) {
		config.addProfileClass("weblogic.servlet.internal.StubSecurityHelper");//for weblogic
		config.addProfileClass("org.apache.tomcat.websocket.server.WsFilter");//for tomcat
		this.config = config;
	}
	
    @Override
    public boolean isNeedCallInject(String className, String methodName){
        return ("javax.servlet.Servlet".equals(className) && "service".equals(methodName)) ||
        		("javax.servlet.FilterChain".equals(className) && "doFilter".equals(methodName));
    }

	@Override
	public String getMethodCallBefore(String className, String methodName) {
		return methodCallBefore;
	}

	@Override
	public String getMethodCallAfter(String className, String methodName) {
		return methodCallAfter;
	}	

}
