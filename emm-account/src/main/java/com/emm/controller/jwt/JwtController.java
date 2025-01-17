package com.emm.controller.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.emm.config.AppConfig;
import com.emm.entity.User;
import com.emm.entity.information.StandardResponseBody;
import com.emm.entity.information.ResponseEnum;
import com.emm.entity.token.TokenInfo;
import com.emm.entity.token.UserTokenThreadLocal;
import com.emm.util.token.JWTTools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class JwtController implements HandlerInterceptor {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static AppConfig appConfig;
    @Autowired
    public JwtController(AppConfig appConfig) {
        JwtController.appConfig = appConfig;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        //获取本次请求路径
        String requestUri = request.getRequestURI();
        log.info("requestUri: {}", requestUri);
        //定义不需要处理的路径（也可以在配置类中配置）
        String[] urls = new String[] {
                "/user/login",
                "/user/register",
                "/index.html",
                "/error",
                "/email/register/{VerificationCodeToken}/{code}",
                "/email/verify/{VerificationCodeToken}/{code}",
                "/email/code"
        };
        //判断本次是否需要处理
        if (urlCheck(requestUri, urls)) {
            return true;
        }
        //获取token信息
        String accessToken = request.getHeader(appConfig.getWebHeaderAccessToken());
        String refreshToken = request.getHeader(appConfig.getWebHeaderRefreshToken());
        log.info("{}: {}", appConfig.getWebHeaderAccessToken(), accessToken);
        log.info("{}: {}", appConfig.getWebHeaderRefreshToken(), refreshToken);

        String refreshAccessTokenUrl = "/token/refresh-access-token";
        String updateRefreshTokenUrl = "/token/refresh-refresh-token";
        StandardResponseBody<?> res = null;
        TokenInfo<User> userTokenInfo;
        if (urlCheck(requestUri, refreshAccessTokenUrl)) {
            log.info("更新 access_token");
            try {
                userTokenInfo = JWTTools.parseUserJWT(refreshToken);
                response.addHeader(appConfig.getWebHeaderAccessToken(), JWTTools.createUserUpdateJWT(userTokenInfo.getData()));
                res = StandardResponseBody.success();
                log.info("更新 access_token 成功");
            } catch (SignatureVerificationException e) {
                log.error("jwt verification error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_SIGNATURE_VERIFICATION);
            } catch (TokenExpiredException e) {
                log.error("jwt token expired error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_EXPIRED);
            } catch (AlgorithmMismatchException e) {
                log.error("jwt algorithm mismatch error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_ALGORITHM_MISMATCH);
            } catch (Exception e) {
                log.error("jwt parse error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_INVALID);
            }
        } else if (urlCheck(requestUri, updateRefreshTokenUrl)) {
            if (appConfig.isALLOW_UPDATE_REFRESH_TOKENS()) {
                try {
                    userTokenInfo = JWTTools.parseUserJWT(refreshToken);
                    response.addHeader(appConfig.getWebHeaderAccessToken(), JWTTools.createUserAccessJWT(userTokenInfo.getData()));
                    response.addHeader(appConfig.getWebHeaderRefreshToken(), JWTTools.createUserRefreshJWT(userTokenInfo.getData()));
                    res = StandardResponseBody.success();
                    log.info("更新 access_token 和 refresh_token 成功");
                } catch (SignatureVerificationException e) {
                    log.error("jwt verification error", e);
                    res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_SIGNATURE_VERIFICATION);
                } catch (TokenExpiredException e) {
                    log.error("jwt token expired error", e);
                    res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_EXPIRED);
                } catch (AlgorithmMismatchException e) {
                    log.error("jwt algorithm mismatch error", e);
                    res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_ALGORITHM_MISMATCH);
                } catch (Exception e) {
                    log.error("jwt parse error", e);
                    res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_INVALID);
                }
            } else {
                log.error("不支持更新refresh_token");
                res = StandardResponseBody.customInfo(ResponseEnum.REFRESH_TOKEN_UPDATE_NOT_ALLOWED);
            }
        } else {
            try {
                userTokenInfo = JWTTools.parseUserJWT(accessToken);
                UserTokenThreadLocal.set(userTokenInfo);
                return true;
            } catch (SignatureVerificationException e) {
                log.error("jwt verification error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.ACCESS_TOKEN_SIGNATURE_VERIFICATION);
            } catch (TokenExpiredException e) {
                log.error("jwt token expired error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.ACCESS_TOKEN_EXPIRED);
            } catch (AlgorithmMismatchException e) {
                log.error("jwt algorithm mismatch error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.ACCESS_TOKEN_ALGORITHM_MISMATCH);
            } catch (Exception e) {
                log.error("jwt parse error", e);
                res = StandardResponseBody.customInfo(ResponseEnum.ACCESS_TOKEN_INVALID);
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(res);
        return false;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        UserTokenThreadLocal.remove();
    }

    /**
     * 判断路径是否需要放行
     * @param urls 请求白名单
     * @param requestUri 请求路径
     * @return
     */
    public boolean urlCheck(String requestUri, String[] urls) {
        for (String url : urls) {
            if (urlCheck(requestUri, url)) {
                return true;
            }
        }
        return false;
    }

    public boolean urlCheck(String requestUri, String url) {
        return PATH_MATCHER.match(url, requestUri);
    }
}
