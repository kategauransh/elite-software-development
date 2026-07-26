package com.gauransh.saas.security;

import com.gauransh.saas.tenant.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TenantJwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Extract X-Tenant-ID header (mimicking JWT claim extraction for simple setup)
        String tenantId = httpRequest.getHeader("X-Tenant-ID");
        
        if (tenantId != null) {
            TenantContext.setTenantId(tenantId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // Guarantee context cleaning to prevent ThreadLocal pollution in connection pools
            TenantContext.clear();
        }
    }
}
