package com.microtracing.logagent.injectors;
import com.microtracing.logagent.LogTraceConfig;
import com.microtracing.logagent.MethodInjector;
public class SpanMethodInjector implements MethodInjector{

    private final static String[][] methodVariables = new String[][]{
        {"com.microtracing.tracespan.Tracer","_$tracer"},
        {"com.microtracing.tracespan.Span","_$span"}
    };
    
	private final static String initAndStartSpan 
        = "  _$tracer = com.microtracing.tracespan.Tracer.getTracer(); \n"
        + "  _$span =  _$tracer.getCurrentSpan(); \n"
        + "  String _$spanName = \"%1$s\"; \n"
        + "  if (!_$span.isStarted()){ \n"
        + "	   _$span.setName(_$spanName); \n"
        +"     _$span.setAutoPrintLog(%2$b);  \n"
        +"     _$span.start();  \n"
        + "	 }else if(!_$spanName.equals(_$span.getName())) { \n "
        +"     _$span = _$tracer.createSpan(_$spanName);  \n"
        +"     _$span.setAutoPrintLog(%2$b);  \n"
        +"     _$span.start();  \n"
        + "  } \n";

	private final static String getSpan
        = "  com.microtracing.tracespan.Tracer _$tracer = com.microtracing.tracespan.Tracer.getTracer(); \n"
        + "  com.microtracing.tracespan.Span _$span =  _$tracer.getCurrentSpan(); \n"
        + "  String _$spanName = \"%1$s\"; \n"
        + "  if (!_$spanName.equals(_$span.getName())) { \n "
        +"     _$span = null;  \n"
        + "  } \n";


    private final static  String methodProcessStart 
        = "  { \n"
        +      initAndStartSpan
        + "  } \n";
                                                 
    private final static  String methodProcessReturn  
        = "  { \n"
        // can use methodVariables
        //+      getSpan 
        + "    if(_$span != null) _$span.stop(); \n"
        + "  } \n";
                                                    
    private final static  String methodProcessException 
        = "  { \n"
        // cannot use methodVariables
        +      getSpan 
        + "    if(_$span != null) { \n"
        + "       _$span.addException(_$e); \n"
        + "       _$span.stop(); \n"
        + "    } \n"
        + "    throw _$e;  \n"
        + "  } \n";
                                                    
    private final static  String methodProcessFinally ="";
    /* stopped in return & exception catch block
        = "  { \n"
        +      getSpan 
        + "    if(_$span != null) _$span.stop(); \n"
        + "  } \n";
    */                            
    
    
    private LogTraceConfig config;
	private String spanName = "METHOD:%1$s.%2$s";
	private boolean autoPrintLog = true;
    	    
    public SpanMethodInjector(LogTraceConfig config){
        this.config = config;
    }
    
    public void setSpanName(String spanName) {
    	this.spanName = spanName;
    }
    
    public void setAutoPrintLog(boolean autoPrintLog) {
    	this.autoPrintLog = autoPrintLog;
    }
            
    @Override
    public  String[][] getMethodVariables(String className, String methodName){
        return methodVariables;
    }
    
    @Override
    public  String getMethodProcessStart(String className, String methodName){
    	String realSpanName =  String.format(spanName, className, methodName);
        return String.format(methodProcessStart, realSpanName, autoPrintLog);
    }
    
    @Override
    public  String getMethodProcessReturn(String className, String methodName){
    	String realSpanName =  String.format(spanName, className, methodName);
        return String.format(methodProcessReturn, realSpanName, autoPrintLog);
    }    
    
    @Override
    public  String getMethodProcessException(String className, String methodName){
    	String realSpanName =  String.format(spanName, className, methodName);
        return String.format(methodProcessException, realSpanName, autoPrintLog);
    }    

    @Override
    public  String getMethodProcessFinally(String className, String methodName){
    	String realSpanName =  String.format(spanName, className, methodName);
        return String.format(methodProcessFinally, realSpanName, autoPrintLog);
    }    
    
    
    
    @Override
    public boolean isNeedProcessInject(String className, String methodName){
        return config.isNeedProcessInject(className, methodName);
    }
    
}