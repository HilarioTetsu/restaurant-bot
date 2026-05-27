package com.restaurante.interaccion.filter;

import com.restaurante.shared.tenant.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest httpServletRequest) {
            // Por ahora simulamos la extraccion mediante un Header HTTP (X-Tenant-ID)
            // Mas adelante, aqui parsearemos el JSON de Meta para extraer el numero del negocio receptor
            String tenantId = httpServletRequest.getHeader(TENANT_HEADER);

            if (tenantId != null && !tenantId.trim().isEmpty()) {
                TenantContext.setCurrentTenant(tenantId);
            } else {
                TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
            }
        }

        try {
            // Continua con la ejecucion de la peticion HTTP
            chain.doFilter(request, response);
        } finally {
            // CRUCIAL: Limpiamos el contexto al terminar la peticion.
            // Como Tomcat reutiliza los hilos de ejecucion, si no limpiamos el ThreadLocal,
            // podriamos contaminar los datos de una peticion posterior de otro restaurante.
            TenantContext.clear();
        }
    }
}