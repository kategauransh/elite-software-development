# Scalable Multi-Tenant B2B SaaS Backend

A multi-tenant backend architecture implementing database-sharing with tenant isolation using Hibernate filters and ThreadLocal propagation.

## Isolation Details

### ThreadLocal Safety
ThreadLocals map data to execution threads. Since Servlet container pools reuse threads, failing to clean the contexts produces serious security breaches where requests inherit preceding contexts:
```
[Request] -> [Filter] -> Set TenantContext -> [Execute Controller] -> Clear (Finally Block)
```
