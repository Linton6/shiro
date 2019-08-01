package com.imooc.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @Author Linton
 * @Date 2019/8/1 12:27
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description 优化性能，先从request中去sessionId，取不到，再从redis中取session。极大的减少了redis的访问次数，提高性能。
 */

public class CustomSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession (SessionKey sessionKey) throws UnknownSessionException {
// ???sessionKey 和sessionId有什么关系
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null) {

            Session session =  (Session) request.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
    }

}

