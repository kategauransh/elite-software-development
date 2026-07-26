# High-Throughput Wallet & Ledger System

A transactional, double-entry financial ledger built with Java and Spring Boot JPA, optimized for concurrency using optimistic locking.

## Locking Strategies

### Optimistic Locking
Optimistic locking avoids locking rows at the database level. Instead, the application verifies that no other transaction modified the record since it was read, using a `@Version` field.
* **Pros:** Ideal for read-heavy systems; scales well with low-to-medium conflicts.
* **Cons:** Transactions fail on write collisions, requiring application-level retries.

### Double-Entry Rule validation
Every balance modification produces matching debits and credits:
\[ \sum \text{Debits} == \sum \text{Credits} \]
