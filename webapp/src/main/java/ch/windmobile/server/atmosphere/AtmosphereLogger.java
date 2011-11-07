package ch.windmobile.server.atmosphere;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.atmosphere.websocket.WebSocketEventListener.WebSocketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtmosphereLogger implements AtmosphereResourceEventListener {
    protected static final Logger log = LoggerFactory.getLogger(AtmosphereLogger.class);

    public AtmosphereLogger() {
    }

    public void onSuspend(final AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.info("onSuspend(): {}:{}", event.getResource().getRequest().getRemoteAddr(), event.getResource().getRequest().getRemotePort());
    }

    public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.info("onResume(): {}:{}", event.getResource().getRequest().getRemoteAddr(), event.getResource().getRequest().getRemotePort());
    }

    public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.info("onDisconnect(): {}:{}", event.getResource().getRequest().getRemoteAddr(), event.getResource().getRequest().getRemotePort());
    }

    public void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.info("onBroadcast(): {}", event.getMessage());
    }

    public void onThrowable(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.warn("onThrowable(): {}", event);
    }

    public void onHandshake(WebSocketEvent event) {
        log.info("onHandshake(): {}", event);
    }

    public void onMessage(WebSocketEvent event) {
        log.info("onMessage(): {}", event);
    }

    public void onClose(WebSocketEvent event) {
        log.info("onClose(): {}", event);
    }

    public void onControl(WebSocketEvent event) {
        log.info("onControl(): {}", event);
    }

    public void onDisconnect(WebSocketEvent event) {
        log.info("onDisconnect(): {}", event);
    }

    public void onConnect(WebSocketEvent event) {
        log.info("onConnect(): {}", event);
    }
}