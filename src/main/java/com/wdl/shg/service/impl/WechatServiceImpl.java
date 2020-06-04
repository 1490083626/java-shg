package com.wdl.shg.service.impl;

import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdl.shg.dto.RawDataDO;
import com.wdl.shg.dto.WechatLoginRequest;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.service.WechatService;
import com.wdl.shg.service.WechatUserService;
import com.wdl.shg.util.HttpClientUtils;

@Service
public class WechatServiceImpl implements WechatService {
	//小程序配置
    private static final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String APPID = "wx0cc85d32a317c597";
    private static final String SECRET = "fd1761ff8b002be0e781bfcbbc671cfc";
    private static final String GRANT_TYPE = "authorization_code";
//    private static final String  = "authoriz;ation_code";
    private static Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

	@Autowired
	private WechatUserService wechatUserService;
    @Override
	/**
	 * loginRequest包含：
	  	1.code //临时登入凭证
		2.rawData //用户非敏感信息，头像和昵称之类的
		3.signature //签名
		4.encryteDate //用户敏感信息，需要解密，（包含unionID）
		5.iv //解密算法的向量
	 */

    //1.根据前端传递过来的loginRequest,获取openId和SessionKey
    //2.根据loginRequest, sessionKey, openId生成用户DO
    //3.根据openid查询用户，为空插入，存在则更新
    //4.给前端返回token，维护登录态
    public Map<String, Object> getUserInfoMap(WechatLoginRequest loginRequest) throws Exception {
        Map<String, Object> userInfoMap = new HashMap<>();
        // 日志对象
        logger.info("Start get SessionKey，loginRequest的数据为：" + JSONObject.toJSONString(loginRequest));
        JSONObject sessionKeyOpenId = getSessionKeyOrOpenId(loginRequest.getCode());
        // 这里的ErrorCodeEnum是自定义错误字段，可以删除，用自己的方式处理
//        Assert.isTrue(sessionKeyOpenId != null, ErrorCodeEnum.P01.getCode());

        // 获取openId && sessionKeygetToken
        String openId = sessionKeyOpenId.getString("openid");
        
        // 这里的ErrorCodeEnum是自定义错误字段，可以删除，用自己的方式处理
//        Assert.isTrue(openId != null, ErrorCodeEnum.P01.getCode());
        String sessionKey = sessionKeyOpenId.getString("session_key");
        WechatUserDO insertOrUpdateDO = buildWechatUserDO(loginRequest, sessionKey, openId);

        // 根据code保存openId和sessionKey
        JSONObject sessionObj = new JSONObject();
        sessionObj.put("openId", openId);
        sessionObj.put("sessionKey", sessionKey);
        // 这里的set方法，自行导入自己项目的Redis，key自行替换，这里10表示10天
//        stringJedisClientTem.set(WechatRedisPrefixConstant.USER_OPPEN_ID_AND_SESSION_KEY_PREFIX + loginRequest.getCode(),
//                sessionObj.toJSONString(), 10, TimeUnit.DAYS);

        // 根据openid查询用户
        WechatUserDO user = wechatUserService.getWechatUserByOpenId(openId);
        if (user == null) {
            // 用户不存在，insert用户，可以加分布式锁，防止insert重复用户，看自己的业务
//            if (setLock(WechatRedisPrefixConstant.INSERT_USER_DISTRIBUTED_LOCK_PREFIX + openId, "1", 10)) {
              // 用户入库
              insertOrUpdateDO.setToken(getToken());
              wechatUserService.register(insertOrUpdateDO);
              userInfoMap.put("token", insertOrUpdateDO.getToken());
//            }
        } else {
            userInfoMap.put("token", getToken());
            //TODO 已存在，做已存在的处理，如更新用户的头像，昵称等，根据openID更新
//            wechatUserService.updateByOpenId(insertOrUpdateDO);
        }
        if(user != null && user.getId() != null && user.getId() != -1L) {
        	Long userId = user.getId();
        	userInfoMap.put("userId", userId);
        }
        userInfoMap.put("openId", openId);
        return userInfoMap;
    }
  
    // 这里的JSONObject是阿里的fastjson，maven导入
    private JSONObject getSessionKeyOrOpenId(String code) throws Exception {
        Map<String, String> requestUrlParam = new HashMap<>();
        // 小程序appId
        requestUrlParam.put("appid", APPID);
        // 小程序secret，自己补充
        requestUrlParam.put("secret", SECRET);
        // 小程序端返回的code
        requestUrlParam.put("js_code", code);
        // 默认参数
        requestUrlParam.put("grant_type", GRANT_TYPE);

        // 发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpClientUtils.doPost(REQUEST_URL, requestUrlParam);
        return JSON.parseObject(result);
    }
  
    private WechatUserDO buildWechatUserDO(WechatLoginRequest loginRequest, String sessionKey, String openId){
        WechatUserDO wechatUserDO = new WechatUserDO();
        wechatUserDO.setOpenId(openId);

        if (loginRequest.getRawData() != null) {
            RawDataDO rawDataDO = JSON.parseObject(loginRequest.getRawData(), RawDataDO.class);
            wechatUserDO.setNickname(rawDataDO.getNickName());
            wechatUserDO.setAvatarUrl(rawDataDO.getAvatarUrl());
            wechatUserDO.setGender(rawDataDO.getGender());
            wechatUserDO.setCity(rawDataDO.getCity());
            wechatUserDO.setCountry(rawDataDO.getCountry());
            wechatUserDO.setProvince(rawDataDO.getProvince());
        }

        // 解密加密信息，获取unionID
//        if (loginRequest.getEncryptedData() != null){
//            JSONObject encryptedData = getEncryptedData(loginRequest.getEncryptedData(), sessionKey, loginRequest.getIv());
//            if (encryptedData != null){
//                String unionId = encryptedData.getString("unionId");
//                wechatUserDO.setUnionId(unionId);
//            }
//        }

        return wechatUserDO;
    }
  
//    private JSONObject getEncryptedData(String encryptedData, String sessionkey, String iv) {
//        // 被加密的数据
//        byte[] dataByte = Base64.decode(encryptedData);
//        // 加密秘钥
//        byte[] keyByte = Base64.decode(sessionkey);
//        // 偏移量
//        byte[] ivByte = Base64.decode(iv);
//        try {
//            // 如果密钥不足16位，那么就补足.这个if中的内容很重要
//            int base = 16;
//            if (keyByte.length % base != 0) {
//                int groups = keyByte.length / base + 1;
//                byte[] temp = new byte[groups * base];
//                Arrays.fill(temp, (byte) 0);
//                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
//                keyByte = temp;
//            }
//            // 初始化
//            Security.addProvider(new BouncyCastleProvider());
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
//            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
//            parameters.init(new IvParameterSpec(ivByte));
//            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
//            byte[] resultByte = cipher.doFinal(dataByte);
//            if (null != resultByte && resultByte.length > 0) {
//                String result = new String(resultByte, "UTF-8");
//                return JSONObject.parseObject(result);
//            }
//        } catch (Exception e) {
//            logger.error("解密加密信息报错", e.getMessage());
//        }
//        return null;
//    }
  
//    private boolean setLock(String key, String value, long expire) throws Exception {
//        boolean result = stringJedisClientTem.setNx(key, value, expire, TimeUnit.SECONDS);
//        return result;
//    }
  
    private String getToken() throws Exception {
        // 这里自定义token生成策略，可以用UUID+sale进行MD5
        return "shg";
    }


}