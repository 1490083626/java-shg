package com.wdl.shg.dto;


public class WechatLoginRequest {
	//微信登录code
    private String code;
    //用户非敏感字段
    private String rawData;
    //签名
    private String signature;
    //用户敏感字段
    private String encryptedData;
    //解密向量
    private String iv;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRawData() {
		return rawData;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
    
}