package com.gauransh.saas.repository;

import com.gauransh.saas.tenant.TenantContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantAwareAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* org.springframework.data.repository.Repository+.*(..))")
    public void beforeRepositoryCall() {
        Session session = entityManager.unwrap(Session.class);
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            // Configure Hibernate dynamic tenant filter
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        }
    }
}
