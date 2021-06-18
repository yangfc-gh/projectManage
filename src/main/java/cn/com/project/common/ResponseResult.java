package cn.com.project.common;

/**
 * 统一响应数据格式
 * @author yangfc
 *
 */
public class ResponseResult {
	
	/**
	 * 请求结果
	 */
	private boolean result;
	
	/**
	 * 结果描述
	 */
	private String message;

	/**
	 * 相应的状态码
	 */
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 响应信息
	 */
	private Object data;
	
	public ResponseResult() {
		
	}
	/**
	 * 请求失败（有错误）或请求成功但没有(不用)返回数据时，使用此构建
	 * @param result
	 * @param message
	 */
	public ResponseResult(boolean result, String message) {
		this.setResult(result);
		this.setMessage(message);
	}

	public ResponseResult(boolean result) {
		this.setResult(result);
		this.setMessage(result ? "操作成功" : "操作失败");
	}
	
	/**
	 * 请求成功返回数据时，使用此构建
	 * @param data
	 */
	public ResponseResult(Object data) {
		this.setResult(true);
		this.setData(data);
	}
	@Override
	public String toString() {
		return "ResponseResult [result=" + result + ", message=" + message + ", data=" + data + "]";
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
