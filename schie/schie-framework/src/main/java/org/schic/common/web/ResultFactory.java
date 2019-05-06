package org.schic.common.web;

public class ResultFactory {

    private ResultFactory() {
    }

    protected static final String SUCCESSCODE = "0";
	protected static final String ERRORCODE = "-1";

    public static Result getSuccessResult() {
        return getResultBean(SUCCESSCODE, "成功");
    }

    public static Result getSuccessResult(String resultMessage) {
        return getResultBean(SUCCESSCODE, resultMessage);
    }

    public static Result getErrorResult(String resultMessage) {
        return getResultBean(ERRORCODE, resultMessage);
    }

    private static Result getResultBean(String resultCode, String resultMessage) {

        Result result = ResultBean.result.getResult();

        result.put("CODE", resultCode);
        result.put("MESSAGE", resultMessage);
        return result;
    }

    public static Result getResultBean(String resultMessage, Object o) {

        Result result = ResultBean.result.getResult();

        result.put("CODE", SUCCESSCODE);
        result.put("MESSAGE", resultMessage);
        result.put("Object", o);
        return result;
    }

    private enum ResultBean {
        result;
        Result getResult() {
            return new Result();
        }
    }
}
