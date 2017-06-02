package com.askdog.web.utils;

import com.aliyun.oss.common.utils.BinaryUtil;
import com.google.common.base.Preconditions;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class AliyunOSSUtils {

    public static boolean verifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody) throws UnsupportedEncodingException {
        String content = getContent(request, ossCallbackBody);

        byte[] authorization = Base64Utils.decodeFromString(request.getHeader("Authorization"));
        String publicKey = getPublicKey(request);
        return doCheck(content, authorization, publicKey);
    }

    private static String getContent(HttpServletRequest request, String ossCallbackBody) throws UnsupportedEncodingException {
        String authStr = java.net.URLDecoder.decode(request.getRequestURI(), "UTF-8");
        if (!StringUtils.isEmpty(request.getQueryString())) {
            authStr += "?" + request.getQueryString();
        }
        authStr += "\n" + ossCallbackBody;
        return authStr;
    }

    private static String getPublicKey(HttpServletRequest request) {
        String pubKeyAddr = new String(Base64Utils.decodeFromString(request.getHeader("x-oss-pub-key-url")));
        Preconditions.checkState(pubKeyAddr.startsWith("http://gosspublic.alicdn.com/") || pubKeyAddr.startsWith("https://gosspublic.alicdn.com/"));
        String retString = new RestTemplate().getForObject(pubKeyAddr, String.class);
        retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
        retString = retString.replace("-----END PUBLIC KEY-----", "");
        return retString;
    }

    private static boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            return signature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}