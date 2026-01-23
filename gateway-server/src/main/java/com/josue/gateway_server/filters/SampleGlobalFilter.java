package com.josue.gateway_server.filters;

import jakarta.servlet.*;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Filtro global.
@Component
public class SampleGlobalFilter implements Filter, Ordered {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
