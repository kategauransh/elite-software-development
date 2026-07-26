package com.gauransh.saas.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import com.gauransh.saas.tenant.TenantContext;

@MappedSuperclass
public abstract class TenantEntity {
    
    private String tenantId;

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    @PrePersist
    public void onPrePersist() {
        if (this.tenantId == null) {
            this.tenantId = TenantContext.getTenantId();
        }
    }
}
